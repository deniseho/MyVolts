package com.example.cassie.myvolts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoResultsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_results_page);
        Toast.makeText(NoResultsPageActivity.this, "update data", Toast.LENGTH_LONG).show();

        Button buttonSend = (Button) findViewById(R.id.updateData);
        buttonSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
          Toast.makeText(NoResultsPageActivity.this, "update data", Toast.LENGTH_LONG).show();

            }
        });
    }

}
