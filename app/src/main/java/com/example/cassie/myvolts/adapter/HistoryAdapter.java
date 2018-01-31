package com.example.cassie.myvolts.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.DataBase;
import com.example.cassie.myvolts.dto.HistoryData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cassie on 10/06/2017.
 */

public class HistoryAdapter extends BaseAdapter {

    private List<HistoryData> datas;

    public Context context;

    private DbHelp dbHelp;

    public HistoryAdapter(Context context) {
        this.context = context;
        dbHelp = new DbHelp(context);
    }



    public void setDatas(List<HistoryData> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public HistoryData getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addDatas(HistoryData data){
        datas.add(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_data_item, parent, false);
        }
        HistoryData item = getItem(position);
        DataAdapter.ViewHolder holder=new DataAdapter.ViewHolder(convertView);
        holder.itemName.setText(item.getName());
        if(holder.delBtn != null) {
            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelp.deleteHisByName(datas.get(position).getItemName());
                    datas.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_name)
        TextView itemName;
        @Nullable
        @BindView(R.id.del)
        ImageView delBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
