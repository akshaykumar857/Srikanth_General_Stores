package com.example.srikanthgeneralstores.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.srikanthgeneralstores.R;
import com.example.srikanthgeneralstores.modalClasses.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
EditText emailId,userName,mobile;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Boolean enter=false;
    Button SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailId=findViewById(R.id.emailText);
        SignUp=findViewById(R.id.signup);
        userName=findViewById(R.id.UserName);
        user= FirebaseAuth.getInstance().getCurrentUser();
        mobile=findViewById(R.id.mobile);
        myRef.child("users").child("userdetails").child(user.getUid()).child("mobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mobile.setText(snapshot.getValue().toString());
                enter=true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void SignUp(View view)
    {
        if(enter) {
            String num = mobile.getText().toString();
            String name = userName.getText().toString();
            String email = emailId.getText().toString();
            Users user1 = new Users(num, name, email);
            myRef.child("users").child("userdetails").child(user.getUid()).setValue(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("success", "UserINserted");
                    Toast.makeText(getApplicationContext(), "UserINserted", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),CategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("failed", "Insertion failed");
                    Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}