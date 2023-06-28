package com.application.easycook;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.MyPantryPackage.PantryFragment;
import com.application.easycook.RecipesPackage.RecipeFragment;
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

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
   ActivityHomeBinding binding;
   HtmlParser parser;
    public MyPantry myPantry;
    private static final String TAG = "HomeActivity";
    ProgressBar progressBar;


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

                    break;
                case R.id.navigation_pantry:
//                    Intent intent = new Intent(getApplicationContext(), PantryActivity.class);
//                    startActivity(intent);
//                    set_firestorDB();
                    replaceFragment(new PantryFragment(myPantry));
                    break;
                case R.id.navigation_riceps:

//                    loadHtmlToFirebase();
//                    parser.showData();
                    Log.d(TAG, "onClick: Switching Activities.");
//                    Intent intent = new Intent(HomeActivity.this, Firebase_connecting.class);
//                    startActivity(intent);
                    replaceFragment(new RecipeFragment(myPantry));
                    break;


            }


            return true;
        });



    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
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
//

//


    }





        
    

}