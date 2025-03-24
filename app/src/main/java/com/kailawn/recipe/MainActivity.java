package com.kailawn.recipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kailawn.recipe.Adapter.Post_item_adapter;
import com.kailawn.recipe.Model.Post_item_model;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView hming;
    RecyclerView recyclerView;
    CircleImageView profileImg;
    List<Post_item_model> postModel;
    Post_item_adapter postItemAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth Auth;

    public MainActivity() {
        // Default constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recView_id);
        profileImg = findViewById(R.id.profile_id);
        hming = findViewById(R.id.hming_id);

        firestore = FirebaseFirestore.getInstance();
        Auth = FirebaseAuth.getInstance();
        postModel = new ArrayList<>();

        // Initialize SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filter the list when the user submits the query
                postItemAdapter.filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list as the user types
                postItemAdapter.filterList(newText);
                return false;
            }
        });

        //  profile image handler
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfilePage.class));
            }
        });

        // User login leh login loh checkna
        if (Auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        } else {
            // Display the user's name
            displayUserName();
        }

        // Set up RecyclerView layout manager
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        // Fetch data from Firestore
        getUserData();
    }

    private void displayUserName() {
        String userId = Auth.getCurrentUser().getUid();
        firestore.collection("user_profile").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("Name"); // Field name in Firestore
                            hming.setText(userName != null ? userName : "User");
                        } else {
                            hming.setText("User");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hming.setText("User");
                    }
                });
    }

    private void getUserData() {
        firestore.collection("chawhmeh").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                                Post_item_model post_item_model = d.toObject(Post_item_model.class);
                                postModel.add(post_item_model);
                            }
                            // Initialize the adapter with the full list
                            postItemAdapter = new Post_item_adapter(MainActivity.this, postModel);
                            recyclerView.setAdapter(postItemAdapter);
                        }
                    }
                });
    }
}