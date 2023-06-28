package com.application.easycook.MyPantryPackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.Product;
import com.application.easycook.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;


public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {
    ArrayList<PantryProduct> pantryProducts;
//    ArrayList<Product> products;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    LocalDateTime localDateTime;
    Context context;
    private PantryAdapter.OnItemClickedListener onItemClickedListener;

    public PantryAdapter(ArrayList<PantryProduct> pantryProducts, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime=LocalDateTime.now();
        }
        this.pantryProducts = pantryProducts;
//        this.products=products;
        this.context=context;
    }
    public PantryAdapter(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantry_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product=pantryProducts.get(position).getProduct();
        PantryProduct pantryProduct=pantryProducts.get(position);
        if(pantryProduct.getAmount()==100){
            holder.amount.setText("1");
        }else
            holder.amount.setText(String.valueOf(pantryProduct.getAmount())+"%");
        int color=R.color.Secondary_Color;
        Point lifetime=product.getLife_time();
        Range <Integer>time=new Range<>(lifetime.x,lifetime.y);


        Date date1=pantryProduct.getDate();
        Duration duration;
        Long hours=new Long(0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime2 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            duration = Duration.between(localDateTime2, localDateTime);
            hours = duration.toDays();
        }
        /////////////////////////////////////////////////////// Test +14 ////////////////
        System.out.println(time);
        int days=hours.intValue();
        days=days;
        if(lifetime.x>days || lifetime.y-days>4 ){
            System.out.println(days);
            holder.amount.getBackground().setTint(context.getResources().getColor(R.color.blue_light));
        } else if (lifetime.y < days) {
            holder.amount.getBackground().setTint(context.getResources().getColor(R.color.red_light));
        }else{
            holder.amount.getBackground().setTint(context.getResources().getColor(R.color.light_green));
//            System.out.println("________________blue_________________"+days);


        }






        int res = context.getResources().getIdentifier(product.getImagePath().get(1), "drawable", context.getPackageName());
        System.out.println(res);
//        imageview.setImageResource(res);
        String imagePath = product.getImagePath().get(1);
//        int res = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
        System.out.println(res);

// imageview.setImageResource(res);

//        holder.amount.getBackground().setTint(context.getResources().getColor(R.color.light_green));
        StorageReference imageRef = storage.getReference().child("html_shufersal").child(product.getImagePath().get(0)).child(product.getImagePath().get(1));
        final long HAF_MEGABYTE = 512  * 512;

//        Glide.with(context)
//                .asBitmap()
//                .load(imageRef)
//                .into(holder.imageView);
        imageRef.getBytes(HAF_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        holder.imageView.setImageBitmap(bitmap);
//                        Glide.with(context)
//                                .load(imageRef)
//                                .into(holder.imageView);
                        Glide.with(context)
                                .asBitmap()
                                .load(bitmap)
                                .into(holder.imageView);
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


    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return pantryProducts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView amount;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.panty_item_img);
            amount = itemView.findViewById(R.id.panty_item_amount);
            imageView.setOnClickListener(new View.OnClickListener() {
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

