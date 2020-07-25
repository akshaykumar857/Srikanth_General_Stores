package com.example.srikanthgeneralstores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressBar loading;
    EditText phone,otp;
    Button sendotp,verifyotp;
    Intent intent;
    TextView resend,labelOtp,noOtp,timer;
    String verificationCode;
    String number;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    Boolean vactive=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            homePage();
        }
        loading=findViewById(R.id.loading);
        phone=findViewById(R.id.mobile);
        timer=findViewById(R.id.timer);
        resend=findViewById(R.id.resend);
        noOtp=findViewById(R.id.noOtp);
        labelOtp=findViewById(R.id.labelOtp);
        sendotp=findViewById(R.id.sendotp);
        verifyotp=findViewById(R.id.verifyotp);
        otp=findViewById(R.id.otp);
        mcallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("Vsuccess", "onVerificationCompleted:" + phoneAuthCredential);

                signInWithPhone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w("Vfailed", "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                loading.setVisibility(View.INVISIBLE);
                verificationCode=s;
                Toast.makeText(getApplicationContext(),"Code sent to the Sender",Toast.LENGTH_SHORT).show();
                resend.setVisibility(View.VISIBLE);
                vactive=true;
        setVisible(loading);
            }
        };
    }
    public void updateTimer(int sec)
    {
       String secleft=Integer.toString(sec);
        if(sec<=9)
        {
           secleft="0"+secleft;
        }
        timer.setText(String.format("00:%s", secleft));
    }
    public void homePage()
    {
        intent=new Intent(getApplicationContext(),CategoryActivity.class);
        startActivity(intent);
        finish();
    }
    public void setVisible(View view)
    {
if(vactive) {
    new CountDownTimer(60000+100,1000)
    {
        public void onTick(long millisec)
        {
            updateTimer((int)millisec/1000);
        }
        public void onFinish()
        {
            verifyotp.setEnabled(false);
            Log.i("Done","TimerFinished");
        }

    }.start();
    timer.setVisibility(View.VISIBLE);
    phone.setVisibility(View.INVISIBLE);
    sendotp.setVisibility(View.INVISIBLE);
    otp.setVisibility(View.VISIBLE);
    String mess = "An OTP has been sent to " + number + ".Verify OTP to Login";
    labelOtp.setText(mess);
    labelOtp.setVisibility(View.VISIBLE);
    noOtp.setVisibility(View.VISIBLE);
    verifyotp.setVisibility(View.VISIBLE);
    vactive = false;
}
    }
    public void sendOtp(View view)
    {
        loading.setVisibility(View.VISIBLE);
        verifyotp.setEnabled(true);
        number="+91"+phone.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS,this,mcallback);

    }

    public void signInWithPhone(PhoneAuthCredential cred)
    {
        Boolean succ=true;
        mAuth.signInWithCredential(cred)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"User signed in Successfully",Toast.LENGTH_SHORT).show();
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            myRef.child("users").child("uniqueMob").child(number).runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                    if(currentData.getValue()==null)
                                    {
                                        currentData.setValue(true);
                                        return Transaction.success(currentData);
                                    }return Transaction.abort();
                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
if(committed)
{
    myRef.child("users").child("userdetails").child(user.getUid()).child("mobile").setValue(number).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Log.i("database", "insertsucc");
            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.i("database", "insertfail");
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    });
    intent=new Intent(getApplicationContext(),SignUpActivity.class);
    startActivity(intent);
    finish();
}else
{
    homePage();
}
                                }
                            });

                         }else {
                            Log.w("signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), "User Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("LoginFailure","User singIn failed"+e.getMessage());
            }
        });
    }
    public void verify(View view) {
        String input_code = otp.getText().toString();
        loading.setVisibility(View.VISIBLE);
        if (input_code.equals("")) {
            Log.i("empty", "emptiness");
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
        } else {
            verificationNumber(verificationCode, input_code);
            Log.i("vnumber","vcode");
        }
    }

    public void verificationNumber(String verificationCode, String input_code) {
PhoneAuthCredential cred=PhoneAuthProvider.getCredential(verificationCode,input_code);
signInWithPhone(cred);
    }

}