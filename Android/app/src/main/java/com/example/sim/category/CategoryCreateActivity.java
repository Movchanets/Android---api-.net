package com.example.sim.category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.dto.category.CategoryCreateDTO;
import com.example.sim.service.CategoryNetwork;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCreateActivity extends BaseActivity {

    private static int SELECT_IMAGE_RESULT=300;
    Uri uri = null;
    ImageView IVPreviewImage;
    TextInputEditText txtCategoryName;
    TextInputEditText txtCategoryPriority;
    TextInputEditText txtCategoryDescription;
    TextInputLayout tfCategoryNameLayout;
    TextInputLayout tfCategoryPriorityLayout;
    TextInputLayout tfCategoryDescriptionLayout;
    TextView tfPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        IVPreviewImage=findViewById(R.id.IVPreviewImage);
        txtCategoryName=findViewById(R.id.txtCategoryName);
        tfCategoryNameLayout = findViewById(R.id.tfCategoryName);
        tfCategoryPriorityLayout = findViewById(R.id.tfCategoryPriority);
        tfCategoryDescriptionLayout = findViewById(R.id.tfCategoryDescription);
        txtCategoryPriority=findViewById(R.id.txtCategoryPriority);
        txtCategoryDescription=findViewById(R.id.txtCategoryDescription);
        tfPhoto = findViewById(R.id.tfPhoto);
        validationName(txtCategoryName);
        validationPriority(txtCategoryPriority);
        validationDescription(txtCategoryDescription);
    }
    private  boolean validation()
    {
        boolean isValid = true;
        String categoryName = txtCategoryName.getText().toString();
        String categoryPriority = txtCategoryPriority.getText().toString();
        String categoryDescription = txtCategoryDescription.getText().toString();
        if(categoryName.isEmpty())
        {
            tfCategoryNameLayout.setError("Введіть назву категорії");
            isValid = false;
        }
        else
        {
            tfCategoryNameLayout.setError(null);
        }
        if(categoryPriority.isEmpty() || Integer.parseInt(categoryPriority) < 0)
        {
            tfCategoryPriorityLayout.setError("Введіть пріоритет категорії");
            isValid = false;
        }
        else
        {
            tfCategoryPriorityLayout.setError(null);
        }
        if(categoryDescription.isEmpty())
        {
            tfCategoryDescriptionLayout.setError("Введіть опис категорії");
            isValid = false;
        }
        else
        {
            tfCategoryDescriptionLayout.setError(null);
        }
        if(uri == null)
        {
            tfPhoto.setError("Виберіть фото");
            isValid = false;
        }
        else
        {
            tfPhoto.setError(null);
        }
        return isValid;
    }
    private String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap=null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch(IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);

        } catch(Exception ex) {
            return null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == SELECT_IMAGE_RESULT) {
            uri = (Uri) data.getParcelableExtra("croppedUri");
            IVPreviewImage.setImageURI(uri);
        }
    }
    private void validationName(TextInputEditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = txtCategoryName.getText().toString();
                if(name.isEmpty()) {
                    tfCategoryNameLayout.setError("Вкажіть назву категорії");
                }
                else {
                    tfCategoryNameLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void validationPriority(TextInputEditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = txtCategoryPriority.getText().toString();
                if(name.isEmpty()) {
                    tfCategoryPriorityLayout.setError("Вкажіть пріоритет категорії");
                }
                else {
                    tfCategoryPriorityLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void validationDescription(TextInputEditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = txtCategoryDescription.getText().toString();
                if(description.isEmpty()) {
                    tfCategoryDescriptionLayout.setError("Вкажіть опис категорії");
                }
                else {
                    tfCategoryDescriptionLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    //Додавання категорії - відправка на сервер даних
    public void onClickCreateCategory(View view) {
        if(!validation())
        {
            return;
        }
        CategoryCreateDTO model = new CategoryCreateDTO();
        model.setName(txtCategoryName.getText().toString());
        model.setPriority(Integer.parseInt(txtCategoryPriority.getText().toString()));
        model.setDescription(txtCategoryDescription.getText().toString());
        model.setImageBase64(uriGetBase64(uri));
        CategoryNetwork.getInstance()
                .getJsonApi()
                .create(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Intent intent = new Intent(CategoryCreateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

    //Вибір фото і її обрізання
    public void onClickSelectImagePig(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_IMAGE_RESULT);
    }

}