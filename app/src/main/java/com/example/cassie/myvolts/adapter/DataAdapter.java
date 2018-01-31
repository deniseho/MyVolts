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



public class DataAdapter<T extends DataBase> extends BaseAdapter{

    private List<T> datas;

    public Context context;

    private DbHelp dbHelp;

    public DataAdapter(Context context) {
        this.context = context;
        dbHelp = new DbHelp(context);
    }



    public void setDatas(List<T> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addDatas(String data){
        datas.add((T) new HistoryData(data));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false);
        }
        T item = getItem(position);
        ViewHolder holder=new ViewHolder(convertView);
        holder.itemName.setText(item.getItemName());
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
