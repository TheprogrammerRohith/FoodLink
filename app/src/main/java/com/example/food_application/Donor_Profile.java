package com.example.food_application;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class Donor_Profile extends Fragment {

    private EditText name, address, phoneNumber;
    private TextView logout,postevent;
    private Button editButton;
    private boolean isEditable = false;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donor_profile, container, false);
        FirebaseAuth auth;
        FirebaseUser user;
        ArrayList<String> list=new ArrayList<>();
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        phoneNumber = view.findViewById(R.id.phone_number);
        editButton = view.findViewById(R.id.edit_button);
        logout=view.findViewById(R.id.signout);
        postevent=view.findViewById(R.id.postevent);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

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

        postevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.post_event, null);
                builder.setView(dialogView);

                EditText event_name=dialogView.findViewById(R.id.event_name);
                EditText event_loc=dialogView.findViewById(R.id.event_location);
                TextView event_date=dialogView.findViewById(R.id.event_date);
                TextView event_time=dialogView.findViewById(R.id.event_time);
                EditText phone_no=dialogView.findViewById(R.id.event_contact);
                Button close=dialogView.findViewById(R.id.close_btn);
                Button post=dialogView.findViewById(R.id.post_btn);

                event_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        // Create DatePickerDialog and set listener
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                getContext(),
                                (view, selectedYear, selectedMonth, selectedDay) -> {
                                    // Update the TextView with the selected date
                                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                    event_date.setText(date);
                                },
                                year, month, day);

                        datePickerDialog.show();
                    }
                });

                event_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        // Initialize TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Format the time and set it in the EditText
                                        String time = String.format("%02d:%02d", hourOfDay, minute);
                                        event_time.setText(time);
                                    }
                                }, hour, minute, true);
                        timePickerDialog.show();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();

                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=event_name.getText().toString();
                        String location=event_loc.getText().toString();
                        String date=event_date.getText().toString();
                        String time=event_time.getText().toString();
                        String contact_no=phone_no.getText().toString();
                        if (name.isEmpty()) {
                            event_name.setError("Event name is required");
                            event_name.requestFocus();
                            return;
                        }
                        if (location.isEmpty()) {
                            event_loc.setError("Location is required");
                            event_loc.requestFocus();
                            return;
                        }
                        if (date.isEmpty()) {
                            event_date.setError("Date is required");
                            event_date.requestFocus();
                            return;
                        }
                        if (time.isEmpty()) {
                            event_time.setError("Time is required");
                            event_time.requestFocus();
                            return;
                        }
                        if (contact_no.isEmpty()) {
                            phone_no.setError("Contact number is required");
                            phone_no.requestFocus();
                            return;
                        }
                        Events event=new Events(name,location,date,time,contact_no,userId);
                        DatabaseReference dRef=FirebaseDatabase.getInstance().getReference().child("Events").child(userId);
                        dRef.setValue(event);
                        Toast.makeText(getContext(), "Event details stored", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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