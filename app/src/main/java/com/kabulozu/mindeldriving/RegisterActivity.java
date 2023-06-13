package com.kabulozu.mindeldriving;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    TextView login;
    EditText edit_nome, edit_email, edit_senha, edit_movel;
    Button bt_cadastrar;
    String[] mensagem = {"Preencha todos os campos", "Cadastro com sucesso"};
    String usuarioID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setStatusBarColor(Color.parseColor("#FF000000"));
        IniciarComponentes();

        bt_cadastrar.setOnClickListener(view -> {
            String nome = edit_nome.getText().toString();
            String movel = edit_movel.getText().toString();
            final String email = edit_email.getText().toString();
            final String senha = edit_senha.getText().toString();
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, mensagem[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{

                mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(RegisterActivity.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                    }else{
                        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                        current_user_db.setValue(true);
                    }
                });
                //CadastrarUsuario(view);
            }
        });

        login = findViewById(R.id.login_here);

        login.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));
    }



    private void CadastrarUsuario(View view){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                SalvarDadosUsuario();
                Snackbar snackbar = Snackbar.make(view, mensagem[1],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                }catch (FirebaseAuthWeakPasswordException e){
                    erro = "Digite uma senha com no minimo 6 caracteres";
                }catch (FirebaseAuthUserCollisionException e){
                    erro = "Essa conta ja existe";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    erro = "E-mail Invalido";
                }catch (Exception e) {
                    erro = "Erro ao Cadastrar";
                }
                Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }
    private void SalvarDadosUsuario(){
        String nome = edit_nome.getText().toString();
        String movel = edit_movel.getText().toString();
        String email = edit_email.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("telemovel", movel);
        usuarios.put("email", email);
        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(aVoid -> Log.d("db", "Sucesso ao salvar dados"))
        .addOnFailureListener(e -> Log.d("db_error", "Erro ao salvar os dados" + e));

        DocumentReference documentReference2 = db.collection("driverAvailable").document(usuarioID);
        documentReference2.set(usuarios).addOnSuccessListener(aVoid -> Log.d("db", "Sucesso ao salvar dados"))
        .addOnFailureListener(e -> Log.d("db_error", "Erro ao salvar os dados" + e));
    }
    private void IniciarComponentes(){
        edit_nome = findViewById(R.id.nomeRegister);
        edit_email = findViewById(R.id.emailRegister);
        edit_senha = findViewById(R.id.passwordRegister);
        edit_movel = findViewById(R.id.movelRegister);
        bt_cadastrar = findViewById(R.id.buttonRegister);
    }
}