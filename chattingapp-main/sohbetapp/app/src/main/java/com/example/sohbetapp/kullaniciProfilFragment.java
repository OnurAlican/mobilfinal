package com.example.sohbetapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sohbetapp.Models.Kullanicilar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class kullaniciProfilFragment extends Fragment {

    String imageUrl;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    View view;
    EditText telno, bolum, sinif, dogumtarihi, kullaniciisim;
    Button kaydetbuton, bilgiArkadasButon;
    CircleImageView profile_image;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    Map map = new HashMap();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);
        tanimla();
        bilgileriGetir();
        return view;
    }

    public void tanimla() {
        kullaniciisim = (EditText) view.findViewById(R.id.kullaniciisim);
        dogumtarihi = (EditText) view.findViewById(R.id.dogumtarihi);
        sinif = (EditText) view.findViewById(R.id.sinif);
        bolum = (EditText) view.findViewById(R.id.bolum);
        telno = (EditText) view.findViewById(R.id.telno);
        kaydetbuton = (Button) view.findViewById(R.id.kaydetbuton);
        bilgiArkadasButon = (Button) view.findViewById(R.id.bilgiArkadasButon);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        kaydetbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }


        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanıcılar").child(user.getUid());

        bilgiArkadasButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new ArkadaslarFragment());
            }
        });


    }

    private void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            String random = RandomString.getSaltString();
            Uri filePath = data.getData();

            StorageReference ref = storageReference.child("kullaniciresimleri").child(random + ".jpg");

            final String[] imageUrl2 = new String[1];


            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {


                        Task<Uri> result = task.getResult().getStorage().getDownloadUrl();
                        result.addOnSuccessListener(uri -> {

                            imageUrl2[0] = uri.toString();
                            Log.i("denem3", "1");
                            Log.i("denem3", imageUrl2[0]);
                            imageUrl = imageUrl2[0];

                        });


                        String isim = kullaniciisim.getText().toString();
                        String dogumtarih = dogumtarihi.getText().toString();
                        String siniff = sinif.getText().toString();
                        String bolumm = bolum.getText().toString();
                        String telefon = telno.getText().toString();

                        Log.i("denem3", "2");

                        reference = database.getReference().child("Kullanıcılar").child(auth.getUid());


                        map.put("isim", isim);
                        map.put("telefon", telefon);
                        map.put("bolum", bolumm);
                        map.put("sınıf", siniff);
                        map.put("dogumtarih", dogumtarih);
                        map.put("resim", "" + imageUrl2[0]);
                        Log.i("denem3", "3");


                        Toast.makeText(getContext(), "Resim başarıyla güncellendi", Toast.LENGTH_LONG).show();


                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("denem3", "5");
                                    ChangeFragment fragment = new ChangeFragment(getContext());
                                    fragment.change(new kullaniciProfilFragment());
                                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Bilgiler güncellenirken hata oluştu", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    else {
                        Toast.makeText(getContext(), "Resim güncelleme başarısız", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }


        }


        public void bilgileriGetir () {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                    kullaniciisim.setText(kl.getIsim());
                    dogumtarihi.setText(kl.getDogumtarih());
                    sinif.setText(kl.getSınıf());
                    bolum.setText(kl.getBolum());
                    telno.setText(kl.getTelefon());
                    imageUrl = kl.getResim();


                    if (!kl.getResim().equals("null")) {
                        Log.i("deneme", kl.getResim());
                        Picasso.get().load(kl.getResim()).into(profile_image);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        public void guncelle () {
            String isim = kullaniciisim.getText().toString();
            String dogumtarih = dogumtarihi.getText().toString();
            String siniff = sinif.getText().toString();
            String bolumm = bolum.getText().toString();
            String telefon = telno.getText().toString();


            reference = database.getReference().child("Kullanıcılar").child(auth.getUid());
            Map map = new HashMap();

            map.put("isim", isim);
            map.put("telefon", telefon);
            map.put("bolum", bolumm);
            map.put("sınıf", siniff);
            map.put("dogumtarih", dogumtarih);
            if (imageUrl.equals("null")) {
                map.put("resim", "null");
            } else {
                map.put("resim", imageUrl);
            }


            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ChangeFragment fragment = new ChangeFragment(getContext());
                        fragment.change(new kullaniciProfilFragment());
                        Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Bilgiler güncellenirken hata oluştu", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
