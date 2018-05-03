package com.peter.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;

    private View leftUp, leftBottom, rightUp, rightMiddle, rightBottom;

    private static final int leftUpColor = Color.parseColor("#555ea1");
    private static final int leftBottomColor = Color.parseColor("#c03580");
    private static final int rightUpColor = Color.parseColor("#8a101b");
    private static final int rightMiddleColor = Color.parseColor("#d5d6d6");
    private static final int rightBottomColor = Color.parseColor("#1e2781");

    private static final int leftUpColorEnd = Color.parseColor("#FFF3F5A3");
    private static final int leftBottomColorEnd = Color.parseColor("#FFF1D781");
    private static final int rightUpColorEnd = Color.parseColor("#FFF09E1C");
    private static final int rightBottomColorEnd = Color.parseColor("#FFA9BF80");

    private static final float[] from1 = new float[3];
    private static final float[] from2 = new float[3];
    private static final float[] from3 = new float[3];
    private static final float[] from5 = new float[3];

    private static final float[] to1 = new float[3];
    private static final float[] to2 = new float[3];
    private static final float[] to3 = new float[3];
    private static final float[] to5 = new float[3];

    private AlertDialogFragment mDialog;
    private WebView mWebview;
    private boolean isWebViewerOpen = false;
    private static final String URL = "https://www.moma.org/#home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebview = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        leftUp = findViewById(R.id.left_up);
        leftBottom = findViewById(R.id.left_bottom);
        rightUp = findViewById(R.id.right_up);
        rightMiddle = findViewById(R.id.right_middle); // while color won't change
        rightBottom = findViewById(R.id.right_bottom);

        leftUp.setBackgroundColor(leftUpColor);
        leftBottom.setBackgroundColor(leftBottomColor);
        rightUp.setBackgroundColor(rightUpColor);
        rightMiddle.setBackgroundColor(rightMiddleColor);
        rightBottom.setBackgroundColor(rightBottomColor);

        Color.colorToHSV(leftUpColor, from1);
        Color.colorToHSV(leftUpColorEnd, to1);
        Color.colorToHSV(leftBottomColor, from2);
        Color.colorToHSV(leftBottomColorEnd, to2);
        Color.colorToHSV(rightUpColor, from3);
        Color.colorToHSV(rightUpColorEnd, to3);
        Color.colorToHSV(rightBottomColor, from5);
        Color.colorToHSV(rightBottomColorEnd, to5);

        seekBar = findViewById(R.id.seekbar_def);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                leftUp.setBackgroundColor(getHsv(progress, from1, to1));
                leftBottom.setBackgroundColor(getHsv(progress, from2, to2));
                rightUp.setBackgroundColor(getHsv(progress, from3, to3));
                rightBottom.setBackgroundColor(getHsv(progress, from5, to5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private int getHsv(int progress, float[] from, float[] to) {
        float[] hsv = new float[3];
        for (int i = 0; i < 3; i++) {
            hsv[i] = (progress * (to[i] - from[i]) / 100 + from[i]);
        }
        return Color.HSVToColor(hsv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Get a reference to the MenuInflater
        MenuInflater inflater = getMenuInflater();

        // Inflate the menu using activity_menu.xml
        inflater.inflate(R.menu.menu, menu);

        // Return true to display the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        mDialog = AlertDialogFragment.newInstance();
        mDialog.setActivity(this);
        mDialog.show(getFragmentManager(), "");
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isWebViewerOpen) {
            isWebViewerOpen = false;
            backToLayout1();
        } else {
            super.onBackPressed();
        }

    }

    private void backToLayout1() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        private MainActivity activity;

        public void setActivity(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(Html.fromHtml("<p style=\"    text-align: center;\">Inspired by the works of artists such as Piet Mondrian and Ben Nicholson.</p>" +
                            "<p style=\"    text-align: center;\">Click below to learn more!</p>"))
                    .setCancelable(true)

                    .setPositiveButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    activity.isWebViewerOpen = true;
                                    activity.mWebview.loadUrl(URL);
                                    activity.setContentView(activity.mWebview);
                                }
                            })
                    .setNegativeButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    AlertDialogFragment.this.getDialog().cancel();
                                }
                            }).create();
        }
    }
}
