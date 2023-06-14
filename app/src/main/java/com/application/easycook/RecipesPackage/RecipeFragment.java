package com.application.easycook.RecipesPackage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.application.easycook.R;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private String mParam1;
    private String mParam2;
    private FirebaseDatabase fireBase=FirebaseDatabase.getInstance("https://ezcook-9b2ab-default-rtdb.europe-west1.firebasedatabase.app");
    private FirebaseAuth mAuth;

    private ArrayList<String> recipes_names;

    private FirebaseStorage storage;
    private ArrayList<RecipeTitle> recipeTitleArrayList;

    public RecipeFragment(ArrayList<RecipeTitle> recipeTitleArrayList) {
        recipes_names=new ArrayList<>();
        storage=FirebaseStorage.getInstance();
        this.recipeTitleArrayList=recipeTitleArrayList;

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        View subrootView=inflater.inflate(R.layout.recipe_home_fragment,container,false);
        Button button7= rootView.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myTask();
            }
        });
        Button button8=rootView.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter=0;
                for(RecipeTitle recipeTitle:recipeTitleArrayList) {
//                    System.out.println(recipeTitle);
                    FetchRecipeTask backgroundTask = new FetchRecipeTask(new Callback() {
                        /////////https://foody.co.il/foody_recipe/%d7%9c%d7%97%d7%9e%d7%a0%d7%99%d7%95%d7%aa-%d7%91%d7%a9%d7%a8/
                        @Override
                        public void onDocumentReady(Document document) {
                            Recipes recipes=paersRecipes(document);
//                            System.out.println(recipes);
                            firestore.collection("Recipes_details").document(recipeTitle.getId()).set(recipes);


                        }


                        @Override
                        public void onError(Exception e) {
                            System.out.println(e.getMessage());
                            // טיפול בשגיאה
                        }
                    });

                    try {
                        backgroundTask.execute(recipeTitle.getLink());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    counter++;
                }
                System.out.println("---------------------------------Finish!------------------------" +
                        "\n--------------------"+counter+"- Recipes!-----------------");
















            }
        });
        replaceSubFragment(new RecipeSubfragment());

//        for(int i=0;i<10;i++){
//            TextView textView = new TextView(getContext());
//            textView.setText("1111111111111111111");
//        }
        mAuth = FirebaseAuth.getInstance();///https://www.10dakot.co.il/recipe

        DatabaseReference productRef=fireBase.getReference();


        return rootView;
    }
    public void myTask(){///foody.co.il/category/ארוחות/?page=10

        StorageReference fileRef=storage.getReference().child("Foody_recipes").child("Foody.html");
        fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String html = new String(bytes);
                String f_name = fileRef.getName();
                System.out.println("Category Start!!\nName: " + f_name + "\n");
                Document document = Jsoup.parse(html);
                Elements elements=document.select("#main > div > article");
                int counter=0;
                for(Element element: elements.select("#category-feed div[data-title]")){

                    String href=element.select("div > a[href]").attr("href");
                    try {
                        String decodedURL = URLDecoder.decode(href, StandardCharsets.UTF_8.toString());
//                        System.out.println(decodedURL);
                        href = decodedURL;
//                        DatabaseReference productRef=fireBase.getReference();
//                        DatabaseReference myp=productRef.child("Recipes_DB").child("מתכונים ב10 דקות").child(name);
//                        myp.setValue(name);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        System.out.println("\n--!!---Error to Decode: \n"+ href +"\n---------^----------");
                    }
                    String title=element.attr("data-title");
                    String description=element.select("div > section.feed-item-details-container > section > div").text();
                    String imagePath=element.select("div > a > div > img").attr("src");
                    String[] splitStrings = imagePath.split("/");
                    ArrayList<String> resultImagePath = new ArrayList<>();
                    // הוספת התוצאות ל-ArrayList
                    for (int i = 0; i < splitStrings.length; i++) {
                        resultImagePath.add(splitStrings[i]);
                    }
                    String imgAuth=element.select(" div > section.recipe-item-details.d-flex > div > a > img").attr("src");
                    String[] splitStrings2 = imagePath.split("/");
                    ArrayList<String> resultAutrImagePath = new ArrayList<>();
                    // הוספת התוצאות ל-ArrayList
                    for (int i = 0; i < splitStrings2.length; i++) {
                        resultAutrImagePath.add(splitStrings2[i]);
                    }
                    String author=element.select("div > section.recipe-item-details.d-flex > div > ul > li:nth-child(1) > a").text();
                    RecipeTitle recipeTitle=new RecipeTitle(title,"", description, href, resultImagePath, author, resultAutrImagePath);
//                    firestore.collection("Recipes_V1").add(recipeTitle);
//                    System.out.println(recipeTitle.toString());
                    counter++;

                }
                System.out.println("\nCounter: "+counter);
//                                            System.out.println(document);

            }
        });











//        String string1 ="https://foody.co.il/category/";
//        String string2=string1+"ארוחות"+"/";
//        String string3=string2+"?page=2";
//        System.out.println("------------Start Processing---------------");
//        FetchRecipeTask backgroundTask = new FetchRecipeTask(new Callback() {
//            /////////https://foody.co.il/foody_recipe/%d7%9c%d7%97%d7%9e%d7%a0%d7%99%d7%95%d7%aa-%d7%91%d7%a9%d7%a8/
//            @Override
//            public void onDocumentReady(Document document) {
//                Elements elements=document.select("#main > div > article");
//                for(Element element: elements.select("#category-feed div[data-title]")){
//                    String href=element.select("div > a[href]").attr("href");
//                    try {
//                        String decodedURL = URLDecoder.decode(href, StandardCharsets.UTF_8.toString());
////                        System.out.println(decodedURL);
//                        href = decodedURL;
////                        DatabaseReference productRef=fireBase.getReference();
////                        DatabaseReference myp=productRef.child("Recipes_DB").child("מתכונים ב10 דקות").child(name);
////                        myp.setValue(name);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                        System.out.println("\n--!!---Error to Decode: \n"+ href +"\n---------^----------");
//                    }
//                    String title=element.attr("data-title");
//                    String description=element.select("div > section.feed-item-details-container > section > div").text();
//                    String imagePath=element.select("div > a > div > img").attr("src");
//                    String[] splitStrings = imagePath.split("/");
//                    ArrayList<String> resultImagePath = new ArrayList<>();
//                    // הוספת התוצאות ל-ArrayList
//                    for (int i = 0; i < splitStrings.length; i++) {
//                        resultImagePath.add(splitStrings[i]);
//                    }
//                    String imgAuth=element.select(" div > section.recipe-item-details.d-flex > div > a > img").attr("src");
//                    String[] splitStrings2 = imagePath.split("/");
//                    ArrayList<String> resultAutrImagePath = new ArrayList<>();
//                    // הוספת התוצאות ל-ArrayList
//                    for (int i = 0; i < splitStrings2.length; i++) {
//                        resultAutrImagePath.add(splitStrings2[i]);
//                    }
//                    String author=element.select("div > section.recipe-item-details.d-flex > div > ul > li:nth-child(1) > a").text();
//                    RecipeTitle recipeTitle=new RecipeTitle(title, description, href, resultImagePath, author, resultAutrImagePath);
//                    System.out.println(recipeTitle.toString());
//
//                }
//
//            }
//
//
//            @Override
//            public void onError(Exception e) {
//                System.out.println(e.getMessage());
//                // טיפול בשגיאה
//            }
//        });
//
//        try {
//            backgroundTask.execute(string3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private class FetchRecipeTask extends AsyncTask<String, Void, Void> {

        private Callback callback;

        public FetchRecipeTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                String url = urls[0];
                Document document = Jsoup.connect(url).get();
                if (callback != null) {
                    callback.onDocumentReady(document);
                }
            } catch (IOException e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
            return null;
        }
    }
    public interface Callback {
        void onDocumentReady(Document document);
        void onError(Exception e);
    }
    private void replaceSubFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.recipeSubfragment,fragment);
        fragmentTransaction.commit();
    }
    public Recipes paersRecipes(Document document){
        String title=document.select("#main > div > article > section.details-container > div.details.container > h1").text();
        System.out.println(title);
        String description=document.select("#main > div > article > section.details-container > div.details.container > div.description.no-print").text();
        HashMap<String,String> ingediantsHash=new HashMap<>();
        String main_image=document.select("#main > div > article > section.details-container > div.featured-content-container.no-print > img").attr("src");
        if(main_image.isEmpty()){
            main_image="?";
        }
        String category=document.select("#main > div > article > section.details-container > div.details.container > ol > li:nth-child(3) > a").text();
        String subCategory=document.select("#main > div > article > section.details-container > div.details.container > ol > li:nth-child(4) > a").text();
        String numberOfDishes=document.select("#number-of-dishes").attr("data-amount");
        Elements ingredients_el=document.select("#recipe-ingredients > div.recipe-ingredients-container.row");
        for(Element element: ingredients_el.select("div")){
            String amount=element.select("h2").text();
            if (!amount.isEmpty()){
                ingediantsHash.put(amount,amount);
            }
            for (Element element1:element.select("li")){
                String p1=element1.select("#ing_ > div > span > span.ingredient-data > span.name").text();
                System.out.println("------------------"+p1+"----------------------");
                String amount3=element1.select("span.amount").text();
                String amount2=element1.select("span.unit").text();

                String ingrediant_unit_type="";
                ingrediant_unit_type=amount3 + " " + amount2;

                System.out.println("------------------"+ingrediant_unit_type+"----------------------");
                if(ingrediant_unit_type.equals("")){
                    ingrediant_unit_type="?";
                }
                if(p1.equals("")){
                    continue;
                }
                ingediantsHash.put(p1,ingrediant_unit_type);
            }
        }
        System.out.println(ingediantsHash);
        ArrayList<String> preparationTime=new ArrayList<>();
        preparationTime.add(document.select("#main > div > article > section.recipe-overview.no-print > div.overview-lists-container-desktop > ul.overview.row > li:nth-child(2) > div > div.key-value.preparation-time").text());
        preparationTime.add(document.select("#main > div > article > section.recipe-overview.no-print > div.overview-lists-container-desktop > ul.overview.row > li:nth-child(2) > div > div:nth-child(2)").text());
        String difficultyLevel=document.select("#main > div > article > section.recipe-overview.no-print > div.overview-lists-container-desktop > ul.overview.row > li.overview-item.difficulty.col-1 > div > div.value.difficulty_level").text();
        String calories=document.select("#main > div > article > section.recipe-overview.no-print > div.overview-lists-container-desktop > ul.overview.row > li:nth-child(3) > div > div.value.calories_per_dish").text();
//        ArrayList<String> ingredients=new ArrayList<>();
        ArrayList<String> PreparationSteps=new ArrayList<>();
        Elements Steps=document.select("#main > div > article > section.recipe-content.original_content");
        int steper=1;
        for (Element step:Steps.select("li")){
            PreparationSteps.add(String.valueOf(steper));
            String s=step.text();
            PreparationSteps.add(s);
            String img="";
            for (Element figureS:step.select("figure")){
                img=figureS.attr("src");
                try {
                    String decodedURL = URLDecoder.decode(img, StandardCharsets.UTF_8.toString());
//                        System.out.println(decodedURL);
                    img = decodedURL;
//                        DatabaseReference productRef=fireBase.getReference();
//                        DatabaseReference myp=productRef.child("Recipes_DB").child("מתכונים ב10 דקות").child(name);
//                        myp.setValue(name);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    System.out.println("\n--!!---Error to Decode: \n"+ img +"\n---------^----------");
                }
            }
            steper++;
        }
        Recipes recipes=new Recipes(ingediantsHash, title,  main_image,  category,  title,  subCategory,  numberOfDishes,  description, preparationTime, difficultyLevel ,  calories, PreparationSteps);
//        System.out.println(recipes);
        return recipes;

//        ArrayList<String> Preparation=new ArrayList<>();
//        String name= document.select("#recipe-ingredients > div.recipe-ingredients-container.row > div:nth-child(3) > h2").text();
//        String img = document.select("#main > div > article > section.details-container > div.featured-content-container.no-print > img").attr("src");
//        Elements elements=document.select("#main > div > article");
//        Elements recipecontent=elements.select("section.recipe-content.original_content");
//        for(Element element:recipecontent.select("li")){
//            System.out.println(element.text());
//            String prepar=element.text();
//            Preparation.add(prepar);
//        }
//        System.out.println(name);
    }
}
