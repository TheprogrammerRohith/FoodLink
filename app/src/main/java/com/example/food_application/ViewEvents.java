package com.example.food_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        TextView txtview=findViewById(R.id.textview);
        RecyclerView recyclerView=findViewById(R.id.event_recycler_view);
        DatabaseReference dRef= FirebaseDatabase.getInstance().getReference().child("Events");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Events> list=new ArrayList<>();
        MyAdapter6 myAdapter6=new MyAdapter6(this,list);
        recyclerView.setAdapter(myAdapter6);
        ImageView back=findViewById(R.id.back_to_home);
        LinearLayout progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEvents.this, CollectorHomePage.class);
                progress_bar.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Events event=ds.getValue(Events.class);
                    list.add(event);
                }
                if (list.isEmpty()) {
                    progress_bar.setVisibility(View.GONE);
                    txtview.setVisibility(View.VISIBLE);
                } else {
                    progress_bar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
                    myAdapter6.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error","something went wrong in fetching evetns");
            }
        });
    }
}