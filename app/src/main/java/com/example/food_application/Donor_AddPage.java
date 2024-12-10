package com.example.food_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Donor_AddPage extends Fragment {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button submit,post;
    private Spinner foodItemSpinner, quantitySpinner;
    LinearLayout linear_layout;
    String userId;
    ArrayList<String> list=new ArrayList<>();
    final ArrayList<String> foodnames = new ArrayList<>();
    final ArrayList<String> foodquantities = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_donor_add_page, container, false);

        submit=myView.findViewById(R.id.submit_btn);
        post=myView.findViewById(R.id.post_btn);
        foodItemSpinner = myView.findViewById(R.id.food_item_spinner);
        quantitySpinner = myView.findViewById(R.id.quantity_spinner);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        linear_layout=myView.findViewById(R.id.items_layout);

        List<String> foodItems = Arrays.asList(
                "Select Item",
                "Rice",
                "Chapati/Rotis",
                "Dal",
                "Vegetable Curry",
                "Paneer Curry",
                "Mixed Vegetables",
                "Pulao/Biryani",
                "Idli",
                "Dosa",
                "Upma",
                "Curd",
                "Milk",
                "Fruits (Bananas, Apples, Oranges)",
                "Biscuits",
                "Cakes"
        );
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, foodItems);
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodItemSpinner.setAdapter(foodAdapter);
        foodItemSpinner.setSelection(0);

        List<String> quantities = Arrays.asList("Select Quantity","10","20","50","100","150","200");
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, quantities);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);
        quantitySpinner.setSelection(0);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFood = foodItemSpinner.getSelectedItem().toString();
                String selectedQuantity = quantitySpinner.getSelectedItem().toString();

                if (selectedFood.equals("Select Item") || selectedQuantity.equals("Select Quantity")) {
                    Toast.makeText(getContext(), "Please select a valid food item and quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                foodnames.add(selectedFood);
                foodquantities.add(selectedQuantity);

                // Create new LinearLayout for this food item
                LinearLayout newLinearLayout = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 30);
                newLinearLayout.setLayoutParams(layoutParams);
                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Create TextView for food name
                TextView foodNameTextView = new TextView(getContext());
                LinearLayout.LayoutParams foodNameLayoutParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                foodNameTextView.setLayoutParams(foodNameLayoutParams);
                foodNameTextView.setText(selectedFood);
                foodNameTextView.setTextSize(25);
                foodNameTextView.setPadding(16, 0, 16, 0);

                // Create TextView for quantity
                TextView quantityTextView = new TextView(getContext());
                LinearLayout.LayoutParams quantityLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                quantityTextView.setLayoutParams(quantityLayoutParams);
                quantityTextView.setText(selectedQuantity);
                quantityTextView.setTextSize(25);
                quantityTextView.setPadding(16, 0, 16, 0);

                // Create remove button with OnClickListener
                TextView removeText = new TextView(getContext());
                LinearLayout.LayoutParams removeTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                removeTextParams.gravity = Gravity.END;
                removeTextParams.setMarginEnd(16);
                removeText.setLayoutParams(removeTextParams);
                removeText.setText("X");
                removeText.setTextSize(25);
                removeText.setPadding(16, 0, 16, 0);

                // Add remove listener to remove the item from the list and layout
                removeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = linear_layout.indexOfChild(newLinearLayout);
                        if (index != -1) {
                            foodnames.remove(index);
                            foodquantities.remove(index);

                            linear_layout.removeView(newLinearLayout);
                            Toast.makeText(getContext(), "Item removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Add views to the LinearLayout
                newLinearLayout.addView(foodNameTextView);
                newLinearLayout.addView(quantityTextView);
                newLinearLayout.addView(removeText);
                linear_layout.addView(newLinearLayout);
            }
        });


        if(user!=null){
            userId = user.getUid();
            Log.d("User id",userId);
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String value = (String) dataSnapshot.getValue();
                            list.add(value);
                        }
                    }
                    else{
                        Log.d("Firebase","something went wrong");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error fetching user details: " + error.getMessage());
                }
            });

        }


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("FoodDetails");
                    String id=userRef.push().getKey();
                    FoodDetails fd=new FoodDetails(foodnames,foodquantities,list.get(1), list.get(2),list.get(0),userId,id);
                    userRef.child(id).setValue(fd);
                    Toast.makeText(getContext(),"successfully posted",Toast.LENGTH_SHORT).show();
                    foodnames.clear();
                    foodquantities.clear();
            }
        });

        return myView;
    }
}