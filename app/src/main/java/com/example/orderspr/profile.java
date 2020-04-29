package com.example.orderspr;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ImageView IV_profile;
    private Button btn_edit_profile;
    private TextView tv_Username_profile, tv_depart_profile, tv_email_profile;
    private DatabaseReference databaseReference;
    private String fbUser, getUsername_profile, getEmail_profile, getDepart_profile;

    // TODO: Rename and change types of parameters

    public profile() {

        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btn_edit_profile = view.findViewById(R.id.Btn_edit_frg);
        tv_depart_profile = view.findViewById(R.id.Tv_depart_PROFILE);
        tv_email_profile = view.findViewById(R.id.Tv_Email_PROFILE);
        tv_Username_profile = view.findViewById(R.id.Tv_Name_PROFILE);

        fbUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), edit_profile.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onStart() {

        databaseReference.child(fbUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getUsername_profile = (String) dataSnapshot.child("userName").getValue();
                getEmail_profile = (String) dataSnapshot.child("email").getValue();
                getDepart_profile = (String) dataSnapshot.child("depart").getValue();

                tv_email_profile.setText(getEmail_profile);
                tv_depart_profile.setText(getDepart_profile);
                tv_Username_profile.setText(getUsername_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        super.onStart();
    }
}
