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

public class CollectorLogin extends AppCompatActivity {

    private Button c_login;
    private EditText c_username,c_password;
    private TextView c_register;
    private ProgressBar c_progress_bar;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(CollectorLogin.this,CollectorHomePage.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collector_login);
        mAuth=FirebaseAuth.getInstance();
        c_username=findViewById(R.id.c_username);
        c_password=findViewById(R.id.c_password);
        c_register=findViewById(R.id.c_register);
        c_login=findViewById(R.id.c_login);
        c_progress_bar=findViewById(R.id.c_progress_bar);

        c_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CollectorLogin.this,CollectorRegister.class);
                startActivity(intent);
            }
        });

        c_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_progress_bar.setVisibility(view.VISIBLE);
                String email=c_username.getText().toString();
                String pwd=c_password.getText().toString();

                if(email.equals("")){
                    c_progress_bar.setVisibility(view.GONE);
                    Toast.makeText(CollectorLogin.this,"enter username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.equals("")){
                    c_progress_bar.setVisibility(view.GONE);
                    Toast.makeText(CollectorLogin.this,"enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                c_progress_bar.setVisibility(view.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(CollectorLogin.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(CollectorLogin.this,CollectorHomePage.class);
                                    startActivity(intent);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CollectorLogin.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });
    }
}
