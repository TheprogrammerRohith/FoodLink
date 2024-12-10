package com.example.food_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonorRegister extends AppCompatActivity {

    private EditText d_r_name,d_r_phonenumber,d_r_address,d_r_username,d_r_password;
    private Button d_register;
    private ProgressBar d_r_progressbar;
    private TextView textview;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference reference;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(DonorRegister.this,DonorHomePage.class);
            startActivity(intent);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_register);
        mAuth=FirebaseAuth.getInstance();
        textview=findViewById(R.id.d_r_textview);
        d_r_name=findViewById(R.id.d_r_name);
        d_r_phonenumber=findViewById(R.id.d_r_phonenumber);
        d_r_address=findViewById(R.id.d_r_address);
        d_r_username=findViewById(R.id.d_r_username);
        d_r_password=findViewById(R.id.d_r_password);
        d_register=findViewById(R.id.d_register);
        d_r_progressbar=findViewById(R.id.d_r_progress_bar);

        d_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_r_progressbar.setVisibility(view.VISIBLE);
                String email=d_r_username.getText().toString();
                String pwd=d_r_password.getText().toString();
                String name=d_r_name.getText().toString();
                String phonenumber=d_r_phonenumber.getText().toString();
                String address=d_r_address.getText().toString();
                if(email.equals("")){
                    d_r_progressbar.setVisibility(view.GONE);
                    Toast.makeText(DonorRegister.this,"enter username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.equals("")){
                    d_r_progressbar.setVisibility(view.GONE);
                    Toast.makeText(DonorRegister.this,"enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                d_r_progressbar.setVisibility(view.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(DonorRegister.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        String userId = currentUser.getUid();
                                        // Save user data to Firebase using userId
                                        saveUserDataToFirebase(userId);
                                        // Proceed to next step (e.g., navigating to another activity)
                                    } else {
                                        // Handle case where currentUser is null
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(DonorRegister.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }

                            private void saveUserDataToFirebase(String userId) {
                                FirebaseUser user=mAuth.getCurrentUser();
                                String Uid=user.getUid();
                                Users users=new Users(name,phonenumber,address,email,Uid);
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(userId);
                                userRef.setValue(users);
                            }
                        });

                }
        });
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorRegister.this, DonorLogin.class);
                startActivity(intent);
            }
        });


    }
}
