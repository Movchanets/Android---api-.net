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
import com.example.sim.dto.account.EditAccountDTO;
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

public class EditProfileActivity extends BaseActivity {

    private static int SELECT_IMAGE_RESULT = 300;
    Uri uri = null;
    ImageView IVPreviewImage;
    TextView tfPhoto;
    TextInputLayout tfLastName, tfFirstName, tfPassword, tfConfirmPassword, tfOldPassword;
    TextInputEditText txtLastName, txtFirstName, txtPassword, txtConfirmPassword, txtOldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        txtLastName = findViewById(R.id.txtLastName);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        tfPhoto = findViewById(R.id.tfPhoto);
        tfLastName = findViewById(R.id.tfLastName);
        tfFirstName = findViewById(R.id.tfFirstName);
        tfPassword = findViewById(R.id.tfPassword);
        tfConfirmPassword = findViewById(R.id.tfConfirmPassword);
        tfOldPassword = findViewById(R.id.tfOldPassword);
        txtOldPassword = findViewById(R.id.txtOldPassword);
        setupError();
    }

    public void onClickChange(View view) {
        if (!validation()) {
            return;
        }
        EditAccountDTO editAccountDTO = new EditAccountDTO();
        editAccountDTO.setFirstName(txtFirstName.getText().toString());
        editAccountDTO.setLastName(txtLastName.getText().toString());
        editAccountDTO.setPassword(txtOldPassword.getText().toString());
        editAccountDTO.setNewPassword(txtPassword.getText().toString());
        String token = HomeApplication.getInstance().getToken();
        String [] parts = token.split("\\.");
        String payload = parts[1];
        String payloadDecoded = new String(Base64.decode(payload, Base64.DEFAULT));
        String [] payloadParts = payloadDecoded.split(",");
        String email = payloadParts[0].split(":")[1];
        editAccountDTO.setEmail(email.replace("\"", ""));
        editAccountDTO.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork.getInstance()
                .getAccountApi()
                .update(editAccountDTO)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);


                        String token = response.body().getToken();
                        HomeApplication.getInstance().saveJwtToken(token);
                        startActivity(intent);
                        finish();
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                        Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        System.out.println("error : " + t.getMessage());
                        CommonUtils.hideLoading();
                    }
                });
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
        if(uri == null){
            Toast.makeText(this, "Виберіть фото", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean isValid = true;
        String name = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String oldPassword = txtOldPassword.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();
        if (name.isEmpty()) {
            txtFirstName.setError("Введіть ім'я");
            isValid = false;
        } else {
            txtFirstName.setError(null);
        }
        if (lastName.isEmpty()) {
            txtLastName.setError("Введіть прізвище");
            isValid = false;
        } else {
            txtLastName.setError(null);
        }
        if (oldPassword.isEmpty()) {
            txtOldPassword.setError("Введіть старий пароль");
            isValid = false;
        } else {
            txtOldPassword.setError(null);
        }
        if (password.isEmpty()) {
            txtPassword.setError("Введіть пароль");
            isValid = false;
        } else {
            txtPassword.setError(null);
        }
        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.setError("Підтвердіть пароль");
            isValid = false;
        } else {
            txtConfirmPassword.setError(null);
        }
        if (!password.equals(confirmPassword)) {
            txtConfirmPassword.setError("Паролі не співпадають");
            isValid = false;
        } else {
            txtConfirmPassword.setError(null);
        }
        if (uri == null) {
            Toast.makeText(this, "Виберіть фото", Toast.LENGTH_SHORT).show();
            tfPhoto.setError("Виберіть фото");
            isValid = false;
        } else {
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
                if (text.length() == 0) {
                    tfFirstName.setError("Введіть ім'я");
                } else {
                    tfFirstName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() == 0) {
                    tfLastName.setError("Введіть прізвище");
                } else {
                    tfLastName.setError(null);
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
        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() == 0) {
                    tfConfirmPassword.setError("Підтвердіть пароль");
                }
                if (text.length() != 0 && !text.toString().equals(txtPassword.getText().toString())) {
                    tfConfirmPassword.setError("Паролі не співпадають");
                } else {
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
