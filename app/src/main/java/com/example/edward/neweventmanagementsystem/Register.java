package com.example.edward.neweventmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edward.neweventmanagementsystem.Model.Organizer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private static final  String TAG = "Register";
    private EditText einputEmail, einputPassword, eusername, ephone;
    private Button ebtnSignUp;
    private ProgressBar progressBar;
    private TextView txtlogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

//        eusername = (EditText) findViewById(R.id.username);
//        ephone = (EditText) findViewById(R.id.phone);
        einputPassword = (EditText) findViewById(R.id.password);
        einputEmail = (EditText) findViewById(R.id.email);

        ebtnSignUp =(Button)findViewById(R.id.sign_up_button);
        txtlogin =  (TextView)findViewById(R.id.loginText);

//        //Init Firebase
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference table_user = database.getReference("Organizer");

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginPage =  new Intent(Register.this, com.example.edward.neweventmanagementsystem.LoginActivity.class);
                startActivity(loginPage);
            }
        });

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


//        ebtnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ProgressDialog mDialog = new ProgressDialog(Register.this);
//                mDialog.setMessage("Please waiting...");
//                mDialog.show();
//
//                table_user.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        //Check if already user phone
//                        if(dataSnapshot.child(ephone.getText().toString()).exists()){
//                            mDialog.dismiss();
//                            Toast.makeText(Register.this, "Phone Number already register", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            if (ephone.length()== 10 || ephone.length()==11 ) {
//
//                                mDialog.dismiss();
//                                Organizer organizer = new Organizer(eusername.getText().toString(), ephone.getText().toString(), einputEmail.getText().toString(), einputPassword.getText().toString());
//                                table_user.child(ephone.getText().toString()).setValue(organizer);
//
//                                Toast.makeText(Register.this, "Sign Up successfully!", Toast.LENGTH_SHORT).show();
//                                Intent intent =  new Intent(Register.this, com.example.edward.neweventmanagementsystem.LoginActivity.class);
//                                startActivity(intent);
//
//                            }else{
//                                mDialog.dismiss();
//                                Toast.makeText(Register.this, "Phone Number Not valid", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    public void handleSignUp(View view){
        String email = this.einputEmail.getText().toString();
        String pasword = this.einputPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            einputEmail.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(pasword)){
            einputPassword.setError("Required");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pasword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Create user with email success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i(TAG, "User created successul. Email: " + user.getEmail());
                            Toast.makeText(Register.this, "User created successful. Email:" + user.getEmail(), Toast.LENGTH_SHORT).show();

                            Intent intent =  new Intent(Register.this, com.example.edward.neweventmanagementsystem.LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Log.w(TAG, "createuserWithEmail:faiure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
