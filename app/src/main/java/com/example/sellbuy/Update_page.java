package com.example.sellbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Update_page extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private ImageView imageView;
    private FirebaseFirestore firestore;
    private String productId;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        nameEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        imageView = findViewById(R.id.updatedImageView);

        // Get product details from intent extras
        productId = getIntent().getStringExtra("productId");
        String productName = getIntent().getStringExtra("productName");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productPrice = getIntent().getStringExtra("productPrice");
        imageUrl = getIntent().getStringExtra("imageUrl");

        // Set product details to UI elements
        nameEditText.setText(productName);
        descriptionEditText.setText(productDescription);
        priceEditText.setText(productPrice);

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(imageView);

        // Update button click listener
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated values
                String updatedName = nameEditText.getText().toString().trim();
                String updatedDescription = descriptionEditText.getText().toString().trim();
                String updatedPrice = priceEditText.getText().toString().trim();

                // Update details in Firestore
                updateProductInFirestore(updatedName, updatedDescription, updatedPrice);
            }
        });
    }

    private void updateProductInFirestore(String updatedName, String updatedDescription, String updatedPrice) {
        // Create a map with updated data
        Map<String, Object> updatedProduct = new HashMap<>();
        updatedProduct.put("Name", updatedName);
        updatedProduct.put("Description", updatedDescription);
        updatedProduct.put("Price", updatedPrice);
        updatedProduct.put("ImageUrl", imageUrl); // Keep the existing image URL

        // Update the document in Firestore
        firestore.collection("product_details").document(productId)
                .update(updatedProduct)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update the details page with new values
                        Intent intent = new Intent();
                        intent.putExtra("productId", productId);
                        intent.putExtra("productName", updatedName);
                        intent.putExtra("productDescription", updatedDescription);
                        intent.putExtra("productPrice", updatedPrice);
                        intent.putExtra("imageUrl", imageUrl); // Send back the image URL
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Update_page.this, "Failed to update details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
