package com.android.sheguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sheguard.ui.activity.userEvents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userEventDetails extends AppCompatActivity {

    TextView singleDiscription;
    ImageView singleImage;

    Button del;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_event_details);

        del=findViewById(R.id.delete);
        singleDiscription=findViewById(R.id.singleDiscription);
        singleImage=findViewById(R.id.singleImage);

        Picasso.get().load(getIntent().getStringExtra("singleImage"))
                .placeholder(R.drawable.baseline_account_box_24)
                .into(singleImage);
        singleDiscription.setText(getIntent().getStringExtra("singleDiscription"));
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventDescription = singleDiscription.getText().toString();
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("events");

                reference.orderByChild("description").equalTo(eventDescription).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(userEventDetails.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(userEventDetails.this, userEvents.class);
                                    startActivity(i);
                                    finish(); // Optional: Close the current activity after starting userEvents
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(userEventDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break; // Assuming there's only one event with this description
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(userEventDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
