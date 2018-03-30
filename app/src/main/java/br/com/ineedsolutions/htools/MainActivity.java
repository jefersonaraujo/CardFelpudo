package br.com.ineedsolutions.htools;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private  String uid;
    private  String Usuario;
    private ArrayList<String> seguindo;



    //private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new  Intent(MainActivity.this, CadastroUserActivity.class);
//                    mTextMessage.setText(R.string.title_home);
                      return true;
                case R.id.navigation_dashboard:

                    Intent j = new Intent(MainActivity.this, CadastroUserActivity.class);
                    startActivity(j);
                    return true;
                case R.id.navigation_notifications:
                    Intent l = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(l);

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("nomes");
        myRef.setValue("Hello World");

        /*Comecar*/
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        seguindo = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) finish();

    }
    private void getUserInfo(){
        uid = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("users/"+ uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario = dataSnapshot.child("usuario").getValue(String.class);
                seguindo.clear();
                for(DataSnapshot s :dataSnapshot.child("seguindo").getChildren()){
                    seguindo.add(s.getValue(String.class));

                }
                Log.d("usuario",Usuario);
                Log.d("lista",seguindo.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sair){
            mAuth.signOut();
            finish();
        }
        return  true;
    }
}
