package com.example.sellbuy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sellbuy.databinding.ActivityUserSignupBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminSignup extends AppCompatActivity {
    ActivityUserSignupBinding am;
    FirebaseFirestore fdb;
    String Password, Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = DataBindingUtil.setContentView(this, R.layout.activity_user_signup);
        fdb = FirebaseFirestore.getInstance();

        am.usbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    // Validations passed, proceed with registration
                    Email = am.uremail.getText().toString();
                    Password = am.urpassword.getText().toString();

                    // Store user data in Firestore
                    Map<String, Object> admin = new HashMap<>();
                    admin.put("Email", Email);
                    admin.put("Password", Password);
                    fdb.collection("Admin").add(admin);

                    Toast.makeText(AdminSignup.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // Navigate to the homepage or another activity as needed
                    Intent in = new Intent(AdminSignup.this, SellHomePage.class);
                    startActivity(in);
                }
            }
        });

        am.loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSignup.this, AdminLogin.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        String email = am.uremail.getText().toString().trim();
        String password = am.urpassword.getText().toString().trim();
        String confirmPassword = am.urcpassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showToast("Please enter your email");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Please enter your password");
            return false;
        }

        if (password.length() < 6) {
            showToast("Password must be at least 6 characters long");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
