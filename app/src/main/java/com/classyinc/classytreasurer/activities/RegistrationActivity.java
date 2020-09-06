package com.classyinc.classytreasurer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.classyinc.classytreasurer.Model.User;

import com.classyinc.classytreasurer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public  class RegistrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;

    private ProgressDialog mDialog;
    //Firebase
    private FirebaseAuth mAuth;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");
        users.keepSynced(true);

        mDialog= new ProgressDialog(this);

        registration();
    }

    private  void registration() {
        mEmail = findViewById(R.id.email_reg_id);
        mPass = findViewById(R.id.pass_reg_id);
        Button btnReg = findViewById(R.id.btn_reg_id);
        TextView mtxtSignin = findViewById(R.id.txt_signinlayout_id);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email Required");
                    return;
                }

                if(TextUtils.isEmpty(pass)) {
                    mPass.setError("Set a Password");
                    return;
                }

                mDialog.setMessage("Processing");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        User user = new User();
                        user.setEmail(mEmail.getText().toString());
                        user.setPassword(mPass.getText().toString());

                        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getApplicationContext(),"registered succcessfully",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                        mDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                                mDialog.dismiss();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    }
                });

            }
        });

        mtxtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}


