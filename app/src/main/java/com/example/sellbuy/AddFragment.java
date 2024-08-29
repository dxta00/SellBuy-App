package com.example.sellbuy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sellbuy.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private FirebaseFirestore fdb;
    private String name, description, price;
    private static final int REQUEST_CODE_SELECT_IMAGE = 101;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        fdb = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        binding.padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.name.getText().toString();
                description = binding.description.getText().toString();
                price = binding.price.getText().toString();

                if (selectedImageUri != null) {
                    String imageUrl = selectedImageUri.toString();

                    // Create a map to store product details
                    Map<String, Object> product = new HashMap<>();
                    product.put("Name", name);
                    product.put("Description", description);
                    product.put("Price", price);
                    product.put("ImageUrl", imageUrl);

                    // Add the product details to Firestore with a random document ID
                    fdb.collection("product_details").document()
                            .set(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Product added successfully", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("AddFragment", "Error adding product: " + e.getMessage());
                                    Toast.makeText(getActivity(), "Failed to add product", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                    binding.selectedImageView.setVisibility(View.VISIBLE);
                    binding.selectedImageView.setImageBitmap(scaledBitmap);
                } catch (IOException e) {
                    Log.e("AddFragment", "Error loading image: " + e.getMessage());
                    Toast.makeText(getActivity(), "Failed to load selected image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("AddFragment", "Selected image URI is null.");
                Toast.makeText(getActivity(), "Failed to retrieve selected image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
