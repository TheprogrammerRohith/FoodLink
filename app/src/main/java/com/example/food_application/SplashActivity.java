package com.example.food_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference donorRef, collectorRef;
    private TextView donor, collector;
    private boolean roleFound = false; // Flag to prevent multiple role checks
    private LinearLayout progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        donor = findViewById(R.id.donor);
        collector = findViewById(R.id.collector);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        new Handler().postDelayed(() -> {
            if (user != null) {
                String uId = user.getUid();
                donorRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(uId);
                collectorRef = FirebaseDatabase.getInstance().getReference().child("Collectors").child(uId);

                // Check if user is a Donor
                donorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && !roleFound) {
                            progress_bar.setVisibility(View.GONE);
                            roleFound = true;  // Set flag to prevent other checks
                            Intent intent = new Intent(SplashActivity.this, DonorHomePage.class);
                            startActivity(intent);
                            finish();  // Finish SplashActivity so it won’t stay in the back stack
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });

                // Check if user is a Collector
                collectorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && !roleFound) {
                            progress_bar.setVisibility(View.GONE);
                            roleFound = true;  // Set flag to prevent other checks
                            String userName=snapshot.child("name").getValue(String.class);
                            Intent intent = new Intent(SplashActivity.this, CollectorHomePage.class);
                            intent.putExtra("userName", userName);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });
            } else {
                progress_bar.setVisibility(View.GONE);
                // New user: show role options
                donor.setVisibility(View.VISIBLE);
                collector.setVisibility(View.VISIBLE);
                donor.setOnClickListener(v -> {
                    Intent intent = new Intent(SplashActivity.this, DonorLogin.class);
                    startActivity(intent);
                });
                collector.setOnClickListener(v -> {
                    Intent intent = new Intent(SplashActivity.this, CollectorLogin.class);
                    startActivity(intent);
                });
            }
        }, 2000);
    }
}
