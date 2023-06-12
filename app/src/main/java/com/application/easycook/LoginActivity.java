package com.application.easycook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    Connection connect;
    String ConnectionResult="";
    // Fields --------------------------------------------------------------------------------------
    ImageView L_return;
    TextView L_forget, L_txt3;
    Button L_button;
    EditText L_email, L_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        L_return=findViewById(R.id.L_img_2);
        L_forget=findViewById(R.id.L_txt_2);
        L_txt3=findViewById(R.id.L_txt_3);
        L_email = findViewById(R.id.L_email);
        L_password = findViewById(R.id.L_pass);
        L_button = findViewById(R.id.L_btn_1);

        L_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InitActivity.class);
                startActivity(intent);
            }
        });

        L_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        L_txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        L_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstr = L_email.getText().toString();
                boolean emailcheck = checkemail(emailstr);
                if (emailcheck == false) {
                    Toast.makeText(LoginActivity.this, "This Email not exist!", Toast.LENGTH_SHORT).show();
                } else {
                    String passwordstr = L_password.getText().toString();
                    boolean passwordcheck = checkpassword(emailstr, passwordstr);
                    if (passwordcheck == false) {
                        Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }

                }

            }
        });


    }

    boolean checkemail(String mail) {
        boolean tag = false;
        try{
            connectionHelper connectionHelper= new connectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!=null){
                String query = "SELECT email FROM USERS WHERE email = " + mail;
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    tag = true;
                    return tag;
                } else {
                    return tag;
                }
            }else
            {
                ConnectionResult="Check Connection";
            }
        }
        catch (Exception ex){
        }
      return tag;
    } // checkemail end.

    boolean checkpassword(String mail, String pass) {
        boolean tag = false;
        try{
            connectionHelper connectionHelper= new connectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!=null){
                String query = "SELECT email, password FROM USERS WHERE email = " + mail + "AND password = " + pass;
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    tag = true;
                    return tag;
                } else {
                    return tag;
                }
            }else
            {
                ConnectionResult="Check Connection";
            }
        }
        catch (Exception ex){
        }
        return tag;
    } // checkpassword end.

}