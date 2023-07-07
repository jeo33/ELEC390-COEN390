package com.example.a390project.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    String username,password,Email,passwordConfirm;
    EditText uid,pw,pwConfirm,email;
    Button Registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference().child("Users");

        uid=findViewById(R.id.editTextName);
        pw=findViewById(R.id.editTextPassword);
        pwConfirm=findViewById(R.id.editTextConfirmPassword);
        email=findViewById(R.id.editTextEmail);
        Registration=(Button) findViewById(R.id.buttonRegister);
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=uid.getText().toString();
                password=pw.getText().toString();
                Email=email.getText().toString();
                passwordConfirm=pwConfirm.getText().toString();
                databaseReference.child(Email).child("Username").setValue(username);
                databaseReference.child(Email).child("password").setValue(password);
                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}