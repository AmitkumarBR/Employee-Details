package com.example.employeedetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtpActivity extends AppCompatActivity {

    private EditText textInputMobileNo;
    private Button buttonGetOtp;
    private ProgressBar progressBarSendingOtp;
    private SharedPrefManager sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        sharedPrefs = new SharedPrefManager(this);

        if (sharedPrefs.getBoolValue("isLoggedIn", false)) {
            startActivity(new Intent(SendOtpActivity.this, EmployeeListActivity.class));
        } else {
            init();
        }
    }

    private void init() {
        textInputMobileNo = (EditText) findViewById(R.id.text_input_mobileNo);
        buttonGetOtp = (Button) findViewById(R.id.button_get_otp);
        progressBarSendingOtp = (ProgressBar) findViewById(R.id.progressBar_sending_otp);

        buttonGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textInputMobileNo.getText().toString().trim().isEmpty()) {
                    if ((textInputMobileNo.getText().toString().trim()).length() == 10) {

                        progressBarSendingOtp.setVisibility(View.VISIBLE);
                        buttonGetOtp.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + textInputMobileNo.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                SendOtpActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBarSendingOtp.setVisibility(View.GONE);
                                        buttonGetOtp.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBarSendingOtp.setVisibility(View.GONE);
                                        buttonGetOtp.setVisibility(View.VISIBLE);
                                        Toast.makeText(SendOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String BackEndOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBarSendingOtp.setVisibility(View.GONE);
                                        buttonGetOtp.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                                        intent.putExtra("mobile", textInputMobileNo.getText().toString());
                                        intent.putExtra("BackEndOtp", BackEndOtp);
                                        startActivity(intent);
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(SendOtpActivity.this, "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SendOtpActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
