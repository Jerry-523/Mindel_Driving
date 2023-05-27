package com.kabulozu.mindeldriving;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btn;
    private EditText loginEmail, loginPassword;
    private TextView register;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn = findViewById(R.id.ButtonLogin);
        register = findViewById(R.id.register);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.LoginProgressBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setStatusBarColor(Color.parseColor("#FF000000"));
        IniciarComponentes();

        btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();
                    if(email.length() == 0 || password.length() == 0){
                        Toast.makeText(getApplicationContext(), "Por favor preencher todos os campos!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AutenticarUsuario();
                        Toast.makeText(getApplicationContext(), "Login com Sucesso",Toast.LENGTH_SHORT).show();
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

    private void AutenticarUsuario(){
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    }, 3000);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,DriverMapsActivity.class);
        startActivity(intent);
        finish();
    }
    private void IniciarComponentes(){
        btn = findViewById(R.id.ButtonLogin);
        register = findViewById(R.id.register);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.LoginProgressBar);
    }
    

}