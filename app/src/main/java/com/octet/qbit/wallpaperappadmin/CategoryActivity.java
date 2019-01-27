package com.octet.qbit.wallpaperappadmin;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import support_package.CategoryData;

public class CategoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE=100;
    private Uri imageUri, uploadedImgUrl;
    private ImageView imageView;
    private EditText categoryEditText, descEditText;
    private String categoryString, descString, thumbNameString, uploadedImgUrlString, temp;
    private FirebaseDatabase fireDb;
    private DatabaseReference fireDbRef;
    private FirebaseStorage fireStorage;
    private StorageReference fireStorageRef;
    private UploadTask uploadTask;
    private Task<Uri> urlTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        imageView = findViewById(R.id.catImageView);
        categoryEditText = findViewById(R.id.categoryEditText);
        descEditText = findViewById(R.id.descEditText);

        fireDb = FirebaseDatabase.getInstance();
    }

    public void onClickBrowseImage(View view) {
        //http://www.androhub.com/android-gallery-image-picker/
        Intent imgPicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imgPicker, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            //log//
            Log.i("customLog", "onActivityResult:"+imageUri.toString());
        }
    }

    public void onClickUploadCat(View view) {
        categoryString=categoryEditText.getText().toString().trim();
        descString=descEditText.getText().toString().trim();
        thumbNameString=categoryString.concat(".jpg");
        if(!categoryString.isEmpty()&& !descString.isEmpty())
        {
            fireDbRef=fireDb.getReference("category");
            fireDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child(categoryString).exists())
                    {
                        Toast.makeText(CategoryActivity.this, "Uploading File", Toast.LENGTH_SHORT).show();
                        uploadFile();
                        updateDatabase();
                    }
                    else
                        Toast.makeText(CategoryActivity.this, "Entered Category already exist", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            //fireDbRef.child(categoryString).child("link").setValue(urlTask.getResult().toString());
            //Log.i("customLog", "onActivityResult | stored urltask = "+urlTask.getResult().toString());
        }
    }

    public void uploadFile(){
        fireStorage = FirebaseStorage.getInstance();
        fireStorageRef= fireStorage.getReference(categoryString+"/"+thumbNameString);

        uploadTask = fireStorageRef.putFile(imageUri);
        urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fireStorageRef.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uploadedImgUrlString =(String)uri.toString();
                temp = uploadedImgUrlString;
                Log.i("customLog", "onActivityResult | stored url = "+uploadedImgUrlString);
                Toast.makeText(CategoryActivity.this, uploadedImgUrlString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDatabase(){
        CategoryData catData = new CategoryData(descString, temp);
        fireDbRef.child(categoryString).setValue(catData);
    }
}
