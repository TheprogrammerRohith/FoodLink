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

public class CollectorRegister extends AppCompatActivity {

    private EditText c_r_name,c_r_phonenumber,c_r_address,c_r_username,c_r_password;
    private Button c_register;
    private ProgressBar c_r_progressbar;
    private TextView textview;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(CollectorRegister.this,CollectorHomePage.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collector_register);
        mAuth=FirebaseAuth.getInstance();
        textview=findViewById(R.id.c_r_textview);
        c_r_name=findViewById(R.id.c_r_name);
        c_r_phonenumber=findViewById(R.id.c_r_phonenumber);
        c_r_address=findViewById(R.id.c_r_address);
        c_r_username=findViewById(R.id.c_r_username);
        c_r_password=findViewById(R.id.c_r_password);
        c_register=findViewById(R.id.c_register);
        c_r_progressbar=findViewById(R.id.c_r_progress_bar);

        c_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_r_progressbar.setVisibility(view.VISIBLE);
                String email=c_r_username.getText().toString();
                String pwd=c_r_password.getText().toString();
                String name=c_r_name.getText().toString();
                String phonenumber=c_r_phonenumber.getText().toString();
                String address=c_r_address.getText().toString();
                if(email.equals("")){
                    c_r_progressbar.setVisibility(view.GONE);
                    Toast.makeText(CollectorRegister.this,"enter username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.equals("")){
                    c_r_progressbar.setVisibility(view.GONE);
                    Toast.makeText(CollectorRegister.this,"enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                c_r_progressbar.setVisibility(view.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(CollectorRegister.this, "Successfully registered", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(CollectorRegister.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }

                            private void saveUserDataToFirebase(String userId) {
                                FirebaseUser user=mAuth.getCurrentUser();
                                String Uid=user.getUid();
                                Users users=new Users(name,phonenumber,address,email,Uid);
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Collectors").child(userId);
                                userRef.setValue(users);
                            }
                        });
//                if(!name.isEmpty() && !phonenumber.isEmpty() && !address.isEmpty()){
//                    Users users=new Users(name,phonenumber,address,email);
//                    db=FirebaseDatabase.getInstance();
//                    reference=db.getReference().child("Collectors");
//                    reference.push().setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(CollectorRegister.this,"Successfully stored",Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                Toast.makeText(CollectorRegister.this, "Failed to store data", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//                }
//                mAuth.createUserWithEmailAndPassword(email, pwd)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                c_r_progressbar.setVisibility(view.GONE);
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(CollectorRegister.this, "Successfully registered", Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    // If sign in fails, display a message to the user.
//
//                                    Toast.makeText(CollectorRegister.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
            }
        });
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CollectorRegister.this, CollectorLogin.class);
                startActivity(intent);
            }
        });
    }
}
