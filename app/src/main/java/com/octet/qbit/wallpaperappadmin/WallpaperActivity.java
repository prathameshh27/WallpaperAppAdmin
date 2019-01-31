package com.octet.qbit.wallpaperappadmin;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    private static final int PICK_IMAGE=100;
    private Uri imageUri, uploadedImgUrl;
    private ImageView addImageButton, mainImage;
    private AlertDialog categorySelector;
    private List<String> categoryList;
    private String categoryStringArray[], tempStringArray[];
    private Button selectCatButton;
    private Adapter adapter;
    private DatabaseReference fireDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        addImageButton = findViewById(R.id.addImageButon);
        mainImage = findViewById(R.id.mainImageView);
        selectCatButton = findViewById(R.id.selectCatButton);
        //categoryStringArray = new String[] {"item 1","item 2","item 3","item 4","item 5"};

        fireDbRef = FirebaseDatabase.getInstance().getReference("category");
        fireDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                categoryStringArray = new String[(int)dataSnapshot.getChildrenCount()];
                Log.i("customLog","L58. itemCount: " + (int)dataSnapshot.getChildrenCount());
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    Log.i("customLog","L61. category: " + data.getKey().toString());
                    categoryStringArray[i]= data.getKey().toString();
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mainImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickBrowseImage(v);
                return false;
            }
        });
    }

    public void onClickBrowseImage(View view) {
        addImageButton.setVisibility(View.GONE);
        Intent imgPicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imgPicker, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode == RESULT_OK)
        {
            imageUri=data.getData();
            mainImage.setImageURI(imageUri);
        }
    }

    public void onClickUploadImg(View view) {
        Toast.makeText(this, "Under construction", Toast.LENGTH_SHORT).show();
    }

    public void onClickSelectCat(View view) {

        categorySelector = new AlertDialog.Builder(this)
                .setTitle("Choose Category")
                .setSingleChoiceItems(categoryStringArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         selectCatButton.setText(categoryStringArray[which]);
                         dialog.cancel();
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        categorySelector.show();

    }
}
