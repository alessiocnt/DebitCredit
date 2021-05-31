package com.simoale.debitcredit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.simoale.debitcredit.utils.Utilities;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout passwordTextInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getSupportActionBar().setTitle("DebitCredit");
        this.passwordTextInput = findViewById(R.id.login_password_textInput);
        this.loginButton = findViewById(R.id.login_button);
        SharedPreferences preferences = getSharedPreferences("pwd", MODE_PRIVATE);

        String password = preferences.getString("password", "-1");
        if (password.equals("-1")) {
            this.handleFirstLogin();
        }

        Log.e("password", password);

        String finalPassword = password;
        this.loginButton.setOnClickListener(v -> {
            String val = this.passwordTextInput.getEditText().getText().toString();
            if (val.equals(finalPassword)) {
                this.startActivity(new Intent(this, MainActivity.class));
            } else {
                Toast.makeText(this, "Wrong password:" + finalPassword + "/", Toast.LENGTH_LONG);
            }
        });
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
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_LONG);
                }
            } else {
                Toast.makeText(this, "Password must be not empty", Toast.LENGTH_LONG);
            }
        });
    }


}