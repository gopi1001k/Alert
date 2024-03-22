package com.android.sheguard.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sheguard.EventsAdapter;
import com.android.sheguard.R;
import com.android.sheguard.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userEvents extends AppCompatActivity {
    RecyclerView recyclerView1;
    ArrayList<Post> recyleList1;


    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_events);
        user= FirebaseAuth.getInstance().getCurrentUser();
       // Toast.makeText(this, user.getEmail()+"", Toast.LENGTH_SHORT).show();

        recyclerView1=findViewById(R.id.eventDetails);
        recyleList1=new ArrayList<>();
        firebaseDatabase= FirebaseDatabase.getInstance();

        //delete=findViewById(R.id.delete);

        EventsAdapter recyclerAdapter = new EventsAdapter(recyleList1,getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.addItemDecoration(new DividerItemDecoration(recyclerView1.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView1.setAdapter(recyclerAdapter);
        //String userid=user.getUid().toString();

        String getmil=user.getUid().toString();

        firebaseDatabase.getReference().child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post model= dataSnapshot.getValue(Post.class);
                    recyleList1.add(0,model);
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
