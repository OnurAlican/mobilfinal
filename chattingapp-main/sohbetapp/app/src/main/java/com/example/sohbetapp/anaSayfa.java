package com.example.sohbetapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.Change;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class anaSayfa extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    private ChangeFragment changeFragment;
    private String userId;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (item) -> {
        switch(item.getItemId())
        {
            case R.id.navigation_home:
                changeFragment.change(new fragment1());
                return true;
            case R.id.navigation_dashboard:
                changeFragment.change(new BildirimFragment());
                return true;
            case R.id.navigation_profil:
                changeFragment.change(new kullaniciProfilFragment());
                return true;
            case R.id.navigation_exit:
                cik();
                return true;



        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tanimla();
        kontrol();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);

        changeFragment=new ChangeFragment(anaSayfa.this);
        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }
    public void tanimla()
    {
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        userId=user.getUid();

    }
    public void kontrol()
    {
        if(user==null)
        {
            Intent intent=new Intent(anaSayfa.this,girisYapma.class);
            startActivity(intent);
            finish();

        }
        else
        {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
            reference.child(user.getUid()).child("state").setValue(true);
        }

    }
    public void cik()
    {
        auth.signOut();
        Intent intent=new Intent(anaSayfa.this,girisYapma.class);
        startActivity(intent);
        finish();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
        reference.child(user.getUid()).child("state").setValue(false);

    }
    protected void onStop()
    {
        super.onStop();;
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
        reference.child(user.getUid()).child("state").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
        reference.child(user.getUid()).child("state").setValue(true);
    }
}