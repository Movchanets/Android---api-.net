package com.example.sim.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sim.BaseActivity;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.contants.Urls;

public class ProfileActivity extends BaseActivity {

    TextView txtEmail, txtFirstName, txtLastName;
    ImageView imgProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtEmail = findViewById(R.id.txtEmail);
        txtFirstName = findViewById(R.id.txtFirstName);
              txtLastName = findViewById(R.id.txtLastName);
        String token = HomeApplication.getInstance().getToken();
        String [] parts = token.split("\\.");
        String payload = parts[1];
        String payloadDecoded = new String(Base64.decode(payload, Base64.DEFAULT));
        String [] payloadParts = payloadDecoded.split(",");
        String email = payloadParts[0].split(":")[1];
        String firstName = payloadParts[1].split(":")[1];
        String lastName = payloadParts[2].split(":")[1];
        String image = payloadParts[3].split(":")[1];
        txtEmail.setText("Email: " + email);
        txtFirstName.setText("First Name: " + firstName);
        txtLastName.setText("Last Name: " +lastName);
        imgProfile = findViewById(R.id.profileImage);
        Glide.with(this).load(Urls.BASE + "/images/" + image.replace(
                "\"", "")).into(imgProfile);

    }
    public void onClickEditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}