package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    FirebaseAuth mAuth;
    String codeSent;
    String phoneNumber;
    String phone;

    EditText editTextPhone, editTextCode;
    Button getVerification, signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);



        mAuth = FirebaseAuth.getInstance();

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCode = findViewById(R.id.editTextCode);

        getVerification = findViewById(R.id.buttonGetVerificationCode);
        getVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerification();
            }
        });

        signIn = findViewById(R.id.buttonSignIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignIn();
            }
        });


    }

    private void verifySignIn() {
        String code = editTextCode.getText().toString().trim();
        if(code.isEmpty()){
            editTextCode.setError("인증번호를 입력하세요");
            editTextCode.requestFocus();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"인증성공",Toast.LENGTH_LONG).show();
                            goToSignUpPage();
                            // ...
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"인증실패",Toast.LENGTH_LONG).show();
                                Log.e("인증","인증실패");
                            }
                        }
                    }
                });
    }

    private void sendVerification() {
        phone= editTextPhone.getText().toString().trim();
        if(phone.length()!=0){
            phone= editTextPhone.getText().toString().trim().substring(1);
        }
        Log.e("전화",phone);
        phoneNumber = phone;
        if(!phoneNumber.isEmpty()){
            phoneNumber ="+82"+phone;
        }
        Log.e("전화번호 인증버튼",phoneNumber);

        if(phoneNumber.isEmpty()){
            editTextPhone.setError("전화번호를 입력하세요!");
            editTextPhone.requestFocus();
            return;
        }
        if(phoneNumber.length()<10){
            editTextPhone.setError("유효한 전화번호를 입력하세요!");
            editTextPhone.requestFocus();
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent =s;
        }
    };


    public void goToSignUpPage(){
        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }

}
