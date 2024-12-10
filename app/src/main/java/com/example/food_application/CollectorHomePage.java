package com.example.food_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class CollectorHomePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Collector_Dashboard collectorDashboard;
    private Collector_ViewItems collectorViewItems;
    private CollectorPastListings collectorPastListings;
    private Collector_Profile collectorProfile;
    private TextView txtview;
    private LinearLayout progress_bar;
    private ImageView notifications;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String UId = user.getUid();
            DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("Collectors").child(UId);

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collector_homepage);
        bottomNavigationView=findViewById(R.id.btmnav);
        frameLayout=findViewById(R.id.main_frame);
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        txtview=findViewById(R.id.header_txt);
        collectorDashboard=new Collector_Dashboard();
        collectorViewItems=new Collector_ViewItems();
        collectorPastListings=new CollectorPastListings();
        collectorProfile=new Collector_Profile();
        notifications=findViewById(R.id.notications);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectorHomePage.this,ViewEvents.class);
                startActivity(intent);
            }
        });

        setFragment(collectorDashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId==R.id.c_dashboard){
                    setFragment(collectorDashboard);
                    return true;
                }
                else if (itemId==R.id.view_items) {
                    setFragment(collectorViewItems);
                    return true;
                } else if (itemId==R.id.past_list) {
                    setFragment(collectorPastListings);
                    return true;
                }else if (itemId==R.id.collector_profile) {
                    setFragment(collectorProfile);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
}
