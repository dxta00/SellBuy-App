package com.example.sellbuy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sellbuy.databinding.ActivityAdminLoginBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {
    private ActivityAdminLoginBinding am;
    private FirebaseFirestore fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = DataBindingUtil.setContentView(this, R.layout.activity_admin_login);
        fdb = FirebaseFirestore.getInstance();

        am.ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = am.ulemail.getText().toString().trim();
                String password = am.ulpassword.getText().toString().trim();
                if (validateInputs(email, password)) {
                    signIn(email, password);
                }
            }
        });

        am.signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLogin.this, AdminSignup.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            showToast("Please enter your email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Please enter your password");
            return false;
        }

        return true;
    }

    private void signIn(final String email, final String password) {
        fdb.collection("Admin").whereEqualTo("Email", email).whereEqualTo("Password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Credentials match, login successful
                            Intent intent = new Intent(AdminLogin.this, SellHomePage.class);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            // No matching user found, login failed
                            showToast("Invalid email or password");
                        }
                    } else {
                        // Error occurred while querying Firestore
                        showToast("Login failed: " + task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
