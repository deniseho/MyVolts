package com.example.cassie.myvolts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.dto.ProductData;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductAdapter extends DataAdapter<ProductData> {
    public ProductAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ProductData item = getItem(position);
        holder=new ViewHolder(convertView);
        holder.itemName.setText(item.getName());
        holder.made.setText(item.getDesc());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.made)
        TextView made;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
