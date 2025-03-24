package com.kailawn.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kailawn.recipe.Adapter.Post_item_adapter;
import com.kailawn.recipe.Model.Post_item_model;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {
    TextView chawhmeh_hming;
    TextView desc;
    TextView siam_dan1;
    TextView siam_dan2;
    ImageView imageView;
    Post_item_adapter postItemAdapter;
    List<Post_item_model> post_item_models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);

        chawhmeh_hming = findViewById(R.id.home_detail_title_id);
        desc = findViewById(R.id.detail_desc_id);
        siam_dan1 = findViewById(R.id.siam_dan1_id);
        siam_dan2 = findViewById(R.id.siam_dan2_id);
        imageView= findViewById(R.id.detail_image);
        post_item_models = new ArrayList<>();

        // intent hmanga data a ken phei show na
        Intent intent = getIntent();
        String chawhmeh_hmingText = intent.getStringExtra("chawhmeh_hming");
        String descText = intent.getStringExtra("desc");
        String siam_danText1 = intent.getStringExtra("siam_dan1");
        String siam_danText2 = intent.getStringExtra("siam_dan2");
        String image_url = intent.getStringExtra("image_url");

        // Set the data to TextViews
        chawhmeh_hming.setText(chawhmeh_hmingText);
        desc.setText(descText);
        siam_dan1.setText(siam_danText1);
        siam_dan2.setText(siam_danText2);
        Glide.with(this)
                .load(image_url)
                .into(imageView);
    }
}
