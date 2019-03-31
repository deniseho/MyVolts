package com.example.cassie.myvolts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NoResultsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_results_page);

        Button buttonSend = (Button) findViewById(R.id.updateDataButton);
        buttonSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                System.out.println("clicked updateDataButton");
            }
        });
    }

}
