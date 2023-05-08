package com.example.sim.category;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.contants.Urls;
import com.example.sim.dto.category.CategoryCreateDTO;
import com.example.sim.dto.category.CategoryItemDTO;
import com.example.sim.dto.category.CategoryDTO;
import com.example.sim.dto.category.CategoryUpdateDTO;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends BaseActivity {

    private static int SELECT_IMAGE_RESULT=300;
    private int CategoryId;
    Uri uri = null;
    ImageView imageView;
    TextInputEditText txtCategoryName, txtCategoryPriority, txtCategoryDescription;
    TextInputLayout tfCategoryName, tfCategoryPriority, tfCategoryDescription;
    TextView tfPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        CategoryId = id;
        setContentView(R.layout.activity_edit_category);
        txtCategoryName = findViewById(R.id.txtCategoryName);
        tfPhoto = findViewById(R.id.tfPhoto);
        txtCategoryPriority = findViewById(R.id.txtCategoryPriority);
        txtCategoryDescription = findViewById(R.id.txtCategoryDescription);
        tfCategoryName = findViewById(R.id.tfCategoryName);
        tfCategoryPriority = findViewById(R.id.tfCategoryPriority);
        tfCategoryDescription = findViewById(R.id.tfCategoryDescription);
        imageView = findViewById(R.id.IVPreviewImage);

        ApplicationNetwork.getInstance()
                .getJsonApi()
                .get(id)
                .enqueue(new Callback<CategoryDTO>() {
                    @Override
                    public void onResponse(Call<CategoryDTO> call, Response<CategoryDTO> response) {
                        CategoryDTO categoryDTO = response.body();

                        //imageView.setImageBitmap(categoryDTO.getImage()
                        txtCategoryName.setText(categoryDTO.getName());
                      txtCategoryPriority.setText(String.valueOf(categoryDTO.getPriority()));
                       txtCategoryDescription.setText(categoryDTO.getDescription());
                        Glide.with(HomeApplication.getAppContext())
                                .load(Urls.BASE + "/images/" + categoryDTO.getImage())
                                .apply(new RequestOptions().override(600))
                                .into(imageView);

                    }

                    @Override
                    public void onFailure(Call<CategoryDTO> call, Throwable t) {

                    }
                });
        setupError();

    }
    private void setupError() {
        txtCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() <= 2) {
                    tfCategoryName.setError(getString(R.string.category_name_required));
                } else {
                    tfCategoryName.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtCategoryPriority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                int number = 0;
                try {
                    number = Integer.parseInt(text.toString());
                } catch (Exception ex) {
                }
                if (number <= 0) {
                    tfCategoryPriority.setError(getString(R.string.category_priority_required));
                }
                else {
                    tfCategoryPriority.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtCategoryDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() <= 2) {
                    tfCategoryDescription.setError(getString(R.string.category_description_required));
                } else {
                    tfCategoryDescription.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private boolean validation() {
        boolean isValid=true;
        String name = txtCategoryName.getText().toString();
        if(name.isEmpty() || name.length()<=2) {
            tfCategoryName.setError(getString(R.string.category_name_required));
            isValid=false;
        }
        int number = 0;
        try {
            number = Integer.parseInt(txtCategoryPriority.getText().toString());
        }catch(Exception ex) {}
        if(number<=0) {
            tfCategoryPriority.setError(getString(R.string.category_priority_required));
            isValid=false;
        }

        String description = txtCategoryDescription.getText().toString();
        if(description.isEmpty() || description.length()<=2) {
            tfCategoryDescription.setError(getString(R.string.category_description_required));
            isValid=false;
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
            imageView.setImageURI(uri);
        }
    }
    public void onClickSelectImagePig(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_IMAGE_RESULT);
    }
    public void onClickEditCategory(View view) {
        if(!validation())
            return;
        CategoryUpdateDTO model = new CategoryUpdateDTO();
        model.setId(CategoryId);
        model.setName(txtCategoryName.getText().toString());
        model.setPriority(Integer.parseInt(txtCategoryPriority.getText().toString()));
        model.setDescription(txtCategoryDescription.getText().toString());
        model.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork.getInstance()
                .getJsonApi()
                .update(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Intent intent = new Intent(EditCategoryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
        CommonUtils.hideLoading();
    }
}