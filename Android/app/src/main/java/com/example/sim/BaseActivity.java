package com.example.sim;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sim.account.LoginActivity;
import com.example.sim.account.ProfileActivity;
import com.example.sim.account.RegisterActivity;
import com.example.sim.application.HomeApplication;
import com.example.sim.category.CategoryCreateActivity;
import com.example.sim.utils.CommonUtils;

public class BaseActivity extends AppCompatActivity {
    public BaseActivity() {
        CommonUtils.setContext(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.setGroupVisible(R.id.group_anonimus, HomeApplication.getInstance().isAuth());
        menu.setGroupVisible(R.id.group_auth, !HomeApplication.getInstance().isAuth());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.m_home:
                try {

                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return true;
            case R.id.m_create:
                try {

                    intent = new Intent(this, CategoryCreateActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return true;
            case R.id.register:
                try {

                    intent = new Intent(this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return true;
            case R.id.login:
                try {

                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return true;
            case R.id.m_logout:
                try {
                    HomeApplication.getInstance().deleteToken();
                    intent = new Intent(this, LoginActivity.class);

                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return true;
            case R.id.m_profile:
                try {
                    intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
