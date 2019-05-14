package com.example.cassie.myvolts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;

/**
 * Created by deniseho on 30/01/2019.
 */

public class EmailActivity extends AppCompatActivity {
    @BindView(R.id.email_layout)
    LinearLayout email_layout;

    private EditText mailTo;
    private EditText mailSubject;
    private EditText mailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        mailTo = (EditText) findViewById(R.id.edit_text_id);
        mailSubject = (EditText) findViewById(R.id.edit_text_subject);
        mailMessage = (EditText) findViewById(R.id.edit_text_message);

        Button buttonEmailSend = (Button) findViewById(R.id.email_send);
        Button buttonUpdateData = (Button) findViewById(R.id.update_data);

        buttonEmailSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    sendMail();

                }catch(Exception ex){
                }
                System.out.println("clicked send email");
            }
        });

        buttonUpdateData.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "updatedata", Toast.LENGTH_LONG).show();

            }
        });
    }


    private void sendMail() {

        View email = getLayoutInflater().inflate(R.layout.activity_email, email_layout, false);

        email_layout.addView(email);

        email_layout.setVisibility(View.VISIBLE);
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