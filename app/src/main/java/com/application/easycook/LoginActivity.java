package com.application.easycook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;

public class LoginActivity extends AppCompatActivity {

    Connection connect;
    String ConnectionResult="";
    private FirebaseAuth mAuth;
    // Fields --------------------------------------------------------------------------------------
    ImageView L_return;
    TextView L_forget, L_txt3;
    Button L_button;
    EditText L_email, L_password;
    ProgressBar L_pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        L_return=findViewById(R.id.L_img_2);
        L_forget=findViewById(R.id.L_txt_2);
        L_txt3=findViewById(R.id.L_txt_3);
        L_email = findViewById(R.id.L_email);
        L_password = findViewById(R.id.L_pass);
        L_button = findViewById(R.id.L_btn_1);
        L_pbar=findViewById(R.id.L_pbar);

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
                L_pbar.setVisibility(View.VISIBLE);
                String email= String.valueOf(L_email.getText());
                String password= String.valueOf(L_password.getText());
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                    Toast.makeText(LoginActivity.this, "All fields Required", Toast.LENGTH_SHORT).show();
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    L_pbar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this, "Authentication with Success.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*boolean checkemail(String mail) {
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
    } // checkpassword end.*/

}