package com.application.easycook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    private ArrayList<ParseProduct> parseproducts;
    private Context context;

    public ParseAdapter(ArrayList<ParseProduct> parseproducts, Context context) {
        this.parseproducts = parseproducts;
        this.context = context;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantry_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapter.ViewHolder holder, int position) {
        ParseProduct parseproduct= parseproducts.get(position);
        holder.textView.setText(parseproduct.getTitle());
        Picasso.get().load(parseproduct.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return parseproducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_pImange);
            textView = itemView.findViewById(R.id.View_product_name);
        }
    }
}
