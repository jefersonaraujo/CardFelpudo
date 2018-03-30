package br.com.ineedsolutions.htools;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private  String uid;
    private  String Usuario;
    private ArrayList<String> seguindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

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
        updateNavigationBarState();


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

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.navigation_dashboard) {
                    startActivity(new Intent(this, DashboardActivity.class));
            } else if (itemId == R.id.navigation_notifications) {
                    startActivity(new Intent(this, NotificationsActivity.class));
            } else if (itemId == R.id.navigation_users){
                    startActivity(new Intent(this,UsersActivity.class));
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();


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
