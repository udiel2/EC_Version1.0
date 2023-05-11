package com.application.easycook;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.ViewHolder {

    private ArrayList<Product> product_list;

    public ArrayList<Product> getChosenProducts() {
        return product_list;
    }

    private ArrayList<ParseProduct> chosenProducts= new ArrayList<>();


    public PantryAdapter(@NonNull View itemView) {
        super(itemView);
    }
}
