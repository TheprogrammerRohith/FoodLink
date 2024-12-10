package com.example.food_application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

public class Collector_Dashboard extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference dRef;
    private RecyclerView recyclerView;
    MyAdapter3 myAdapter3;
    ArrayList<Engaged2> list;
    String UserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_collector_dashboard, container, false);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        if(user!=null){
            UserId=user.getUid();
        }
        recyclerView=view.findViewById(R.id.collector_recycler_view2);
        dRef= FirebaseDatabase.getInstance().getReference().child("CollectorEngaged").child(UserId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        myAdapter3=new MyAdapter3(getContext(),list);
        recyclerView.setAdapter(myAdapter3);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Engaged2 obj=ds.getValue(Engaged2.class);
                    list.add(obj);
                }
                if (list.isEmpty()) {
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
                    FrameLayout frameLayout=view.findViewById(R.id.cv_frame_layout2);
                    frameLayout.addView(textView);
                } else {
                    recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
                    myAdapter3.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}