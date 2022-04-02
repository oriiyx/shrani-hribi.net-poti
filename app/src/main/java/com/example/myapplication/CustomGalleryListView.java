package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomGalleryListView extends ArrayAdapter<EntryGallery> {

    private ArrayList<EntryGallery> entryGalleryArrayList;

    private Activity context;

    public CustomGalleryListView(Activity context, ArrayList<EntryGallery> entryGalleryArrayList) {
        super(context, R.layout.gallery_listview_layout, entryGalleryArrayList);

        this.context = context;
        this.entryGalleryArrayList = entryGalleryArrayList;
    }

    class ViewHolder {
        TextView description;
        ImageView imageView;
        TextView imageID;

        ViewHolder(View view) {
            description = (TextView) view.findViewById(R.id.entryGallerySingleDesc);
            imageView = (ImageView) view.findViewById(R.id.entryGallerySingleImage);
            imageID = (TextView) view.findViewById(R.id.entryGallerySingleImageID);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gallery_listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.imageView.setImageBitmap(entryGalleryArrayList.get(position).image);
        viewHolder.description.setText(entryGalleryArrayList.get(position).description);
        viewHolder.imageID.setText(Integer.toString(entryGalleryArrayList.get(position).imageID));

        return r;
    }
}
