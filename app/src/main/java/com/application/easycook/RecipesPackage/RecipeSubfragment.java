package com.application.easycook.RecipesPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.R;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;

import java.util.ArrayList;

public class RecipeSubfragment extends Fragment {

    private LinearLayout linearLayout;
    private MyPantry myPantry;
    private ArrayList<Recipes> recipes;
    private RecipeTitleAdapter adapter;
    private ArrayList<RecipeTitle> recipeTitles;


    public RecipeSubfragment(MyPantry pantry, ArrayList<RecipeTitle> recipeTitles, ArrayList<Recipes> recipes) {
        this.myPantry = pantry;
        this.recipeTitles = recipeTitles;
        this.recipes = recipes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_home_fragment, container, false);
        linearLayout = rootView.findViewById(R.id.recipehomelinearLayout);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeTitleAdapter( recipeTitles, getContext(), myPantry);
        recyclerView.setAdapter(adapter);
        ConstraintLayout constraintLayout = new ConstraintLayout(requireContext());
        constraintLayout.addView(recyclerView);
        linearLayout.addView(constraintLayout);
        makeContent();
        adapter.setOnItemClickedListener(new RecipeTitleAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int position) {
                replaceFragment(new RecipeDetailDialod(recipeTitles.get(position),myPantry));

            }
        });







    }

    public void updateRecycle(ArrayList<Recipes> recipes, ArrayList<RecipeTitle> recipeTitles) {
        this.recipes.clear();
        this.recipeTitles.clear();
        this.recipes.addAll(recipes);
        this.recipeTitles.addAll(recipeTitles);
        adapter.notifyDataSetChanged();
    }

    private void makeContent() {
        // כאן אתה יכול להוסיף תוכן נוסף לפרגמנט
    }
    private void replaceFragment2(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();



    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager(); // השתמש ב-getChildFragmentManager() כאשר אתה עובד בתוך פרגמנט
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout, fragment);
        fragmentTransaction.addToBackStack("fragmentA");
        fragmentTransaction.commit();
    }
    public void upDateRecipes(ArrayList<RecipeTitle> recipeTitles, ArrayList<Recipes> recipes){
        this.recipes.clear();
        this.recipeTitles.clear();
        this.recipes.addAll(recipes);
        this.recipeTitles.addAll(recipeTitles);
        adapter.notifyDataSetChanged();
    }

    public void addItems() {
        adapter.index+=4;
        adapter.notifyDataSetChanged();
    }
}
