package com.application.easycook;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.MyPantryPackage.PantryFragment;
import com.application.easycook.databinding.ActivityHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
   ActivityHomeBinding binding;
   HtmlParser parser;
    public MyPantry myPantry;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        parser= new HtmlParser(getBaseContext());
        setContentView(R.layout.activity_home);
        myPantry=new MyPantry(getBaseContext());
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
//                    HTMLProcessor h=new HTMLProcessor();
//                    h.processHTML();
                    Intent intent1 = new Intent(HomeActivity.this, DataBaseLoader.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_pantry:
//                    Intent intent = new Intent(getApplicationContext(), PantryActivity.class);
//                    startActivity(intent);
                    replaceFragment(new PantryFragment(myPantry));
                    break;
                case R.id.navigation_riceps:

//                    loadHtmlToFirebase();
//                    parser.showData();
                    Log.d(TAG, "onClick: Switching Activities.");
                    Intent intent = new Intent(HomeActivity.this, Firebase_connecting.class);
                    startActivity(intent);
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
    private void loadHtmlToFirebase()  {


        try {
            Document doc;
//            File inputStream=new File("app/src/main/res/Html-product_from_shofersal/veg-fresh.html");
            InputStream inputStream = getResources().getAssets().open("veg-fresh.html");
//            InputStream inputStream = getResources().getAssets().open("file")
            doc = Jsoup.parse(inputStream, "UTF-8", "http://example.com/");
//            System.out.println(doc);
            Elements data = doc.select("main#main");
            for (Element li : data.select("section.tileSection3 li")) {
                String imgsrc = li.select("img").attr("src");
                if (imgsrc == "")
                    continue;
                System.out.println("String imgsrc: " + imgsrc);
                String title = li.attr("data-product-name");
                System.out.println("Title:"+title);
                if (!imgsrc.contains("http"))
                    continue;
                Log.d("items", "img: " + imgsrc + " title: " + title);
            }
//            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//
//            Document doc;
//            InputStream inputStream = getAssets().open("Html-product_from_shofersal/veg-fresh.html");
//
//            doc = Jsoup.parse(inputStream, "UTF-8", "http://example.com/");
//            System.out.println(doc);
//            Elements data = doc.select("main#main");
//            for (Element li : data.select("section.tileSection3 li")) {
//                String imgsrc = li.select("a").select("img").attr("src");
//                System.out.println("lllllllllllll: " + imgsrc);
//                String title = li.select("div.text.description").select("strong").text();
//                if (!imgsrc.contains("http"))
//                    continue;
//                Log.d("items", "img: " + imgsrc + " title: " + title);
//
//            }
//
//
//            System.out.println(data);
//            int size = data.size();
//            if (data.contains("section.tileSection3")) {
//
//            }
//        }catch (Exception exception){
//            System.out.println(exception);
//
//        }

//


    }
    private static class HtmlParsingTask extends AsyncTask<String, Void, Void> {
        private Context context;
        public ArrayList<String> titels;
        public ArrayList<String> images;


        public HtmlParsingTask(Context context) {
            this.context = context;
        }



        @Override
        protected Void doInBackground(String... fileNames) {
            String fileName = fileNames[0];
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open(fileName);
                Document doc = Jsoup.parse(inputStream, "UTF-8", "");
//                Document document = Jsoup.parse(doc.outerHtml());
                Elements data = doc.select("main#main");
                for (Element li : data.select("section.tileSection3 li")) {
                    String imgsrc = li.select("a").select("img").attr("src");
                    System.out.println("String imgsrc: " + imgsrc);
                    String title = li.select("div.text.description").select("strong").text();
                    System.out.println("Title:"+title);
                    if (!imgsrc.contains("http"))
                        continue;
                    Log.d("items", "img: " + imgsrc + " title: " + title);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }



        
    

}