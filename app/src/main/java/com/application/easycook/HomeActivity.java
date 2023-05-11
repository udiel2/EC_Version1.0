package com.application.easycook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.easycook.databinding.ActivityHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
   ActivityHomeBinding binding;
   static MyPantry myPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myPantry=new MyPantry();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navigation_pantry:
//                    Intent intent = new Intent(getApplicationContext(), PantryActivity.class);
//                    startActivity(intent);
                    replaceFragment(new PantryFragment());
                    break;
                case R.id.navigation_riceps:
                    replaceFragment(new RecipeFragment());
                    break;


            }


            return true;
        });
        //gs://ezcookversion1.appspot.com/EZ_icons/cola.png
//         StorageReference pathReference = storageRef.child("EZ_icons/cola.png");
//        try {
//            File localfile= File.createTempFile("tempfile",".png");
//            pathReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
//                    ImageView imageView= findViewById(R.id.imageView);
//                    imageView.setImageBitmap(bitmap);
//                }
//            });

//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }
    private void MakePanrty(){
         this.myPantry=new MyPantry();

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
}