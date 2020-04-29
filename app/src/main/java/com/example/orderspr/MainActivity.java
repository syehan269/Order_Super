package com.example.orderspr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener authStateListener;
    private BottomNavigationView bottomNavigationView;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private MaterialAlertDialogBuilder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        //menampilkan halaman fragment baru
        getFragmentpage(new home());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Super.ver");

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        subToTopic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            //          back_dialog();
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            alertDialogBuilder.setTitle("Exit")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //FirebaseAuth.getInstance().signOut();
                            //Intent intent = new Intent(MainActivity.this, login.class);
                            //startActivity(intent);
                            moveTaskToBack(true);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alertDialogBuilder.show();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.about:
                // about
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
                LayoutInflater inflater =LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.dialog_about, null);
                alertDialogBuilder.setView(view);

                alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialogBuilder.show();

                return true;
            case R.id.logout:
                // logout
                unsubTopic();
                FirebaseAuth.getInstance().signOut();
                Intent logout_int = new Intent(MainActivity.this, login.class);
                startActivity(logout_int);

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;

            //menampilkan fragment
            switch (item.getItemId()){

                case R.id.action_home:
                    fragment = new home();
                    getFragmentpage(fragment);
                    return true;

                case R.id.action_profile:
                    fragment = new profile();
                    getFragmentpage(fragment);
                    return true;

            }
            return false;

        }
    };

    private void getFragmentpage(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.page_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void deleteData() {
        try {

            FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(MainActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteACC() {

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog(){
        alertDialogBuilder.setTitle("Delete Account");
        alertDialogBuilder.setMessage("Are you sure ?").
                setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setTitle("Deleting");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        deleteACC();
                        deleteData();

                        FirebaseAuth.getInstance().signOut();
                        Intent logout = new Intent(MainActivity.this, login.class);
                        startActivity(logout);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.show();
    }

    private void subToTopic(){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference subRef = FirebaseDatabase.getInstance().getReference("user").child(Uid);

        subRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String setTopic = (String) dataSnapshot.child("depart").getValue();

                FirebaseMessaging.getInstance().subscribeToTopic("T_"+setTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                subRef.child("topic").setValue("T_"+setTopic);

                                //Toast.makeText(MainActivity.this, "Sub To "+ setTopic, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        FirebaseMessaging.getInstance().subscribeToTopic("T_Account")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(MainActivity.this, "Notification will show up", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
*/
    }

    private void unsubTopic(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference getRef = FirebaseDatabase.getInstance().getReference("user").child(uid);

        getRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String getTopic = (String) dataSnapshot.child("topic").getValue();

                FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(MainActivity.this, "Cleared "+getTopic, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR logout: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
