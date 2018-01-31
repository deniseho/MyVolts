package com.example.cassie.myvolts.testing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cassie.myvolts.MainActivity;
import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.WelcomeActivity;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.util.TestUtil;

import java.io.Serializable;
import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

public class TestingTasksSelection extends AppCompatActivity {

    private ArrayList<Bean> mList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DbHelp dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_tasks_selection);


        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        TestUtil.clearSharedPreference(editor);

        dbHelp = new DbHelp(this);
        dbHelp.listTesting();

        for (int i = 0; i < 5; i ++) {
            mList.add(new Bean());
        }

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(TestingTasksSelection.this, R.layout.item, null);
                    holder.tv = (TextView) convertView.findViewById(R.id.tv);
                    holder.cb = (SmoothCheckBox) convertView.findViewById(R.id.scb);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                final Bean bean = mList.get(position);
                holder.cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        bean.isChecked = isChecked;

                        if(isChecked == true){
                            editor.putInt("task", (position+1));
                            long startTime = System.currentTimeMillis();
                            editor.putLong("starttime", startTime);
                            editor.commit();
                            Intent intent = new Intent(TestingTasksSelection.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                String text = "";
                if(position == 4){
                    text = "Skip";
                }else {
                    text = getString(R.string.string_item_subffix) + (position + 1);
                }
                holder.tv.setText(text);
                holder.cb.setChecked(bean.isChecked);

                return convertView;
            }

            class ViewHolder {
                SmoothCheckBox cb;
                TextView tv;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bean bean = (Bean) parent.getAdapter().getItem(position);
                bean.isChecked = !bean.isChecked;
                SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.scb);
                checkBox.setChecked(bean.isChecked, true);
            }
        });
    }

    class Bean implements Serializable {
        boolean isChecked;
    }

}
