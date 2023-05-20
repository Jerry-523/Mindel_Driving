package com.kabulozu.mindeldriving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btn;
    EditText loginEmail, loginPassword;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn = findViewById(R.id.ButtonLogin);
        register = findViewById(R.id.register);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();
                    if(email.length() == 0 || password.length() == 0){
                        Toast.makeText(getApplicationContext(), "Por favor preencher todos os campos!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Login Success",Toast.LENGTH_SHORT).show();
                    }

                }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
/*
    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
    startActivity(intent);
    finish();
*/

}