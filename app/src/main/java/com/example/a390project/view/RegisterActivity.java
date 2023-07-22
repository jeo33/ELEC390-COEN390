package com.example.a390project.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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
    String username,password,Email,passwordConfirm,Phonenumber;
    EditText uid,pw,pwConfirm,email,Phone;
    Button Registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference().child("Users");
        Phone=findViewById(R.id.editTextphone);
        uid=findViewById(R.id.editTextName);
        pw=findViewById(R.id.editTextPassword);
        pwConfirm=findViewById(R.id.editTextConfirmPassword);
        email=findViewById(R.id.editTextEmail);
        Registration=(Button) findViewById(R.id.buttonRegister);
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phonenumber=Phone.getText().toString();
                username = uid.getText().toString();
                password = pw.getText().toString();
                Email = email.getText().toString();
                passwordConfirm = pwConfirm.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child(username).child("Email").setValue(Email);
                    databaseReference.child(username).child("password").setValue(password);
                    databaseReference.child(username).child("username").setValue(username);
                    databaseReference.child(username).child("phone").setValue(Phonenumber);
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }
}