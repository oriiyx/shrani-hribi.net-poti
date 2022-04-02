package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterEntity extends ArrayAdapter<Entity> {

    private Activity activity;
    private ArrayList<Entity> entity;
    private static LayoutInflater inflater = null;

    public AdapterEntity(Activity activity, int resource, ArrayList<Entity> _entity) {
        super(activity, resource, _entity);

        try {
            this.activity = activity;
            this.entity = _entity;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    @Override
    public int getCount() {
        return entity.size();
    }


    public Entity getItem(Entity position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView title;
        public TextView id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.all_entry_listview_layout, null);
                holder = new ViewHolder();

                holder.title = (TextView) vi.findViewById(R.id.entityListTitle);
                holder.id = (TextView) vi.findViewById(R.id.entityListId);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.title.setText(entity.get(position).getEntityName());
            holder.id.setText(entity.get(position).getId());

        } catch (Exception e) {
            Log.d("Error", e.toString());
        }

        return vi;
    }
}
