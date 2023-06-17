package com.application.easycook.RecipesPackage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.MyPantryPackage.PantryProduct;
import com.application.easycook.R;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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
    private  RecipeSubfragment recipeSubfragment;
    MyPantry myPantry;

    public RecipeFragment(MyPantry myPantry) {
        recipes_names=new ArrayList<>();
        this.myPantry=myPantry;
        storage=FirebaseStorage.getInstance();
        this.recipeTitleArrayList=myPantry.getRecipeTitleList();
        ArrayList<RecipeTitle> recipeTitles=myPantry.getRecipeTitleList();
        ArrayList<RecipeTitle> recipeTitles1=new ArrayList<>();
        ArrayList<Recipes> recipes=new ArrayList<>();
        for(int i=0;i<20;i++){
            recipeTitles1.add(recipeTitles.get(i));
            recipes.add(myPantry.getRecipeByid(recipeTitles.get(i).getId()));

        }
        recipeSubfragment=new RecipeSubfragment(myPantry,recipeTitles1,recipes);



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
        Button gg=rootView.findViewById(R.id.button5);
        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookNow();
            }
        });
//        gg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    firestore.collection("Recipes_all")
//                            .get()
//                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    int counter = 0;
//                                    List<DocumentSnapshot> querySnapshots = queryDocumentSnapshots.getDocuments();
//                                    for (DocumentSnapshot documentSnapshot : querySnapshots) {
//                                        documentSnapshot.getData();
//                                        Recipes recipe = documentSnapshot.toObject(Recipes.class);
//                                        Map<String, Object> recip = new HashMap<>();
//                                        recip.put("name", recipe.getRecipeName());
//                                        recip.put("diff", recipe.getDifficultyLevel());
//                                        recip.put("country", recipe.getTitle());
//                                        System.out.println(recipe);
//                                        firestore.collection("Recipes_Main").document("Categorys").collection(recipe.getCategory()).document(recipe.getId()).set(recip);
////                                        System.out.println(documentSnapshot.get("link"));
////                                        RecipeFragment.FetchRecipeTask task = new FetchRecipeTask(new Callback() {
////                                            @Override
////                                            public void onDocumentReady(Document document) {
////                                                Recipes recipes = paersRecipes(document);
////                                                String basename=recipeTitle.getLink();
////                                                String[] splitStrings = basename.split("/");
////                                                basename=splitStrings[splitStrings.length-1];
////                                                basename=basename.replace("-"," ");
////                                                recipes.setRecipeName(basename);
////                                                recipes.setId(documentSnapshot.getId());
////                                                firestore.collection("Recipes_all").document(documentSnapshot.getId()).set(recipes);
////                                            }
////
////                                            @Override
////                                            public void onError(Exception e) {
////
////                                            }
////                                        });
////                                        task.execute(recipeTitle.getLink()); // הפעלת ה-AsyncTask עם ה-URL בתור הפרמטר
//                                        counter++;
//                                    }
//                                    System.out.println("Finish Update From FireStore!\n----- " + counter + " Products upload.");
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    System.out.println("------------------------ Error to get firestore----------------------" +
//                                            e.getMessage());
//                                }
//                            });
//                } catch (Exception exception) {
//                    System.out.println(exception.getMessage());
//                }
//            }
//        });


        replaceSubFragment(recipeSubfragment);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference productRef=fireBase.getReference();


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView searchView=view.findViewById(R.id.search_view_recipe_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<RecipeTitle> titles= myPantry.getRecipeTitlesByName(query);
                ArrayList<RecipeTitle> recipeTitles1=new ArrayList<>();
                ArrayList<Recipes> recipes=new ArrayList<>();
                for(RecipeTitle recipeTitle:titles){
                    recipeTitles1.add(recipeTitle);
                    recipes.add(myPantry.getRecipeByid(recipeTitle.getId()));
                    System.out.println(recipeTitle);

                }
                recipeSubfragment.updateRecycle(recipes,recipeTitles1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


//        Button button7=view.findViewById(R.id.button5);

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
        Recipes recipes=new Recipes(null,ingediantsHash, title,  main_image,  category,  title,  subCategory,  numberOfDishes,  description, preparationTime, difficultyLevel ,  calories, PreparationSteps);
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
    private void CookNow(){
        ArrayList<RecipeTitle> canCookNow=new ArrayList<>();
        ArrayList<String> name_tags=new ArrayList<>();
        for(PantryProduct product:myPantry.getPantryList()){
            String name=product.getProduct().getName();
            String[] split=name.split(" ");
            for(int i = 0; i<split.length;i++){
                name_tags.add(split[i]);
            }
        }
        for(RecipeTitle recipeTitle:myPantry.getRecipeTitleList()){
            Recipes recipe=myPantry.getRecipeByid(recipeTitle.getId());
//            System.out.println(recipe);
            if(recipe==null){
                continue;
            }
            for(String ingeridian: recipe.getIngredients().keySet()){
                for(String name:name_tags){
                    if(ingeridian.contains(name)){
                        canCookNow.add(recipeTitle);
                    }
                }
            }
        }
        for(RecipeTitle recipeTitle:canCookNow){
            System.out.println(recipeTitle.getTitle());
        }
    }

}
