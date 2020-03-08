package com.example.cassie.myvolts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class NoResultsPageActivity extends AppCompatActivity {
    private static final String TAG = "NoResultsPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_results);
        Toast.makeText(NoResultsPageActivity.this, "update data", Toast.LENGTH_LONG).show();
    }

}
