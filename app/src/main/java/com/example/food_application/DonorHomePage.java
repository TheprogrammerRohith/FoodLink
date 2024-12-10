package com.example.food_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonorHomePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Donor_Dashboard donorDashboard;
    private Donor_AddPage donorAddPage;
    private DonorPastListings donorPastListings;
    private Donor_Profile donor_profile;
    private TextView txtview;
    private LinearLayout progress_bar;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String UId = user.getUid();
            DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(UId);

            dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        if (txtview != null && userName != null) {
                            progress_bar.setVisibility(View.GONE);
                            txtview.setText("Hi "+userName + " 👋👋👋");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error if needed
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_homepage);

        txtview = findViewById(R.id.header_txt);  // Initialize txtview here
        bottomNavigationView = findViewById(R.id.btmnav);
        frameLayout = findViewById(R.id.main_frame);
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        // Initialize fragments
        donorDashboard = new Donor_Dashboard();
        donorAddPage = new Donor_AddPage();
        donorPastListings = new DonorPastListings();
        donor_profile = new Donor_Profile();

        // Set the initial fragment
        setFragment(donorDashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.dashboard) {
                setFragment(donorDashboard);
                return true;
            } else if (itemId == R.id.add_items) {
                setFragment(donorAddPage);
                return true;
            } else if (itemId == R.id.past_list) {
                setFragment(donorPastListings);
                return true;
            } else if (itemId == R.id.donor_profile) {
                setFragment(donor_profile);
                return true;
            } else {
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
