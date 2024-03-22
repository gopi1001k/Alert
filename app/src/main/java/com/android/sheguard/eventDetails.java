package com.android.sheguard;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class eventDetails extends AppCompatActivity {

    TextView singleDiscription;
    ImageView singleImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        singleDiscription=findViewById(R.id.singleDiscription);
        singleImage=findViewById(R.id.singleImage);
        Picasso.get().load(getIntent().getStringExtra("singleImage"))
                .placeholder(R.drawable.baseline_account_box_24)
                .into(singleImage);
        singleDiscription.setText(getIntent().getStringExtra("singleDiscription"));

    }
}
