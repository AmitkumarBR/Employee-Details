package com.example.employeedetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText inputOtp1, inputOtp2, inputOtp3, inputOtp4, inputOtp5, inputOtp6;
    private TextView textGetMobileNo, textResendOtp;
    private Button buttonSubmit;
    private ProgressBar progressBarVerifyingOtp;
    private SharedPrefManager sharedPrefs;
    private String getOtpBackEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        sharedPrefs = new SharedPrefManager(this);

        init();

    }

    private void init() {
        inputOtp1 = (EditText) findViewById(R.id.input_otp1);
        inputOtp2 = (EditText) findViewById(R.id.input_otp2);
        inputOtp3 = (EditText) findViewById(R.id.input_otp3);
        inputOtp4 = (EditText) findViewById(R.id.input_otp4);
        inputOtp5 = (EditText) findViewById(R.id.input_otp5);
        inputOtp6 = (EditText) findViewById(R.id.input_otp6);
        textResendOtp = (TextView) findViewById(R.id.text_resend_otp);

        textGetMobileNo = (TextView) findViewById(R.id.text_get_mobileNo);
        textGetMobileNo.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));

        getOtpBackEnd = getIntent().getStringExtra("BackEndOtp");
        progressBarVerifyingOtp = (ProgressBar) findViewById(R.id.progressBar_verifying_otp);

        buttonSubmit = (Button) findViewById(R.id.button_submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputOtp1.getText().toString().trim().isEmpty() && !inputOtp2.getText().toString().trim().isEmpty()) {
                    String enterOtp = inputOtp1.getText().toString() +
                            inputOtp2.getText().toString() +
                            inputOtp3.getText().toString() +
                            inputOtp4.getText().toString() +
                            inputOtp5.getText().toString() +
                            inputOtp6.getText().toString();

                    if (getOtpBackEnd != null) {
                        progressBarVerifyingOtp.setVisibility(View.VISIBLE);
                        buttonSubmit.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getOtpBackEnd, enterOtp
                        );

                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBarVerifyingOtp.setVisibility(View.GONE);
                                        buttonSubmit.setVisibility(View.VISIBLE);

                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getApplicationContext(), EmployeeListActivity.class);
                                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            sharedPrefs.saveBoolValue("isLoggedIn", true);
                                            startActivity(intent);
                                        } else {
                                            sharedPrefs.saveBoolValue("isLoggedIn", false);
                                            Toast.makeText(VerifyOtpActivity.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        sharedPrefs.saveBoolValue("isLoggedIn", false);
                        Toast.makeText(VerifyOtpActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(VerifyOtpActivity.this, "OTP Verify", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Please Enter All Numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpOtp();

        textResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        VerifyOtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String NewBackEndOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getOtpBackEnd = NewBackEndOtp;
                                Toast.makeText(VerifyOtpActivity.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    public void setUpOtp() {

        inputOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        
    }

}