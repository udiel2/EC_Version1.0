package com.application.easycook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Properties;
import android.se.omapi.Session;
import android.widget.Toast;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

    // Fields --------------------------------------------------------------------------------------
    ImageView F_return;
    TextView F_txt3;
    EditText F_mail;
    Button F_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        F_return=findViewById(R.id.F_img_2);
        F_txt3=findViewById(R.id.F_txt_3);
        F_button = findViewById(R.id.F_btn_1);
        F_mail = findViewById(R.id.F_email);

        F_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        F_txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        F_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userlog = "easycooksce@gmail.com";
                final String passlog = "evil123!";
                String message = "Did you forgot your password? TEST";
                Properties props = new Properties();
                props.put("mail.smtp.auth","true");
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.port","587");
                javax.mail.Session session = javax.mail.Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            @Override
                            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                return new javax.mail.PasswordAuthentication(userlog, passlog);
                            }
                        });
                try {
                    Message msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(F_mail.getText().toString()));
                    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(F_mail.getText().toString()));
                    msg.setSubject("Test Easycook");
                    msg.setText(message);
                    Transport.send(msg);
                    Toast.makeText(getApplicationContext(),"email send successfully", Toast.LENGTH_LONG).show();
                }catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    } // gmail activer acces aux applications moins securisees



}