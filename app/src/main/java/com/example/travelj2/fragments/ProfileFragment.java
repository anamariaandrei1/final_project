package com.example.travelj2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.example.travelj2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private CircleImageView avatar;
    private TextView username;
    private TextView email;
    private TextView phone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("Users");

        avatar = root.findViewById(R.id.profile_image);
        username = root.findViewById(R.id.usernameTextView);
        email = root.findViewById(R.id.emailTextView);
        phone = root.findViewById(R.id.phoneTextView);

        Query query = databaseRef.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              for (DataSnapshot ds : snapshot.getChildren()) {
                  String name = "" + ds.child("name").getValue();
                  String emailAddress = "" + ds.child("email").getValue();
                  String phoneNumber = ""+ ds.child("phone").getValue();
                  String imagePath = ""+ ds.child("image").getValue();
                  username.setText(name);
                  email.setText(emailAddress);
                  phone.setText(phoneNumber);
                  if (!imagePath.equals("")) {
                      Picasso.get().load(imagePath).into(avatar);
                  }
                  else {
                      Picasso.get().load(R.drawable.ic_add_photo).into(avatar);
                  }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}
