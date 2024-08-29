package com.example.sellbuy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.sellbuy.databinding.ActivitySellHomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class SellHomePage extends AppCompatActivity {
    private static final int MENU_ADD = R.id.add;
    private static final int MENU_ORDERS = R.id.orders;
    private static final int MENU_DETAILS = R.id.details;
    private static final int MENU_EDIT = R.id.edit;

    private ActivitySellHomePageBinding am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        am = ActivitySellHomePageBinding.inflate(getLayoutInflater());
        setContentView(am.getRoot());

        replaceFragment(new AddFragment());

        am.bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == MENU_ADD) {
                    replaceFragment(new AddFragment());
                    return true;
                } else if (item.getItemId() == MENU_ORDERS) {
                    replaceFragment(new OrdersFragment());
                    return true;
                } else if (item.getItemId() == MENU_DETAILS) {
                    replaceFragment(new DetailsFragment());
                    return true;
                } else if (item.getItemId() == MENU_EDIT) {
                    replaceFragment(new EditFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
