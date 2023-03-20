package com.application.easycook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
   ///ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StorageReference pathReference = storageRef.child("EZ_icons/cola.png");
        //gs://ezcookversion1.appspot.com/EZ_icons/cola.png
        try {
            File localfile= File.createTempFile("tempfile",".png");
            pathReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    ImageView imageView= findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}