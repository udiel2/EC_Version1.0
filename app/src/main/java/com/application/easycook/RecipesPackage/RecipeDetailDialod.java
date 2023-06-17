package com.application.easycook.RecipesPackage;

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
import androidx.fragment.app.FragmentManager;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.R;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailDialod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailDialod extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseStorage storage=FirebaseStorage.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecipeTitle recipeTitle;
    private MyPantry myPantry;
    private Recipes recipes;

    public RecipeDetailDialod(RecipeTitle recipeTitle, MyPantry myPantry) {
        this.recipeTitle=recipeTitle;
        this.myPantry=myPantry;
        recipes=myPantry.getRecipeByid(recipeTitle.getId());


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
        TextView dificalty=view.findViewById(R.id.recipetitleitam_diflevel);
        TextView p_time=view.findViewById(R.id.recipetitleitam_totaltime);
        Button back=view.findViewById(R.id.recipedetails_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
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













    }
//    private void replaceFragment(Fragment fragment){
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
//        fragmentTransaction.commit();
//    }
}