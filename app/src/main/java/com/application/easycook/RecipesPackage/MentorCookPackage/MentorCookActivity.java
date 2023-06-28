package com.application.easycook.RecipesPackage.MentorCookPackage;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.Product;
import com.application.easycook.RecipesPackage.RecipeProductsAdapter;
import com.application.easycook.RecipesPackage.Recipes;
import com.application.easycook.databinding.ActivityMentorCookBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MentorCookActivity extends AppCompatActivity {
    ActivityMentorCookBinding binding;
    private ArrayList<Product> products;
    private ArrayList<String> name_tags;
    private ArrayList<String> amounts;
    private ArrayList<String> units;
    private ArrayList<String> ingrediatGrop;
    private  Recipes recipe ;

    private LinearLayout linearLayout;

    private MyPantry myPantry;

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPantry=new MyPantry(this);
        Recipes recipes = null;
        binding = ActivityMentorCookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // בדיקה האם קיבלת נתון מה-Intent
        if (getIntent().hasExtra("מקור_הנתון")) {
            // קבלת הנתון מה-Intent
            String data = getIntent().getStringExtra("מקור_הנתון");
            recipe=myPantry.getRecipeByid(data);
            makeproductList();
            System.out.println(recipes);
            // השתמש בנתון לפי הצורך
            // לדוגמה, הדפסה בלוג
            Log.d("MentorCookActivity", "הנתון שהתקבל: " + data);
        }




        LinearLayout linearLayout1=binding.mentorPanelLinarIngridi;
        LinearLayout linearLayout=binding.mentorPanelLinarsteps;
        ConstraintLayout constraintLayout1 = new ConstraintLayout(this);
        RecyclerView recyclerView = new RecyclerView(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        constraintLayout1.addView(recyclerView);
        for (String step : recipe.getPreparationSteps()) {
            ConstraintLayout constraintLayout = new ConstraintLayout(this);
            TextView stepTextView = new TextView(this);
            stepTextView.setText(step);
            constraintLayout.addView(stepTextView);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            linearLayout.addView(constraintLayout, layoutParams);
        }

        RecipeProductsAdapter adapter=new RecipeProductsAdapter(myPantry,products,amounts,ingrediatGrop,units);
        recyclerView.setAdapter(adapter);
        linearLayout1.addView(constraintLayout1);

        adapter.notifyDataSetChanged();
        TextView title=binding.mentorPanelName;
        TextView dish=binding.mentorPanelDishs;
        dish.setText(recipe.getNumberOfDishes());
        title.setText(recipe.getTitle());
        TextView dicription =binding.mentorPanelDiscription;
        dicription.setText(recipe.getDescription());
        setContentView(binding.getRoot());

    }

    public void makeproductList(){
        products=new ArrayList<>();
        name_tags=new ArrayList<>();
        amounts=new ArrayList<>();
        units=new ArrayList<>();
        ingrediatGrop=new ArrayList<>();
        ArrayList <String> ingrediatGrop_temp=new ArrayList<>();

        HashMap<String,String> hash=recipe.getIngredients();


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
        System.out.println("555555555555555555555");

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
        System.out.println(ingrediatGrop_temp);
        loop:for(String name:name_tags){
            ArrayList<Product> productArrayList=myPantry.getProductsByName(name);
            for(Product p :productArrayList) {
                if(p.getName().contains(name)){
                    products.add(p);
                    continue loop;
                }
            }
            Product product=new Product(name);
            products.add(product);
        }
        System.out.println("5555555555555555555555555"+name_tags);

    }

}