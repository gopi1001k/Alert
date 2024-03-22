package com.android.sheguard.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sheguard.R;
import com.android.sheguard.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class eventsUpload extends AppCompatActivity {

    TextView mDescription;
    ImageView mImageView;
    Button mSubmit;
    String ImageURL;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadfeed);

        mDescription=findViewById(R.id.description);
        mImageView=findViewById(R.id.uploadbtn);
        mSubmit=findViewById(R.id.submit);

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data=result.getData();
                    uri=data.getData();
                    mImageView.setImageURI(uri);
                }
                else{
                    Toast.makeText(eventsUpload.this,"No Image Selected",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent photoPicker=new Intent(Intent.ACTION_PICK);
                 photoPicker.setType("image/*");
                 activityResultLauncher.launch(photoPicker);
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    public void saveData(){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("eventImages").child(uri.getLastPathSegment());

        AlertDialog.Builder builder=new AlertDialog.Builder(eventsUpload.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(eventsUpload.this,"!working",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage=uriTask.getResult();
                ImageURL=urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(eventsUpload.this, "Failed to save Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void uploadData(){
        String desc=mDescription.getText().toString();
        Post post=new Post(ImageURL,desc);
        //Toast.makeText(eventsUpload.this,"!working",Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("events").child(desc).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(eventsUpload.this,"Saved",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(eventsUpload.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
