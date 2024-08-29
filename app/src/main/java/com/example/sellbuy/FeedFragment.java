package com.example.sellbuy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FeedFragment extends Fragment {

    private FirebaseFirestore firestore;
    private TextView nameTextView, descriptionTextView, priceTextView;
    private ImageView imageView;
    private Button deleteButton;
    private String productId, name, description, price, imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        nameTextView = view.findViewById(R.id.nameTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        priceTextView = view.findViewById(R.id.priceTextView);
        imageView = view.findViewById(R.id.imageView);


        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getString("productId");
            name = bundle.getString("productName");
            description = bundle.getString("productDescription");
            price = bundle.getString("productPrice");
            imageUrl = bundle.getString("productImageUrl");

            nameTextView.setText(name);
            descriptionTextView.setText(description);
            priceTextView.setText("Rs. " + price);

            Glide.with(requireContext()).load(imageUrl).into(imageView);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(productId);
            }
        });
    }

    private void deleteProduct(String productId) {
        firestore.collection("product_details").document(productId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate back to the previous screen or refresh the product list
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DetailsFragment", "Failed to delete product", e);
                        Toast.makeText(requireContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
