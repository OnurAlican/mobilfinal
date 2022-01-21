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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class kayitol extends AppCompatActivity {

    EditText email, sifre;
    Button buton;
    FirebaseAuth auth;
    TextView hesabimVar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitol);
        tanimla();

    }

    public void tanimla() {
        email = (EditText) findViewById(R.id.email);
        sifre = (EditText) findViewById(R.id.sifre);
        buton = (Button) findViewById(R.id.buton);
        auth = FirebaseAuth.getInstance();
        hesabimVar = (TextView) findViewById(R.id.hesabimVar);


        buton.setOnClickListener(v -> {
            String inputemail = email.getText().toString();
            String pass = sifre.getText().toString();
            if (!inputemail.equals("") && !pass.equals("")) {
                if (isValid(inputemail)) {
                    kayittOl(inputemail, pass);
                    email.setText("");
                    sifre.setText("");
                } else
                    Toast.makeText(getApplicationContext(), "Geçerli bir email adresi giriniz", Toast.LENGTH_LONG).show();

            } else
                Toast.makeText(getApplicationContext(), "İstenilen alanlar boş bırakılamaz.", Toast.LENGTH_LONG).show();

        });
        hesabimVar.setOnClickListener(v -> {
            Intent intent = new Intent(kayitol.this, girisYapma.class);
            startActivity(intent);
            finish();
        });
    }

    public static boolean isValid(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }


    public void kayittOl(String email, String siffre) {
        auth.createUserWithEmailAndPassword(email, siffre).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference().child("Kullanıcılar").child(auth.getUid());
                Map map = new HashMap();
                map.put("resim", "null");
                map.put("isim", "null");
                map.put("telefon", "null");
                map.put("bolum", "null");
                map.put("sınıf", "null");
                map.put("dogumtarih", "null");
                map.put("state",false);
                reference.setValue(map);
                Toast.makeText(getApplicationContext(), "Üyeliğiniz tamamlandı.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(kayitol.this, girisYapma.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Kayıt alınamadı.", Toast.LENGTH_LONG).show();
            }
        });
    }
}