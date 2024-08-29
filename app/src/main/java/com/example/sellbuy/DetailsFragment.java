package com.example.sellbuy;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellbuy.Product; // Import your Product class
import com.example.sellbuy.YourRecyclerViewAdapter; // Import your adapter class
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {

    private FirebaseFirestore fdb;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private YourRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        fdb = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productList = new ArrayList<>();
        adapter = new YourRecyclerViewAdapter(productList, getContext()); // Change YourRecyclerViewAdapter to your adapter class
        recyclerView.setAdapter(adapter);
        loadProducts();
        return view;
    }

    private void loadProducts() {
        fdb.collection("product_details")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("Name");
                            String description = document.getString("Description");
                            String price = document.getString("Price");
                            String imageUrl = document.getString("ImageUrl");
                            Product product = new Product(name, description, price, imageUrl);
                            productList.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Failed to fetch products", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
