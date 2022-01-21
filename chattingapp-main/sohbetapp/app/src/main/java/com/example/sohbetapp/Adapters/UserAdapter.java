package com.example.sohbetapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetapp.ChangeFragment;
import com.example.sohbetapp.Models.Kullanicilar;
import com.example.sohbetapp.R;
import com.example.sohbetapp.UserProfile;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeyList, Activity activity, Context context) {
        this.userKeysList = userKeyList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    //layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlayout, parent, false);
        return new ViewHolder(view);
    }

    //viewlara setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        //holder.usernameTextView.setText(userKeysList.get(position).toString());
        reference.child("Kullanıcılar").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                Boolean userState = Boolean.parseBoolean(snapshot.child("state").getValue().toString());

                Picasso.get().load(kl.getResim()).into(holder.userimage);
                holder.usernameTextView.setText(kl.getIsim());

                if (userState == true) {
                    holder.user_state_img.setImageResource(R.drawable.online);

                } else {
                    holder.user_state_img.setImageResource(R.drawable.offline);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.userAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment fragment = new ChangeFragment(context);
                fragment.changeWithParameter(new UserProfile(), userKeysList.get(position));
            }
        });

    }

    //adapteri oluşturulacak olan listenin size'ı
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }

    //viewların tanımlanma işlemleri
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        CircleImageView userimage,user_state_img;
        LinearLayout userAnaLayout;

        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            userimage = (CircleImageView) itemView.findViewById(R.id.userimage);
            userAnaLayout = (LinearLayout) itemView.findViewById(R.id.userAnaLayout);
            user_state_img= (CircleImageView) itemView.findViewById(R.id.user_state_img);
        }

    }


}
