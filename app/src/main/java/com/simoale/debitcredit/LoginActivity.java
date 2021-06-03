package com.simoale.debitcredit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.utils.Utilities;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout passwordTextInput;
    private Button loginButton;
    private ImageButton buttonFingerprint;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getSupportActionBar().setTitle("DebitCredit");


        this.passwordTextInput = findViewById(R.id.login_password_textInput);
        this.loginButton = findViewById(R.id.login_button);
        this.buttonFingerprint = findViewById(R.id.button_fingerprint);
        SharedPreferences preferences = getSharedPreferences("pwd", MODE_PRIVATE);
        String password = preferences.getString("password", "-1");
        if (password.equals("-1")) {
            this.handleFirstLogin();
        } else {
            this.executeFingerprintLogin();
            this.loginButton.setOnClickListener(v -> {
                String val = this.passwordTextInput.getEditText().getText().toString();
                if (val.equals(password)) {
                    this.startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void executeFingerprintLogin() {


        executor = ContextCompat.getMainExecutor(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt(LoginActivity.this,
                    executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(),
                            "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use standard login")
                    .build();
            biometricPrompt.authenticate(promptInfo);
            this.buttonFingerprint.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));
        } else {
            this.buttonFingerprint.setVisibility(View.INVISIBLE);
        }
    }

    private void handleFirstLogin() {
        setContentView(R.layout.change_password);
        TextInputLayout pwd1 = findViewById(R.id.new_password_layout);
        TextInputLayout pwd2 = findViewById(R.id.confirm_password_layout);
        Button save = findViewById(R.id.change_password_save_button);
        Button cancel = findViewById(R.id.change_password_cancel_button);
        cancel.setVisibility(View.INVISIBLE);
        save.setOnClickListener(v -> {
            String p1;
            String p2;
            p1 = pwd1.getEditText().getText().toString();
            p2 = pwd2.getEditText().getText().toString();
            if (Utilities.checkDataValid(p1, p2)) {
                if (p1.equals(p2)) {
                    SharedPreferences preferences = getSharedPreferences("pwd", MODE_PRIVATE);
                    preferences.edit().putString("password", p1).apply();
                    this.startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Password must be not empty", Toast.LENGTH_LONG).show();
            }
        });
    }


}