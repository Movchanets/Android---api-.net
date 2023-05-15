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
import com.example.sim.application.HomeApplication;
import com.example.sim.dto.account.LoginResponse;
import com.example.sim.dto.account.RegisterDTO;
import com.example.sim.dto.account.ValidationRegisterDTO;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private static int SELECT_IMAGE_RESULT = 300;
    Uri uri = null;
    ImageView IVPreviewImage;
    TextView tfPhoto;
    TextInputLayout tfLastName, tfFirstName, tfEmail, tfPassword, tfConfirmPassword;
    TextInputEditText txtLastName, txtFirstName, txtEmail, txtPassword, txtConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtLastName = findViewById(R.id.txtLastName);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        tfPhoto = findViewById(R.id.tfPhoto);
        tfLastName = findViewById(R.id.tfLastName);
        tfFirstName = findViewById(R.id.tfFirstName);
        tfEmail = findViewById(R.id.tfEmail);
        tfPassword = findViewById(R.id.tfPassword);
        tfConfirmPassword = findViewById(R.id.tfConfirmPassword);
        setupError();
    }

    public void onClickRegister(View view) {
        if (!validation()) {
          return;
        }
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setLastName(txtLastName.getText().toString());
        registerDTO.setFirstName(txtFirstName.getText().toString());
        registerDTO.setEmail(txtEmail.getText().toString());
        registerDTO.setPassword(txtPassword.getText().toString());
        registerDTO.setConfirmPassword(txtConfirmPassword.getText().toString());
        registerDTO.setPhoto(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork.getInstance()
                .getAccountApi()
                .register(registerDTO)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        String errorBody = null;
                        try {
                            errorBody = response.errorBody().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        showErrorsServer(errorBody);
                        String token = response.body().getToken();
                        HomeApplication.getInstance().saveJwtToken(token);
                        startActivity(intent);
                        finish();
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        System.out.println("error : " + t.getMessage());
                        CommonUtils.hideLoading();
                    }
                });
    }
    private void showErrorsServer(String json) {
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json, ValidationRegisterDTO.class);
        String str="";
        if(result.getErrors().getEmail()!=null) {
            for (String item: result.getErrors().getEmail())
                str+=item;
        }

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    private String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);

        } catch (Exception ex) {
            return null;
        }
    }

    private boolean validation() {
        boolean isValid=true;
        String name = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();
        if(name.isEmpty())
        {
            txtFirstName.setError("Введіть ім'я");
            isValid = false;
        }
        else
        {
            txtFirstName.setError(null);
        }
        if(lastName.isEmpty())
        {
            txtLastName.setError("Введіть прізвище");
            isValid = false;
        }
        else
        {
            txtLastName.setError(null);
        }
        if(email.isEmpty())
        {
            txtEmail.setError("Введіть email");
            isValid = false;
        }
        else
        {
            txtEmail.setError(null);
        }
        if(password.isEmpty())
        {
            txtPassword.setError("Введіть пароль");
            isValid = false;
        }
        else
        {
            txtPassword.setError(null);
        }
        if(confirmPassword.isEmpty())
        {
            txtConfirmPassword.setError("Підтвердіть пароль");
            isValid = false;
        }
        else
        {
            txtConfirmPassword.setError(null);
        }
        if(!password.equals(confirmPassword))
        {
            txtConfirmPassword.setError("Паролі не співпадають");
            isValid = false;
        }
        else
        {
            txtConfirmPassword.setError(null);
        }
        if(uri == null)
        { Toast.makeText(this, "Виберіть фото", Toast.LENGTH_SHORT).show();
            tfPhoto.setError("Виберіть фото");
            isValid = false;
        }
        else
        {
            tfPhoto.setError(null);
        }
        return isValid;

    }
    private void setupError() {
        txtFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                   if(text.length() == 0)
                    {
                        tfFirstName.setError("Введіть ім'я");
                    }
                    else
                    {
                        tfFirstName.setError(null);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if(text.length() == 0)
                {
                    tfLastName.setError("Введіть прізвище");
                }
                else
                {
                    tfLastName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                  if((!TextUtils.isEmpty(text) && !Patterns.EMAIL_ADDRESS.matcher(text).matches())){
                    tfEmail.setError("Невірний формат email");
                }
                else
                {
                    tfEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if(text.length() == 0)
                {
                    tfPassword.setError("Введіть пароль");
                }
                else
                {
                    tfPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if(text.length() == 0)
                {
                    tfConfirmPassword.setError("Підтвердіть пароль");
                }
                if (text.length() != 0 && !text.toString().equals(txtPassword.getText().toString()))
                {
                    tfConfirmPassword.setError("Паролі не співпадають");
                }
                else
                {
                    tfConfirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_RESULT) {
            uri = (Uri) data.getParcelableExtra("croppedUri");
            IVPreviewImage.setImageURI(uri);
        }
    }

    //Вибір фото і її обрізання
    public void onClickSelectImagePig(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_IMAGE_RESULT);
    }
}
