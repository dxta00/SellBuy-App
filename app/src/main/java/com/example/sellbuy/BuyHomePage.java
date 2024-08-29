package com.example.sellbuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyHomePage extends AppCompatActivity {

    private static final String TAG = "BuyHomePage";

    private FirebaseFirestore fdb;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private YourRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_home_page);

        fdb = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productList = new ArrayList<>();
            adapter = new YourRecyclerViewAdapter(productList, this);
            recyclerView.setAdapter(adapter);

            loadProducts();
        }
    }

    private void loadProducts() {
        fdb.collection("product_details")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("Name");
                        String description = document.getString("Description");
                        String price = document.getString("Price");
                        String imageUrl = document.getString("ImageUrl");
                        Product product = new Product(name, description, price, imageUrl);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch products", e);
                    Toast.makeText(this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                });



    }

    public void goToOtherPage() {
        Intent intent = new Intent(this, Buy.class); // Replace OtherActivity.class with the activity you want to navigate to
        startActivity(intent);
    }
}