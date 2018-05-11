package com.peter.selfie;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoFragment.ListSelectionListener {


    private static final String TAG = "SelfieActivity";

    public static final int REQUEST_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String EXTRA_URI = "extra_uri";
    private static final String EXTRA_NAME = "extra_name";


    private String fullFileName;
    private String fileName;
    private Uri photoURI;

    private PhotoListAdapter photoListAdapter;
    private ListView list;
    private List<PhotoListAdapter.RowObj> photoList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        photoListAdapter = new PhotoListAdapter(this, R.layout.photo_list, photoList);
        list.setAdapter(photoListAdapter);
        registerForContextMenu(list);
        photoListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHistoryPhoto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get a reference to the MenuInflater
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu using activity_menu.xml
        inflater.inflate(R.menu.menu, menu);

        // Return true to display the menu
        menu.findItem(R.id.menu_camera).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dispatchTakePictureIntent();
                return true;
            }
        });
        return true;
    }

    // Create Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                deletePhoto(info.id);
                Toast.makeText(getApplicationContext(), "img deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    private void deletePhoto(long id) {
        final PhotoListAdapter.RowObj photo = photoList.get((int) id);
        photoList.remove(photo);
        photoListAdapter.notifyDataSetChanged();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equals(photo.fullName);
            }
        });
        for (File f : files) {
            f.delete();
        }
    }

    private void initHistoryPhoto() {
        photoList.clear();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith("jpg");
            }
        });

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return -1 * o1.getName().compareTo(o2.getName());
            }
        });
        for (File file : files) {
            Uri uri = FileProvider.getUriForFile(this,
                    "com.peter.selfie.provider",
                    file);
            if (file.length() == 0) {
                file.delete();
            } else {
                photoList.add(new PhotoListAdapter.RowObj(System.currentTimeMillis(), file.getName().substring(0, 15), uri, file.getName()));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photoList.add(0, new PhotoListAdapter.RowObj(System.currentTimeMillis(), fileName, photoURI, fullFileName));
        } else if (resultCode == RESULT_CANCELED) {
            // User Cancelled the action
//            mCurrentPhotoPath = null;
            Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            String name = "";
            try {
                // Create an image file name
                name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile(name, ".jpg", storageDir);
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.peter.selfie.provider",
                        photoFile);
                fileName = name;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(EXTRA_NAME, name);
                takePictureIntent.putExtra(EXTRA_URI, photoFile.getAbsolutePath());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onListSelection(int index) {

    }
}
