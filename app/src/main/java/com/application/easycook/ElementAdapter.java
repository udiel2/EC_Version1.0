package com.application.easycook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> {
    private ArrayList<Product> productArrayList;

    private ArrayList<Element> elements;
    private Context context;



    public ElementAdapter(ArrayList<Product> products, Context context) {
        this.productArrayList = products;
        this.context=context;

    }


    @NonNull
    @Override
    public ElementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_item, parent, false);
//        productArrayList=new ArrayList<>();
        return new ElementAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.name.setText(product.getName());
//        holder.imag_src.setText(product.getImagePath());
        holder.category.setText(product.getCategory());
        holder.barcod.setText(product.getBar_cod());
        holder.unit_type.setText(product.getUnit_type());
//        holder.imageView.setImageResource(R.drawable.forgot_img);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("html_shufersal").child(product.getImagePath().get(0)).child(product.getImagePath().get(1));
//        String file_name=product.getImagePath().get(product.getImagePath().size());
        final long ONE_MEGABYTE = 500 * 500;
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bitmap);
//                        StorageReference destinationFileRef = storage.getReference().child("Product_icons").child(product.getName());
//                        destinationFileRef.putBytes(bytes)
//                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        // ההעתקה הצליחה
//                                        // ניתן להוסיף כאן פעולות נוספות או לטפל בקובץ המעותק באופן רצוי
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception exception) {
//                                        // כשההעתקה נכשלה
//                                        // ניתן לטפל בשגיאה כאן
//                                    }
//                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // טעינת התמונה נכשלה, טפל בזה כראוי
                    }
                });
        //////////////////////////////////////////////////////
        // אתחול חיבור ל-Firebase Storage


//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//// קישור לתמונה ב-Firebase Storage
//        String url="/html_shufersal/"+"tavlinim_files/EQR36_L_P_3272976_1.png";
//        StorageReference imageRef = storage.getReference().child("html_shufersal").child("tavlinim_files").child("EQR36_L_P_3272976_1.png");
//        String src=imageRef.toString();
//        System.out.println(imageRef.toString());
//        RequestOptions options = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.NONE) // לא לשמור תמונה במטמון
//                .skipMemoryCache(true); // לא לשמור תמונה בזיכרון המטמון
//        Glide.with(context).using(new FirebaseImageLoader()).load(imageRef).apply(options).into(holder.imageView);
//
//        Glide.with(context)
//                .using(new FirebaseImageLoader())
//                .load(imageRef)
//                .apply(options)
//                .into(holder.imageView);

// הורדת התמונה והצגתה ב-ImageView באמצעות Glide
//        Glide.with(context)
//                .load(src)
//                .apply(options)
//                .into(holder.imageView);
//        Picasso.get().load(parseproduct.getImgUrl()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView category;

        TextView barcod;
        TextView name;
        TextView imag_src;
        TextView unit_type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_pImange);
            name = itemView.findViewById(R.id.View_product_name);
            barcod=itemView.findViewById(R.id.barcode);
            unit_type=itemView.findViewById(R.id.unittype);
            category=itemView.findViewById(R.id.category);
            imag_src=itemView.findViewById(R.id.src_img);
        }
    }


}