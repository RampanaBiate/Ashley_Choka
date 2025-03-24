package com.kailawn.recipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    EditText name, email, phone;
    Button save_btn;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ImageView arrow, profileImage;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.pname_id);
        email = findViewById(R.id.pemail_id);
        phone = findViewById(R.id.Enter_phone);
        save_btn = findViewById(R.id.save_btn);
        arrow = findViewById(R.id.arrowback_id);
        profileImage = findViewById(R.id.profile_image);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        email.setEnabled(false); // Disable email editing

        getUserData(); // Corrected method name

        profileImage.setOnClickListener(v -> openFileChooser());

        save_btn.setOnClickListener(v -> {
            uploadImage();
            if (name.getText().toString().isEmpty()) {
                name.setError("I hming enter rawh");
            } else if (phone.getText().toString().isEmpty()) {
                phone.setError("I phone number enter rawh");
            } else {
                saveUserProfile();
            }
        });
    }

    private void getUserData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Nghak lawk rawh");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        firestore.collection("user_profile").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(documentSnapshot -> {
                    progressDialog.hide();
                    if (documentSnapshot.exists()) {
                        name.setText(Objects.requireNonNull(documentSnapshot.get("Name")).toString());
                        email.setText(Objects.requireNonNull(documentSnapshot.get("email")).toString());
                        phone.setText(Objects.requireNonNull(documentSnapshot.get("Phone_no")).toString());
                    }
                })
                .addOnFailureListener(e -> progressDialog.hide());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri); // Show selected image
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference fileReference = storageReference.child("profile_images/" + auth.getCurrentUser().getUid() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveImageUrlToFirestore(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrlToFirestore(String imageUrl) {
        HashMap<String, Object> userProfile = new HashMap<>();
        userProfile.put("profileImageUrl", imageUrl);
        progressDialog.hide();

        firestore.collection("user_profile").document(auth.getCurrentUser().getUid())
                .update(userProfile)
                .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show());
    }

    private void saveUserProfile() {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Nghak lawk rawh");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Name", name.getText().toString());
        hashMap.put("Phone_no", phone.getText().toString());

        firestore.collection("user_profile").document(auth.getCurrentUser().getUid())
                .update(hashMap).addOnSuccessListener(unused -> {
                    progressDialog.hide();
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    finishAffinity();
                })
                .addOnFailureListener(e -> {
                    progressDialog.hide();
                    Toast.makeText(ProfileActivity.this, "Ti nawn leh rawh", Toast.LENGTH_SHORT).show();
                });
    }
}
