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

import org.w3c.dom.Text;

public class DonorLogin extends AppCompatActivity {

    private Button d_login;
    private EditText d_username,d_password;
    private TextView d_register;
    private ProgressBar d_progress_bar;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(DonorLogin.this,DonorHomePage.class);
            startActivity(intent);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_login);
        mAuth=FirebaseAuth.getInstance();
        d_username=findViewById(R.id.d_username);
        d_password=findViewById(R.id.d_password);
        d_register=findViewById(R.id.d_register);
        d_login=findViewById(R.id.d_login);
        d_progress_bar=findViewById(R.id.d_progress_bar);

        d_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorLogin.this,DonorRegister.class);
                startActivity(intent);
            }
        });

        d_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_progress_bar.setVisibility(view.VISIBLE);
                String email=d_username.getText().toString();
                String pwd=d_password.getText().toString();

                if(email.equals("")){
                    d_progress_bar.setVisibility(view.GONE);
                    Toast.makeText(DonorLogin.this,"enter username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.equals("")){
                    d_progress_bar.setVisibility(view.GONE);
                    Toast.makeText(DonorLogin.this,"enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                d_progress_bar.setVisibility(view.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(DonorLogin.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(DonorLogin.this,DonorHomePage.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(DonorLogin.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });
    }
}
