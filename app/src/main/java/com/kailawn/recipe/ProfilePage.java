package com.kailawn.recipe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilePage extends AppCompatActivity {
    TextView name;
    TextView email;
    TextView phone;
    ImageView profileImage;
    TextView id;
    Button edit_btn;
    ImageView arrow;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    StorageReference storageReference;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        name= findViewById(R.id.profilename_id);
        email= findViewById(R.id.profileemail_id);
        phone= findViewById(R.id.profilephone_id);
        id= findViewById(R.id.profile_uid);
        arrow= findViewById(R.id.arrow);
        edit_btn= findViewById(R.id.edit_btn);
        profileImage=findViewById(R.id.profile_image_id);
        firebaseFirestore= FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth= FirebaseAuth.getInstance();



        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, MainActivity.class));
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, ProfileActivity.class));
            }
        });

        getUserData();



    }

    private void getUserData() {

        progressDialog= new ProgressDialog(ProfilePage.this);
        progressDialog.setMessage("Nghak lawk rawh");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseFirestore.collection("user_profile").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressDialog.hide();
                        if (documentSnapshot.exists()){
                            name.setText(documentSnapshot.get("Name").toString());
                            email.setText(documentSnapshot.get("email").toString());
                            phone.setText(documentSnapshot.get("Phone_no").toString());
                            id.setText(documentSnapshot.get("uid").toString());

                            // Get profile image URL
                            if (documentSnapshot.contains("profileImageUrl")) {
                                String imageUrl = documentSnapshot.getString("profileImageUrl");

                                // Use Glide to load the image into the ImageView
                                Glide.with(ProfilePage.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.user) // Optional placeholder image
                                        .into(profileImage);
                            }
                        } else {
                            Toast.makeText(ProfilePage.this, "Unable to load data", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                    }
                });
    }
}