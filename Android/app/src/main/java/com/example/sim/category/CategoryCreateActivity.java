package com.example.sim.category;

import static com.oginotihiro.cropview.Crop.REQUEST_PICK;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sim.BaseActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.io.File;

public class CategoryCreateActivity extends BaseActivity implements View.OnClickListener  {
    private CropView cropView;
    private ImageView resultIv;
    private LinearLayout btnlay;
    private Button doneBtn;
    private Button cancelBtn;

    private Bitmap croppedBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        cropView = (CropView) findViewById(R.id.cropView);
        resultIv = (ImageView) findViewById(R.id.resultIv);
        btnlay = (LinearLayout) findViewById(R.id.btnlay);
        doneBtn = (Button) findViewById(R.id.doneBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        doneBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick:
                reset();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                startActivityForResult(intent, REQUEST_PICK);
                return true;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK) {
            Uri source = data.getData();

            cropView.setVisibility(View.VISIBLE);
            btnlay.setVisibility(View.VISIBLE);

            cropView.of(source).asSquare().initialize(CategoryCreateActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.doneBtn) {
            final ProgressDialog dialog = ProgressDialog.show(CategoryCreateActivity.this, null, "Please wait…", true, false);

            cropView.setVisibility(View.GONE);
            btnlay.setVisibility(View.GONE);
            resultIv.setVisibility(View.VISIBLE);

            new Thread() {
                public void run() {
                    croppedBitmap = cropView.getOutput();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultIv.setImageBitmap(croppedBitmap);
                        }
                    });

                    Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                    CropUtil.saveOutput(CategoryCreateActivity.this, destination, croppedBitmap, 90);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                }
            }.start();
        } else if (id == R.id.cancelBtn) {
            reset();
        }
    }

    private void reset() {
        cropView.setVisibility(View.GONE);
        resultIv.setVisibility(View.GONE);
        btnlay.setVisibility(View.GONE);
        resultIv.setImageBitmap(null);
    }
}