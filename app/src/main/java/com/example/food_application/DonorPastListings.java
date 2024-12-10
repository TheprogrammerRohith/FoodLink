package com.example.food_application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonorPastListings extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference dRef;
    private RecyclerView recyclerView;
    MyAdapter4 myAdapter;
    ArrayList<DonorPL> list;
    String userId;
    private LinearLayout progress_bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_donor_past_listings, container, false);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if(user!=null){
            userId=user.getUid();
        }
        progress_bar=view.findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        recyclerView=view.findViewById(R.id.dpl_recycler_view2);
        dRef= FirebaseDatabase.getInstance().getReference().child("d_pastlistings").child(userId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        myAdapter=new MyAdapter4(getContext(),list);
        recyclerView.setAdapter(myAdapter);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    DonorPL obj=ds.getValue(DonorPL.class);
                    list.add(obj);
                }
                if (list.isEmpty()) {
                    progress_bar.setVisibility(View.GONE);
                    // Display a message when no items are available
                    LinearLayout.LayoutParams textview_layout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    TextView textView = new TextView(getActivity());
                    textView.setText("Currently Engaged items are null.");
                    textView.setLayoutParams(textview_layout);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(20);
                    textview_layout.setMargins(30,50,30,20);
                    recyclerView.setVisibility(View.GONE); // Hide the RecyclerView
                    // Add the TextView to your layout
                    // For example, if you have a LinearLayout:
                    FrameLayout frameLayout=view.findViewById(R.id.dpl);
                    frameLayout.addView(textView);
                } else {
                    progress_bar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}