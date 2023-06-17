package com.application.easycook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class OCR  extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // בדיקה אם יש הרשאת מצלמה וקריאת קבצים
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }

        // פונקציה לקריאת התמונה והפעלת זיהוי התווים
        performOCR();
    }

    private void performOCR() {
//        ITesseract tesseract = new Tesseract();
//        tesseract.setDatapath(getFilesDir() + "/tessdata/");
//        tesseract.setLanguage("heb");
//
//        String imagePath = "<path_to_image>"; // יש לציין את הנתיב לתמונה של המסמך
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//
//        try {
//            String result = tesseract.doOCR(bitmap);
//            String[] extractedText = result.split("\\r?\\n");
//
//            for (String line : extractedText) {
//                Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
//            }
//        } catch (TesseractException e) {
//            e.printStackTrace();
//        }
    }
}