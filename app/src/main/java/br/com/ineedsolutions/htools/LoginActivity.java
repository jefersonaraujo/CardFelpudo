package br.com.ineedsolutions.htools;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth nAuth;
    private EditText editEMail;
    private EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        nAuth = FirebaseAuth.getInstance();
        editEMail = findViewById(R.id.campoEmail);
        editSenha = findViewById(R.id.campoSenha);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = nAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private  void updateUI(FirebaseUser user){
        if(user != null){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }
    public void login(View view){
        String email = editEMail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if(email.equals("")){
            editEMail.setError("Preencha este Campo !");
            return;
        }
        if(senha.equals("")){
            editSenha.setError("Preencha este Campo !");
            return;
        }
        nAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI(nAuth.getCurrentUser());
                }else{
                    try{
                        throw  task.getException();
                    }catch (Exception e){
                        Log.e("Login", e.getMessage());
                    }
                    Toast.makeText(LoginActivity.this, "Usuário ou Senha Incorreto !", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }


            }
        });

    }
}
