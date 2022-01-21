package com.example.sohbetapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.lang.ref.Reference;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends Fragment {


    View view;
    String otherId,userId;
    TextView kullaniciadi,telefonno,bolumm,sinifno,dogummtarih,arkadasekletext;
    ImageView arkadaseklebuton,userProfileMesajImage;
    FirebaseDatabase firebaseDatabase;
    CircleImageView bilgifoto;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,reference_arkadaslik;
    String kontrol="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user_profile, container, false);
        tanimla();
        action();
        return view;
    }
    public void tanimla()
    {
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        reference_arkadaslik=firebaseDatabase.getReference().child("Arkadaslik_Istek");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();

        otherId=getArguments().getString("userid");
        bilgifoto=(CircleImageView)view.findViewById(R.id.bilgifoto);
        arkadasekletext=(TextView)view.findViewById(R.id.arkadasekletext);
        kullaniciadi=(TextView)view.findViewById(R.id.kullaniciadi);
        telefonno=(TextView)view.findViewById(R.id.telefonno);
        bolumm=(TextView)view.findViewById(R.id.bolumm);
        sinifno=(TextView)view.findViewById(R.id.sinifno);
        dogummtarih=(TextView)view.findViewById(R.id.dogummtarih);
        arkadaseklebuton=(ImageView)view.findViewById(R.id.arkadaseklebuton);
        userProfileMesajImage=(ImageView)view.findViewById(R.id.userProfileMesajImage);
        reference_arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userId))
                {
                    kontrol="istek";
                    arkadaseklebuton.setImageResource(R.drawable.arkadassil);
                    arkadasekletext.setText("İsteği Kaldır");

                }
                else {

                    arkadaseklebuton.setImageResource(R.drawable.arkadasekle);
                    arkadasekletext.setText("Arkadaş Ekle");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(otherId))
                {
                    kontrol="arkadas";
                    arkadaseklebuton.setImageResource(R.drawable.arkadaskaldir);
                    arkadasekletext.setText("Arkadaş Sil");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void action()
    {
        reference.child("Kullanıcılar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl= snapshot.getValue(Kullanicilar.class);
                kullaniciadi.setText("Kullanıcı Adı: "+kl.getIsim());
                telefonno.setText("Telefon Numarası: "+kl.getTelefon());
                bolumm.setText("Bölüm: "+kl.getBolum());
                sinifno.setText("Sınıf: "+kl.getSınıf());
                dogummtarih.setText("Doğum Tarihi: "+kl.getDogumtarih());
                if(!kl.getResim().equals("null")) {
                    Picasso.get().load(kl.getResim()).into(bilgifoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        arkadaseklebuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kontrol.equals("istek"))
                {
                    arkadasIptalEt(otherId,userId);

                }
                else if(kontrol.equals("arkadas"))
                {
                    arkadasTablosundanCikar(otherId,userId);
                }
                else
                {
                    arkadasEkle(otherId,userId);
                }


            }
        });

        userProfileMesajImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),ChatActivity.class);
                intent.putExtra("userName",kullaniciadi.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
            }
        });


    }

    private void arkadasTablosundanCikar(final String otherId, final String userId) {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        arkadaseklebuton.setImageResource(R.drawable.arkadasekle);
                        arkadasekletext.setText("Arkadaş Ekle");



                    }
                });

            }
        });
    }

    public void arkadasEkle(String otherId,String userId)
    {
        reference_arkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    reference_arkadaslik.child(otherId).child(userId).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                kontrol="istek";
                                Toast.makeText(getContext(),"Arkadaşlık isteği başarıyla yollandı",Toast.LENGTH_LONG).show();
                                arkadaseklebuton.setImageResource(R.drawable.arkadassil);
                                arkadasekletext.setText("İsteği Kaldır");
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Bir problem meydana geldi",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(),"Bir problem meydana geldi",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void arkadasIptalEt(String otherId,String userId)
    {
        reference_arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        arkadaseklebuton.setImageResource(R.drawable.arkadasekle);
                        arkadasekletext.setText("Arkadaş Ekle");

                        Toast.makeText(getContext(),"Arkadaşlık isteği iptal edildi",Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }
}