package com.example.cassie.myvolts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.dto.HotData;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HotAdapter extends DataAdapter<HotData> {
    public HotAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hot, parent, false);
        }
        HotData item = getItem(position);
        ViewHolder holder=new ViewHolder(convertView);
        holder.itemName.setText(item.name);
        holder.price.setText(item.price);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
