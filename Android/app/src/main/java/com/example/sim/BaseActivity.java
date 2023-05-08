package com.example.sim;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sim.account.LoginActivity;
import com.example.sim.account.RegisterActivity;
import com.example.sim.category.CategoryCreateActivity;
import com.example.sim.utils.CommonUtils;

public class BaseActivity extends AppCompatActivity {
    public BaseActivity() {
        CommonUtils.setContext(this);
    }
    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main, menu);
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
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
        case R.id.m_create:
            try {

                intent = new Intent(this, CategoryCreateActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
        case R.id.register:
            try {

                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
        case R.id.login:
            try {

                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
              default:
                return super.onOptionsItemSelected(item);
         }
   }
}
