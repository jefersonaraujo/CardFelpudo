package br.com.ineedsolutions.htools;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CadastroUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editEmail;
    private EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user_activity);

        mAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.cadEmail);
        editSenha = findViewById(R.id.cadSenha);
    }
    public  void salvar(View view){
        final String usuario = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        if(usuario.equals("")){
            editEmail.setError("Preencha este Campo !");
            editEmail.requestFocus();
            return;
        }
        if(senha.equals("")){
            editSenha.setError("Preencha este Campo !");
            editSenha.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(usuario,senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           FirebaseUser user = mAuth.getCurrentUser();
                           FirebaseDatabase database = FirebaseDatabase.getInstance();
                           DatabaseReference userRef = database.getReference("users/" + user.getUid());

                           Map<String, Object> userInfos = new HashMap<>();
                           userInfos.put("usuario",usuario);
                           userInfos.put("email",usuario);
                           userRef.setValue(userInfos);
                           finish();

                       }else{
                           try{
                               throw  task.getException();

                           }catch (FirebaseAuthWeakPasswordException e){
                               editSenha.setError("Senha Franca");
                               editSenha.requestFocus();
                           }catch (FirebaseAuthInvalidCredentialsException e){
                               editEmail.setError("Email invalido !");
                               editEmail.requestFocus();
                           }catch (FirebaseAuthUserCollisionException e){
                               editEmail.setError("Email ja existe !");
                               editEmail.requestFocus();
                           }catch (Exception e){
                               Log.e("Cadastro", e.getMessage());
                           }
                       }
                    }
                });
    }
}
