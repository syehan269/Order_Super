package com.example.orderspr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class handle_list extends AppCompatActivity {

    private Toolbar toolbar;
    private String departement, fbUser, fbKey;
    private DatabaseReference handleReference, userReference;
    private RecyclerView listView;
    private TextView empty_TV;
    private FirebaseAuth fbAuth;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private MaterialAlertDialogBuilder alertDialogBuilder;
    private Query queryListAll, queryListApp, queryListDis, queryList;
    private FirebaseRecyclerAdapter<handleAdapter, handle_list.ViewHolderHandleADM> firebaseRecyclerAdapterAll,
            firebaseRecyclerAdapterApp, firebaseRecyclerAdapterDis, firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_list);

        toolbar = findViewById(R.id.toolbar);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser().getUid();

        progressBar = findViewById(R.id.progress_handle);
        empty_TV = findViewById(R.id.Empty_handle);
        storageReference = FirebaseStorage.getInstance().getReference();

        handleReference = FirebaseDatabase.getInstance().getReference("Form");
        userReference = FirebaseDatabase.getInstance().getReference().child("user").child(fbUser).child("level");

        queryListApp = handleReference.orderByChild("approval").equalTo("Approved");
        queryListDis = handleReference.orderByChild("approval").equalTo("Disapproved");

        queryListAll = handleReference.orderByChild("status").equalTo("NO");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Handle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.RV_handle);
        listView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_handle_spr, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.avial:
                displayList();
                return false;

            case R.id.disapprove:
                displayListDis();
                return false;

            case R.id.approve:
                displayListApp();
                return false;

            case R.id.depart:
                displayDialog();

                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaListAdm() {

        queryList = handleReference.orderByChild("queryHandle").equalTo("Administration_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);

    }

    private void displayListFin() {
        queryList = handleReference.orderByChild("queryHandle").equalTo("Finance-Accounting_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);
    }

    private void displayMar() {
        queryList = handleReference.orderByChild("queryHandle").equalTo("Marketing_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);
    }

    private void displayListPri() {
        queryList = handleReference.orderByChild("queryHandle").equalTo("Primary_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);
    }

    private void displayListHR() {
        queryList = handleReference.orderByChild("queryHandle").equalTo("HR-GA_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);
    }

    private void displayListSec() {
        queryList = handleReference.orderByChild("queryHandle").equalTo("Secondary_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);
    }

    private void displayListIt() {
        queryList = handleReference.orderByChild("queryHandle").equalTo("IT_NO");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryList
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                //viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapter, true);
    }

    private void displayListApp() {
        firebaseRecyclerAdapterApp = new FirebaseRecyclerAdapter<handleAdapter, handle_list.ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryListApp
        ) {
            @Override
            protected void populateViewHolder(handle_list.ViewHolderHandleADM viewHolderHandleADM, final handleAdapter handleAdapter, int i) {

                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });

            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterApp, true);
    }

    private void displayListDis() {
        firebaseRecyclerAdapterDis = new FirebaseRecyclerAdapter<handleAdapter, handle_list.ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryListDis
        ) {
            @Override
            protected void populateViewHolder(handle_list.ViewHolderHandleADM viewHolderHandleADM, final handleAdapter handleAdapter, int i) {

                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });

            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterDis, true);
    }

    private void displayList() {

        firebaseRecyclerAdapterAll = new FirebaseRecyclerAdapter<handleAdapter, handle_list.ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderHandleADM.class,
                queryListAll
        ) {
            @Override
            protected void populateViewHolder(handle_list.ViewHolderHandleADM viewHolderHandleADM, final handleAdapter handleAdapter, int i) {

                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setName(handleAdapter.getNname());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });

            }
        };

        listView.setAdapter(firebaseRecyclerAdapterAll);
    }

    public static class ViewHolderHandleADM extends RecyclerView.ViewHolder{
        View lView;

        public ViewHolderHandleADM(View itemView){
            super(itemView);
            lView = itemView;
        }

        public void setDepartHandle(String Ccategory){
            TextView depart_tv = lView.findViewById(R.id.Tv_Departement_hit);
            depart_tv.setText(Ccategory);
        }

        public void setApprove(String Aaprove){
            TextView approve_TV = lView.findViewById(R.id.Approve_TV);
            approve_TV.setText(Aaprove);
        }

        public void setTItleHandle(String NName){
            TextView name_tv = lView.findViewById(R.id.Tv_Title_hit);
            name_tv.setText(NName);
        }

        private void setStatusList(){
            final ImageView handle_status = lView.findViewById(R.id.status_handle);
            final DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference().child("Form");

        }
        public  void setName(String name){
            TextView name_tv = lView.findViewById(R.id.Tv_Departement_hit);
            name_tv.setText(name);
        }

    }

    private void displayDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        final String data = "dialog_depart";

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View lView = layoutInflater.inflate(R.layout.dialog_depart_pro, null);
        final RecyclerView rv_depart = lView.findViewById(R.id.RV_depart_pro);

        rv_depart.setLayoutManager(new LinearLayoutManager(handle_list.this));

        dialogBuilder.setView(lView);
        Log.d(data, "setting layout success");

        DatabaseReference queryDepart = FirebaseDatabase.getInstance().getReference().child("Type");
        Log.d(data,"setting data path");

        FirebaseRecyclerAdapter<category, ViewHolderCategory> firebaseAdapter =
                new FirebaseRecyclerAdapter<category, ViewHolderCategory>(
                        category.class,
                        R.layout.list_depart_pro,
                        ViewHolderCategory.class,
                        queryDepart
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderCategory viewHolder, category model, int i) {
                        Log.d(data, "adapter created");
                        viewHolder.setCategory(model.getCategory());
                        Log.d(data, "set the data");

                        final String key = getRef(i).getKey();
                        final String value = model.getCategory();

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(handle_list.this, value, Toast.LENGTH_SHORT).show();
                                displayPRO(value);
                            }
                        });
                    }
                };

        rv_depart.setAdapter(firebaseAdapter);

        dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogBuilder.show();
    }

    private void displayPRO(String departName) {
        Query queryRequest = handleReference.orderByChild("category").equalTo(departName);

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderRequest>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list.ViewHolderRequest.class,
                queryRequest
        ) {
            @Override
            protected void populateViewHolder(ViewHolderRequest viewHolderRequest, handleAdapter handleAdapter, int i) {
                final String getKey = getRef(i).getKey();
                final String getTitle = handleAdapter.getNname();

                viewHolderRequest.setApproveADMREQ(handleAdapter.getApproval());
                viewHolderRequest.setDepartADMREQ(handleAdapter.getNname());
                viewHolderRequest.setTitleADMREQ(handleAdapter.getRRequest());

                viewHolderRequest.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(handle_list.this, read_request.class);
                        intent.putExtra("id_request", getKey);
                        startActivity(intent);
                    }
                });


            }
        };
        listView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ViewHolderCategory extends RecyclerView.ViewHolder{

        View view;

        public ViewHolderCategory(View itemView){
            super(itemView);
            view = itemView;
        }

        public void setCategory(String Category){
            TextView kategori = view.findViewById(R.id.CB_depart_pro);
            kategori.setText(Category);

        }
    }

    public static class  ViewHolderRequest extends RecyclerView.ViewHolder {
        View view;

        public ViewHolderRequest(View itemView){
            super(itemView);
            view = itemView;
        }

        public void setTitleADMREQ(String title){
            TextView listTitle = view.findViewById(R.id.Tv_Title_hit);
            listTitle.setText(title);
        }

        public void setDepartADMREQ(String Ccategory){
            TextView depart_tv = view.findViewById(R.id.Tv_Departement_hit);
            depart_tv.setText(Ccategory);
        }

        public void setApproveADMREQ(String Aaprove){
            TextView approve_TV = view.findViewById(R.id.Approve_TV);
            approve_TV.setText(Aaprove);
        }

        private void setStatusX(){
            final ImageView handle_status = view.findViewById(R.id.status_handle);

            handle_status.setImageResource(R.drawable.baseline_error_outline_black_48dp);
            handle_status.setColorFilter(Color.RED);

        }

        private void setStatusO(){
            final ImageView handle_status = view.findViewById(R.id.status_handle);

            handle_status.setImageResource(R.drawable.outline_check_circle_black_48dp);
            handle_status.setColorFilter(Color.GREEN);

        }

        public void setName(String name){
            TextView name_tv = view.findViewById(R.id.Tv_Departement_hit);
            name_tv.setText(name);
        }

    }

}
