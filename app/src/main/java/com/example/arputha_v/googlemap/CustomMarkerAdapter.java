package com.example.arputha_v.googlemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arputha_v on 3/9/2018.
 */

public class CustomMarkerAdapter extends BaseAdapter {

    private Context context;
    private List<Locations> locationsList;

    public CustomMarkerAdapter(Context context, List<Locations> locationsList) {
        this.context = context;
        this.locationsList = locationsList;
    }

    @Override
    public int getCount() {
        return locationsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return locationsList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.custom_marker_list_item, viewGroup, false);

        }

        Locations item = locationsList.get(position);
        LinearLayout listItemContainer = rowView.findViewById(R.id.list_item_container);
        TextView tvTitle = rowView.findViewById(R.id.tv_title);
        TextView tvDescription = rowView.findViewById(R.id.tv_description);
        ImageView ivMarker = rowView.findViewById(R.id.iv_marker);

        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        if (!"".equals(item.getLocationImage())) {
            Picasso.get()
                    .load(item.getLocationImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(ivMarker);
        } else {
            ivMarker.setImageResource(R.mipmap.ic_launcher);
        }

        return rowView;
    }
}
