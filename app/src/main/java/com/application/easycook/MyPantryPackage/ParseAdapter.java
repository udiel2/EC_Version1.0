package com.application.easycook.MyPantryPackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.Product;
import com.application.easycook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    private ArrayList<Product> products;

    private ArrayList<Product> selectedProducts = new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance();


    private Context context;

    public ParseAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;

    }
    public ArrayList<Product> getSelectedProducts() {
        return selectedProducts;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantry_item_layout,parent,false);

        return new ViewHolder(view);
    }
    public void toggleSelection(int position) {
        Product product = products.get(position);
//        for(Product product1:selectedProducts){
//            if(product1.getId().equals(product.getId())){
//
//            }
//        }
        if (selectedProducts.contains(product)) {
            selectedProducts.remove(product);
        } else {
            selectedProducts.add(product);
        }
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapter.ViewHolder holder, int position) {
        Product product=products.get(position);
        holder.textView.setText(product.getName());
        CheckBox c=holder.checkBox;
        Button chek=holder.chek;
        StorageReference imageRef = storage.getReference().child("html_shufersal").child(product.getImagePath().get(0)).child(product.getImagePath().get(1));
        final long HAF_MEGABYTE = 512 * 512;
        imageRef.getBytes(HAF_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bitmap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println("טענית התמונה נכשלה");
                        // טעינת התמונה נכשלה, טפל בזה כראוי
                    }
                });
        chek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (onItemClickedListener != null) {
//                    onItemClickedListener.onItemClick(position);
//                }
                if(!holder.is_chek){
                    holder.is_chek=true;
                    chek.setForeground(context.getResources().getDrawable(android.R.drawable.presence_online));
                }
                else {
                    holder.is_chek=false;
                    chek.setForeground(context.getResources().getDrawable(android.R.drawable.presence_invisible));
                }

            }
        });
//        if(selectedProducts.contains(products.get(position))){
//            c.setChecked(true);
//        }



    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.onItemClickedListener = listener;
    }


    public interface OnItemClickedListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Boolean is_chek;
        ImageView imageView;
        TextView textView;
        String s_red_button = "@mipmap/red_foreground";
        String s_green_button = "@mipmap/green_foreground";


        CheckBox checkBox;
        Button chek;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            is_chek=false;
            imageView = itemView.findViewById(R.id.imageView_pImange);
            textView = itemView.findViewById(R.id.View_product_name);
            checkBox = itemView.findViewById(R.id.checkBox);

            chek=itemView.findViewById(R.id.chek);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickedListener != null) {
                        onItemClickedListener.onItemClick(getAdapterPosition());
                    }
                }
            });
            chek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickedListener != null) {
                        onItemClickedListener.onItemClick(getAdapterPosition());
                    }

                }
            });


        }
    }
}
