package com.example.cassie.myvolts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.testing.TestingTasksSelection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cassie on 23/05/2017.
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.pager)
    ViewPager viewpager;

    private int[] pager = new int[]{R.drawable.s1, R.drawable.s2, R.drawable.s3};
    private List<View> pagerview = new ArrayList<>();
    DbHelp dbHelp;

/*    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        for (int i = 0; i < pager.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_pager, null);
            ImageView img=(ImageView) view.findViewById(R.id.img);
            img.setImageResource(pager[i]);
            if (i == pager.length - 1) {
                View buttion = view.findViewById(R.id.button);
                buttion.setVisibility(View.VISIBLE);
                buttion.setOnClickListener(this);
            }else{
                View skip = view.findViewById(R.id.skip);
                skip.setVisibility(View.VISIBLE);
                skip.setOnClickListener(this);
            }
            pagerview.add(view);
        }

        viewpager.setAdapter(new WPagerAdapter());

        dbHelp = new DbHelp(this);
        dbHelp.getInitData();
        /*sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();*/
    }


    @Override
    public void onClick(View v) {
        forwardToMainActivity();
    }

    private void forwardToMainActivity() {
        Intent main = new Intent(WelcomeActivity.this, SearchActivity.class);
        startActivity(main);
        finish();
    }

    private void forwardToTestingActivity() {
        Intent main = new Intent(WelcomeActivity.this, TestingTasksSelection.class);
        startActivity(main);
        finish();
    }

    /*private void isTestMode(String istesting){
        TestUtil.clearSharedPreference(editor);
        editor.putString("testmode", istesting);
        editor.commit();
    }*/

    /*private void isGoToTestingMode(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(WelcomeActivity.this);
        builder.setMessage("Testing Mode?");
        builder.setTitle("Confirm");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isTestMode("yes");
                long startTime = System.currentTimeMillis();
                editor.putLong("starttime", startTime);
                editor.commit();
                forwardToTestingActivity();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isTestMode("no");
                forwardToMainActivity();
            }
        });
        builder.create().show();
    }*/


    class WPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagerview.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagerview.get(position));
            return pagerview.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
