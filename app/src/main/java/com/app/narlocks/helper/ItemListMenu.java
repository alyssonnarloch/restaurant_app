package com.app.narlocks.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.narlocks.model.Item;
import com.app.narlocks.restaurant_app.R;

import java.util.List;

public class ItemListMenu extends ArrayAdapter<Item> {
    private final int THUMBSIZE = 96;

    private static class ViewHolder {
        ImageView imgIcon;
        TextView name;
        TextView price;
    }

    public ItemListMenu(Context context, List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_menu, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);

        byte[] decodedString = Base64.decode(item.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        viewHolder.name.setText(item.getName());
        viewHolder.price.setText(Extras.brFormat(item.getPrice()));
        viewHolder.imgIcon.setImageBitmap(decodedByte);

        return convertView;
    }
}
