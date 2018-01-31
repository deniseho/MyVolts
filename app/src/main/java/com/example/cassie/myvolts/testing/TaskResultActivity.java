package com.example.cassie.myvolts.testing;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.util.TestUtil;

public class TaskResultActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_result);


        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        TextView results = (TextView) findViewById(R.id.result);
        results.setText(TestUtil.displayResult(sharedPreferences));
    }
}
