package com.android.sheguard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.sheguard.PostAdapter;
import com.android.sheguard.R;
import com.android.sheguard.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Events extends AppCompatActivity {
    FloatingActionButton upload;
    RecyclerView recyclerView;
    ArrayList<Post> recycleList;
    PostAdapter recyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed);

        upload = findViewById(R.id.upload);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recycleList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        recyclerAdapter = new PostAdapter(recycleList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), eventsUpload.class);
                startActivity(i);
            }
        });

        loadPosts();
    }

    private void loadPosts() {
        firebaseDatabase.getReference().child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recycleList.clear(); // Clear existing data before adding new ones
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post model = dataSnapshot.getValue(Post.class);
                    recycleList.add(recycleList.size(),model);
                }
                // Reverse the list to display newly created posts first
                Collections.reverse(recycleList);
                recyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false); // Hide refreshing animation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false); // Hide refreshing animation
            }
        });
    }
}
