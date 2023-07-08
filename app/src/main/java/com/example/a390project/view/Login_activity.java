package com.example.a390project.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_activity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    String username;
    String password;
    TextView welcome,Login_text,toRegister;
    EditText uid,pw;
    Button login,Signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        uid=findViewById(R.id.Username_input);
        pw=findViewById(R.id.Password_input);
        welcome=findViewById(R.id.Welcome_Text);
        Login_text=findViewById(R.id.Log_in_Text);
        login=(Button) findViewById(R.id.Log_in_button);
        Signup=(Button) findViewById(R.id.RegisterHint);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_activity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference().child("Users");

        // below line is used to get the instance
        // of our Firebase database.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyLogin(uid.getText().toString(), pw.getText().toString(), new LoginVerificationCallback() {
                    @Override
                    public void onVerificationComplete(boolean isSuccess) {
                        if (isSuccess) {
                            Log.v("loginActivity", "1");
                            Intent intent = new Intent(Login_activity.this, DisplayActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong username/password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


                /*
                \
                Log.v("loginActivity", verifyLogin(uid.getText().toString(),pw.getText().toString())+"  answer");
                if(verifyLogin(uid.getText().toString(),pw.getText().toString()))
                {
                    Log.v("loginActivity", "1");
                    Intent intent = new Intent(Login_activity.this, DisplayActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong username/password.", Toast.LENGTH_SHORT).show();
                }
                 */


    }




    public void  verifyLogin(String X, String U,LoginVerificationCallback callback) {

        boolean[] isSuccess = {false};
        // below line is used to get
        // reference for our database.

        Log.v("loginActivity","1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot snap :  dataSnapshot.getChildren())
            {
                String s=snap.child("Username").getValue().toString();
                Log.v("loginActivity",s);
                if(s.equals(X))
                {
                    String p=snap.child("password").getValue().toString();
                    if(p.equals(U)) {
                        Log.v("loginActivity",p);
                        welcome.setText("good");
                        isSuccess[0]=true;
                        break;
                    }
                }

            }
                if (isSuccess[0]) {
                    callback.onVerificationComplete(true);
                } else {
                    callback.onVerificationComplete(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.v("loginActivity","ERROR");
            }
        });
        Log.v("loginActivity", isSuccess[0]+"  is the return value");
    }

    public interface LoginVerificationCallback {
        void onVerificationComplete(boolean isSuccess);
    }
}