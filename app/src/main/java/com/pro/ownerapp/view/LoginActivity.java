package com.pro.ownerapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.ownerapp.R;
import com.pro.ownerapp.util.Constant;
import com.pro.ownerapp.util.Utils;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
//    private static final String TAG = "LoginActivity";

    private EditText editTxtPassword;
    private EditText editTxtConfirmPassword;
    private Button btnLogin;
    private boolean mIsRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIsRegistered = preferences.getBoolean(Constant.IS_REGISTERED, false);

        initUI();
    }

    private void initUI() {

        editTxtPassword = (EditText) findViewById(R.id.editTxtPassword);
        editTxtConfirmPassword = (EditText) findViewById(R.id.editTxtConfirmPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(onClickListener);

        if (mIsRegistered) {
            editTxtConfirmPassword.setVisibility(View.GONE);
        } else {
            editTxtConfirmPassword.setVisibility(View.VISIBLE);
            btnLogin.setText(getString(R.string.register));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLogin:
                    if (mIsRegistered) {
                        doLogin();
                    } else {
                        register();
                    }
                    break;
            }
        }
    };

    private void register() {
        String password = editTxtPassword.getText().toString();
        String confirmPassword = editTxtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Utils.showAlertDialog(LoginActivity.this, "Error", "Please enter and confirm password to register.");
        } else {
            if (password.equals(confirmPassword)) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().putBoolean(Constant.IS_REGISTERED, true).commit();
                preferences.edit().putString(Constant.REGISTERED_PASSWORD, password).commit();

                startActivity(new Intent(LoginActivity.this, MessageActivity.class));
            } else {
                Utils.showAlertDialog(LoginActivity.this, "Error", "Passwords do not match!");
            }
        }
    }

    private void doLogin() {
        String password = editTxtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Utils.showAlertDialog(LoginActivity.this, "Error", "Please enter the password to login");
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String registeredPassword = preferences.getString(Constant.REGISTERED_PASSWORD, null);

            if (password.equals(registeredPassword)) {
                startActivity(new Intent(LoginActivity.this, MessageActivity.class));
            } else {
                Utils.showAlertDialog(LoginActivity.this, "Error", "Incorrect Password!");
            }
        }
    }
}
