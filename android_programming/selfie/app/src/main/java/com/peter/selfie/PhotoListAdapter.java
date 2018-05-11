package com.peter.selfie;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;


public class PhotoListAdapter extends ArrayAdapter<PhotoListAdapter.RowObj> {

    private final Activity context;
    private final LayoutInflater inflater;

    public PhotoListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<RowObj> objects) {
        super(context, textViewResourceId, objects);
        this.context = (Activity) context;
        this.inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.photo_list, null);
        ImageView imageView = view.findViewById(R.id.item_icon);
        imageView.setImageURI(getItem(position).img);
        TextView textView = view.findViewById(R.id.item_name);
        textView.setText(getItem(position).name);
        return view;
    }

    public static class RowObj {
        public long ID;
        public String name;
        public String fullName;
        public Uri img;

        public RowObj(long id, String name, Uri img, String fullName) {
            this.ID = id;
            this.name = name;
            this.img = img;
            this.fullName = fullName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RowObj rowObj = (RowObj) o;
            return ID == rowObj.ID;
        }

        @Override
        public int hashCode() {

            return Objects.hash(ID);
        }
    }
}
