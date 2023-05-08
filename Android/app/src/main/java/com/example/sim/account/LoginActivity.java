package com.example.sim.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.dto.account.LoginDTO;
import com.example.sim.dto.account.LoginResponse;
import com.example.sim.dto.account.RegisterDTO;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {


    TextInputLayout tfEmail, tfPassword;
    TextInputEditText txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        tfEmail = findViewById(R.id.tfEmail);
        tfPassword = findViewById(R.id.tfPassword);

        setupError();
    }

    public void onClickLogin(View view) {
        if (!validation()) {
            return;
        }
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(txtEmail.getText().toString());
        loginDTO.setPassword(txtPassword.getText().toString());
        CommonUtils.showLoading();
        ApplicationNetwork.getInstance()
                .getAccountApi()
                .login(loginDTO)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.isSuccessful())
                        {
                            String token = response.body().getToken();
                            System.out.println("token : " + token);
                            Toast.makeText(LoginActivity.this, "Success " + token, Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        System.out.println("error : " + t.getMessage());
                        CommonUtils.hideLoading();
                    }
                });
    }


    private boolean validation() {
        boolean isValid = true;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();


        if (email.isEmpty()) {
            txtEmail.setError("Введіть email");
            isValid = false;
        } else {
            txtEmail.setError(null);
        }
        if (password.isEmpty()) {
            txtPassword.setError("Введіть пароль");
            isValid = false;
        } else {
            txtPassword.setError(null);
        }


        return isValid;

    }

    private void setupError() {


        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if ((!TextUtils.isEmpty(text) && !Patterns.EMAIL_ADDRESS.matcher(text).matches())) {
                    tfEmail.setError("Невірний формат email");
                } else {
                    tfEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() == 0) {
                    tfPassword.setError("Введіть пароль");
                } else {
                    tfPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


}
