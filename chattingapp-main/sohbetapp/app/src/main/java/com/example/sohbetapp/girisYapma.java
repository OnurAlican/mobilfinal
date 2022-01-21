package com.example.sohbetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class girisYapma extends AppCompatActivity {
    private EditText email_login, sifre_login;
    private Button buton_login;
    private TextView hesabimyok;
    private ChangeFragment changeFragment;
    FirebaseAuth auth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yapma);
        tanimla();


    }

    public void tanimla() {
        email_login = (EditText) findViewById(R.id.email_login);
        sifre_login = (EditText) findViewById(R.id.sifre_login);
        buton_login = (Button) findViewById(R.id.buton_login);
        hesabimyok=(TextView) findViewById(R.id.hesabimyok);
        auth = FirebaseAuth.getInstance();
        buton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_login.getText().toString();
                String pass = sifre_login.getText().toString();
                if (!email.equals("") && !pass.equals("")) {
                    sistemeGiris(email, pass);
                } else {
                    Toast.makeText(getApplicationContext(), "Boş bırakılamaz.", Toast.LENGTH_LONG).show();
                }

            }
        });
        hesabimyok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(girisYapma.this,kayitol.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sistemeGiris(String email, String parola) {
        auth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(girisYapma.this, anaSayfa.class);

                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(getApplicationContext(), "Hatalı bilgi girdiniz.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}