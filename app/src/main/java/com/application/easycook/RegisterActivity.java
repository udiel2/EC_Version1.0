package com.application.easycook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    Connection connect;
    String ConnectionResult="";
    // Fields --------------------------------------------------------------------------------------
    ImageView R_return;
    TextView R_txt3;
    EditText R_fname,R_lname,R_email,R_password,R_repassword;
    Button R_btn;
    connectionHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        R_return=findViewById(R.id.R_img_2); // Arrow to return
        R_txt3=findViewById(R.id.R_txt_3); // To signin page
        R_fname=findViewById(R.id.R_fname); // first name field
        R_lname=findViewById(R.id.R_lname); // last name field
        R_email=findViewById(R.id.R_email); // email field
        R_password=findViewById(R.id.R_pass); // pass field
        R_repassword=findViewById(R.id.R_repass); // repass field
        R_btn=findViewById(R.id.R_btn_1); // btn to validate


        R_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InitActivity.class);
                startActivity(intent);
            }
        }); // goBack to Main

        R_txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }); // Go to SignIn

        R_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user= R_email.getText().toString();
                String pass= R_password.getText().toString();
                String repass= R_repassword.getText().toString();
                String fname= R_fname.getText().toString();
                String lname= R_lname.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass) || TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname))
                    Toast.makeText(RegisterActivity.this, "All fields Required", Toast.LENGTH_SHORT).show();
                else {
                    if(pass.equals(repass)) {
                        SQLregister(user, pass, fname, lname);
                        //GetTextFromSQL(view);
                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), InitActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    } // End of OnCreate


    protected void SQLregister(String email, String pass, String fname, String lname) {
        try{
            connectionHelper connectionHelper= new connectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!=null){
                String query = "INSERT INTO USERS (email, password, firstname, lastname) VALUES ('" + email +"','"+ pass +"','"+ fname + "','"+ lname +"')";
                Statement st = connect.createStatement();
                st.executeUpdate(query);
            }else
            {
                ConnectionResult="Check Connection";
            }

        }
        catch (Exception ex){

        }
    }
   /*public void GetTextFromSQL(View v){
       R_email = findViewById(R.id.R_email);
       R_password=findViewById(R.id.R_pass);
       R_fname=findViewById(R.id.R_fname);
       R_lname=findViewById(R.id.R_lname);
        try{
            connectionHelper connectionHelper= new connectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!=null){
                String query = "INSERT INTO USERS";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    R_email.setText(rs.getString(1));
                    R_password.setText(rs.getString(2));
                    R_fname.setText(rs.getString(3));
                    R_lname.setText(rs.getString(4));
                }
            }else
            {
                ConnectionResult="Check Connection";
            }

        }
        catch (Exception ex){

        }

    }*/

} // End Class

