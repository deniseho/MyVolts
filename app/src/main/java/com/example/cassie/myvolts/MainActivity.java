package com.example.cassie.myvolts;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;

import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.fragment.SearchFragment;
import com.example.cassie.myvolts.util.DensityUtil;

import butterknife.ButterKnife;

/**
 * Created by cassie on 23/05/2017.
 */

public class MainActivity extends AppCompatActivity {

    //    Button click;
//        public  static TextView data;
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//
//            click = (Button) findViewById(R.id.button);
//            data = (TextView) findViewById(R.id.fetcheddata);
//
//            click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    fetchData process = new fetchData();
//                    process.execute();
//                }
//            });
//
//        }



    DbHelp dbHelp;

//    @BindView(R.id.main)
//    RadioButton main;
//    @BindView(R.id.hot)
//    RadioButton scanner;
//    @BindView(R.id.set)
//    RadioButton contact;

    String made, type, model;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        initDrawable(main);
//        initDrawable(scanner);
//        initDrawable(contact);

        dbHelp = new DbHelp(this);

        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        made = this.getIntent().getStringExtra("madeInput");
        type = this.getIntent().getStringExtra("deviceInput");
        model = this.getIntent().getStringExtra("modelInput");
        if(made != null && type != null && model != null){
            SearchFragment searchFragment = new SearchFragment();

            Bundle bundle = new Bundle();
            bundle.putString("madeInput", made);
            bundle.putString("deviceInput", type);
            bundle.putString("modelInput", model);
            searchFragment.setArguments(bundle);

            changeFragment(searchFragment);
          }

    }

    public void initDrawable(RadioButton v){
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0,0, DensityUtil.dp2px(this,30),DensityUtil.dp2px(this,30));
        v.setCompoundDrawables(null,drawable,null,null);
    }


//    @OnClick({R.id.main, R.id.hot, R.id.set})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.main:
//                SearchFragment searchFragment = new SearchFragment();
//                changeFragment(searchFragment);
//                break;
//            case R.id.hot:
//                //changeFragment(new HotFragment());
//                TestUtil.storeSearchClicks(sharedPreferences, editor, "scanBtnClick");
//                Intent intent = new Intent(MainActivity.this,
//                        CaptureActivity.class);
//                startActivityForResult(intent, 0);
//                break;
//            case R.id.set:
//                //changeFragment(new SetFragment());
//                String phone = "014407407";
//
//                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
//                startActivity(dialIntent);
//
//                break;
//        }
//    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.content,fragment);
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == 0 && resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Intent intent = new Intent(MainActivity.this,
                        ProductDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }



}
