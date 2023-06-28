package com.application.easycook.RecipesPackage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.Product;
import com.application.easycook.R;
import com.application.easycook.RecipesPackage.MentorCookPackage.MentorCookActivity;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class RecipeDetailDialod extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseStorage storage=FirebaseStorage.getInstance();
    private RecipeProductsAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecipeTitle recipeTitle;
    private MyPantry myPantry;
    private Recipes recipes;
    private ArrayList<Product> products;
    private ArrayList<String> name_tags;
    private ArrayList<String> amounts;
    private ArrayList<String> units;
    private ArrayList<String> ingrediatGrop;


    public RecipeDetailDialod(RecipeTitle recipeTitle, MyPantry myPantry) {
        this.recipeTitle=recipeTitle;
        this.myPantry=myPantry;
        recipes=myPantry.getRecipeByid(recipeTitle.getId());
        makeproductList();


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail_dialod, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title=view.findViewById(R.id.recipedetails_title);
        TextView category= view.findViewById(R.id.recipedetails_category);
        TextView caloris=view.findViewById(R.id.recipedetails_caloris);
        TextView dificalty=view.findViewById(R.id.recipedetails_diffecaltlevel);
        TextView p_time=view.findViewById(R.id.recipedetails_totaltima);
        Button back=view.findViewById(R.id.recipedetails_back);
        RecyclerView recyclerView=view.findViewById(R.id.productlistRecipe_recycleview);
        String cat1=recipes.getCategory();
        if(!recipes.getSubCategory().isEmpty()){
            cat1=cat1 + " > " + recipes.getSubCategory();
        }
        category.setText(cat1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RecipeFragment(myPantry));
            }
        });
        String ptime1=recipes.getPreparationTime().get(0);
        String ptime3=recipes.getPreparationTime().get(1);
        ptime1=ptime1+" "+ptime3;
        p_time.setText(ptime1);
        caloris.setText(recipes.getCalories());
        dificalty.setText(recipes.getDifficultyLevel());
        ImageView img=view.findViewById(R.id.recipedetails_imageView);
        System.out.println(recipes);
        Picasso.get().load(recipes.getMain_image()).into(img);
        title.setText(recipes.getTitle());
        StorageReference imageRef = storage.getReference().child("Foody_recipes").child(recipeTitle.getImagPath().get(1)).child(recipeTitle.getImagPath().get(2));
        final long HAF_MEGABYTE = 512 * 512;
        imageRef.getBytes(HAF_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(bitmap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println("טענית התמונה נכשלה");
                        // טעינת התמונה נכשלה, טפל בזה כראוי
                    }
                });
        //recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter=new RecipeProductsAdapter(myPantry,products,amounts,ingrediatGrop,units);
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickedListener(new ParseAdapter.OnItemClickedListener() {
//            @Override
//            public void onItemClick(int position) {
//                adapter.toggleSelection(position);
//                selectedProducts.clear();
//                ArrayList<Product> p=adapter.getSelectedProducts();
//                if(!p.isEmpty())
//                    for(Product product: p){
//                        selectedProducts.add(product);
//                    }
//            }
//        });
        Button mentor=view.findViewById(R.id.recipedetails_mentor);
        mentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MentorCookActivity.class);
                // הוספת נתונים ל-Intent
                String data = recipes.getId();
                intent.putExtra("מקור_הנתון", data);
                intent.putExtra("amount",amounts);
                intent.putExtra("tags",name_tags);
                intent.putExtra("units",units);
                intent.putExtra("grop",ingrediatGrop);
                // הפעלת ה-Intent על מנת לשלוח את הנתונים
                startActivity(intent);
            }
        });


    }

   public void makeproductList(){
        products=new ArrayList<>();
        name_tags=new ArrayList<>();
        amounts=new ArrayList<>();
        units=new ArrayList<>();
        ingrediatGrop=new ArrayList<>();
       ArrayList <String> ingrediatGrop_temp=new ArrayList<>();

       HashMap<String,String> hash=recipes.getIngredients();


        int grop_index=0;
//       ArrayList<String> stract=new ArrayList<>();
       String grop="";
        for(String key: hash.keySet()) {
            if (key.length() == 1) {
                grop_index++;
                grop = hash.get(key);
                ingrediatGrop.add(grop);
            }
//            if(key.contains("unit")){
//                stract.add(key);
//            }
        }

       System.out.println(ingrediatGrop);
        for (String key:hash.keySet()){
            if(key.contains("ingrediant")){
                name_tags.add(hash.get(key));
                String amount_temp=key.replace("_ingrediant_","-amount_");
                String unit_temp=key.replace("_ingrediant_","_unit_");
                amounts.add(hash.get(amount_temp));
                units.add((hash.get(unit_temp)));
                int i=Integer.parseInt(String.valueOf(key.charAt(0)));
                System.out.println(i);
                if(grop_index!=0){
                    int k=i-1;
                    if(k==0)
                        k=1;
                    ingrediatGrop_temp.add(ingrediatGrop.get(k));
                    System.out.println(ingrediatGrop.get(k));
                }


            }
        }
        ingrediatGrop=ingrediatGrop_temp;
//        System.out.println(ingrediatGrop_temp);
        loop:
            for(String name:name_tags){
                  ArrayList<Product> productArrayList=myPantry.getProductsByName(name);
                  System.out.println(productArrayList);
                  for(Product p :productArrayList) {
//                      System.out.println("----------------------------------------------------");
//                      p.showProduct();
//                      System.out.println("----------------------------------------------------");
                      if(p.getName().contains(name)){
                        products.add(p);
                        continue loop;
                        }
                    }
                    Product product=new Product(name);
                    products.add(product);
       }
//        System.out.println("5555555555555555555555555"+name_tags);

   }

        private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
}