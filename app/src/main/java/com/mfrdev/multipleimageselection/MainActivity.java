package com.mfrdev.multipleimageselection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mfrdev.multipleimageselection.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    public static final int PICK_IMAGE_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.uploadButton.setOnClickListener(this::imageUpload);
    }

    private void imageUpload(View view) {
        mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    pickImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Required For Upload Image.", Toast.LENGTH_SHORT).show();
                }
            });

    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageResult.launch(Intent.createChooser(intent, "Select Image(s)"));
    }

    private final ActivityResultLauncher<Intent> pickImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                List<Uri> list = new ArrayList<>();
                if (data.getClipData() != null) {
                    //Select multiple Images
                    ClipData clipData = data.getClipData();
                    int itemCount = clipData.getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        list.add(uri);
                    }
                    Log.i("OnActivityForResult","======================Multiple Image=====================");

                   for (Uri uri: list){
                       Log.i("OnActivityForResult",uri.toString());
                   }

                }else {
                    //Select single image
                    Log.i("OnActivityForResult","======================Single Image=====================");
                    Toast.makeText(getApplicationContext(), "Single Image", Toast.LENGTH_SHORT).show();
                    if(data.getData() != null){
                        Log.i("OnActivityForResult",data.getData().toString());
                    }
                }
            }
        }
    });
}