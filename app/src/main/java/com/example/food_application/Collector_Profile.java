package com.example.food_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Collector_Profile extends Fragment {

    private EditText name, address, phoneNumber;
    private Button editButton;
    private boolean isEditable = false;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collector_profile, container, false);

        FirebaseAuth auth;
        FirebaseUser user;
        ArrayList<String> list=new ArrayList<>();
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        phoneNumber = view.findViewById(R.id.phone_number);
        editButton = view.findViewById(R.id.edit_button);
        TextView logout=view.findViewById(R.id.signout);

        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        if(user!=null){
            userId = user.getUid();
            Log.d("User id",userId);
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Collectors").child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String value = (String) dataSnapshot.getValue();
                            list.add(value);
                        }
                        address.setText(list.get(0));
                        name.setText(list.get(1));
                        phoneNumber.setText(list.get(2));

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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditableFields();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(),"user signed out",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void toggleEditableFields() {
        isEditable = !isEditable;
        name.setEnabled(isEditable);
        address.setEnabled(isEditable);
        phoneNumber.setEnabled(isEditable);

        if (isEditable) {
            editButton.setText("Save");
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUpdatedDeatils();
                }
            });
        } else {
            editButton.setText("Edit");
        }
    }
    private void saveUpdatedDeatils() {
        String updatedName = name.getText().toString();
        String updatedAddress = address.getText().toString();
        String updatedPhoneNumber = phoneNumber.getText().toString();

        if (userId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(userId);
            userRef.child("name").setValue(updatedName);
            userRef.child("address").setValue(updatedAddress);
            userRef.child("phonenumber").setValue(updatedPhoneNumber);

            Toast.makeText(getContext(),"Details Updated",Toast.LENGTH_SHORT).show();
        }

        // Disable fields again after saving
        toggleEditableFields();
    }
}