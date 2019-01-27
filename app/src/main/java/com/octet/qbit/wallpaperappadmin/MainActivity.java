package com.octet.qbit.wallpaperappadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        emailEditText =(EditText)findViewById(R.id.emailEditField);
        passwordEditText =(EditText)findViewById(R.id.passwordEditField);
        loginButton =(Button) findViewById(R.id.loginButton);

    }


    //delete entire block once the app is full functional
    @Override
    protected void onStart() {
        super.onStart();

        loginButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                auth.signInWithEmailAndPassword("p@test.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                });

                return true;
            }
        });
    }

    public void onClickLogin(View view) {

        email=emailEditText.getText().toString().trim();
        password=passwordEditText.getText().toString().trim();

        if(!email.isEmpty()&& !password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Logging you In", Toast.LENGTH_SHORT).show();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}
