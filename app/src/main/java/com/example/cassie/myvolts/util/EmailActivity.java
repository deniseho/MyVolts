package com.example.cassie.myvolts.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cassie.myvolts.R;

public class EmailActivity extends AppCompatActivity {
    private EditText mailTo;
    private EditText mailSubject;
    private EditText mailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        mailTo = (EditText) findViewById(R.id.edit_text_id);
        mailSubject = (EditText) findViewById(R.id.edit_text_subject);
        mailMessage = (EditText) findViewById(R.id.edit_text_message);

        Button buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendMail();
            }
        });
    }

    private void sendMail() {
        String recipientList = mailTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = mailSubject.getText().toString();
        String message = mailMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rcf822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
    }

}
