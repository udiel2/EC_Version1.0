package com.application.easycook.MyPantryPackage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.easycook.Product;
import com.application.easycook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class PantrydisplayProductDetails{
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    LocalDateTime localDateTime ;
    private Context context;
    LayoutInflater inflater;
    MyPantry myPantry;

    PantryAdapter mainAdapter;
    PantryAdapter mainAdapter2;
    private OnButtonClickListener buttonClickListener;
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        buttonClickListener = listener;
    }




    public PantrydisplayProductDetails(Context context , LayoutInflater inflater,MyPantry myPantry) {
        this.context = context;
        this.inflater=inflater;
        this.myPantry=myPantry;
        PantryAdapter.ViewHolder[] viewHolders = new PantryAdapter.ViewHolder[2]; // n הוא מספר המחלקות שברצונך ליצור

    }
    public void setMain_Adapter(PantryAdapter mainAdapter,PantryAdapter mainAdapter2){
        this.mainAdapter=mainAdapter;
        this.mainAdapter2=mainAdapter2;
    }

    public void displayProductDetails(PantryProduct product,PantryAdapter adapter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime=LocalDateTime.now();
        }
        if(product.getCategory().equals("כל המוצרים")){

        }
        Product p=product.getProduct();
        System.out.println(p);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = inflater.inflate(R.layout.pantry_item_deteils_dialog, null);
        builder.setView(dialogView);
        Button add_amount=dialogView.findViewById(R.id.pantrydialogplus);
        Button minus_amount=dialogView.findViewById(R.id.pantrydialogminus);
        Button remove=dialogView.findViewById(R.id.pantrydialogremove);
        TextView name = dialogView.findViewById(R.id.pantry_dialog_name);
        TextView timetype=dialogView.findViewById(R.id.pantrydialogtimetype);
        TextView category = dialogView.findViewById(R.id.pantrydialogcategory);
        TextView date = dialogView.findViewById(R.id.pantrydialogdate);
        ImageView img = dialogView.findViewById(R.id.pantrydialogimg);
        TextView amount=dialogView.findViewById(R.id.pantrydialogamount);
        TextView amout_type=dialogView.findViewById(R.id.pantrydialogamounttype);
        amout_type.setText(p.getUnit_weight());
        if(product.getAmount()==100){
            amount.setText("1");
        }else
            amount.setText(String.valueOf(product.getAmount())+"%");
        // וכן הלאה עבור כל התצוגה שברצונך לעדכן
        name.setText(product.getProduct().getName());
        category.setText(product.getProduct().getCategory());
        //////////////////////////date//////////////////
        Date date1=product.getDate();
        System.out.println("Date1: "+date1);
        Duration duration;
        Long hours=new Long(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime2 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            duration = Duration.between(localDateTime2, localDateTime);
            hours = duration.toMinutes();
        }
        int time=hours.intValue();
        String time_type="דקות";
        if(time>60){
            time=time/60;
            time_type="שעות";
            if(time>24){
                time=time/24;
                time_type="ימים";
                if(time>7){
                    time=time/7;
                    time_type="שבועות";
                }
            }
        }

        timetype.setText(time_type);
        date.setText(String.valueOf(time));
        TextView status = dialogView.findViewById(R.id.pantrydialogstatus);
        int days=hours.intValue()/60;
        Point lifetime=p.getLife_time();
        System.out.println("------------------------"+days);
        if( lifetime.x>days || lifetime.y-days>4 ){
            status.setText("בתוקף");
            status.getBackground().setTint(context.getResources().getColor(R.color.blue_light));
        } else if (lifetime.y < days) {
            status.setText("פג התוקף");
            status.getBackground().setTint(context.getResources().getColor(R.color.red_light));
        }else{
            status.setText("פג תוקף בקרוב");
            status.getBackground().setTint(context.getResources().getColor(R.color.light_yellow));

        }
        StorageReference imageRef = storage.getReference().child("html_shufersal").child(product.getProduct().getImagePath().get(0)).child(product.getProduct().getImagePath().get(1));
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
//        productNameTextView.setText(product.getName());
//        productImageView.setImageResource(product.getImageResourceId());
        // עדכון תצוגה נוספת לפי הצורך


        AlertDialog dialog = builder.create();
        dialog.show();
        add_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount1=product.getAmount();
                if(amount1==100) {
                    return;
                }else {
                    amount1 += 10;
                    System.out.println(product.getAmount_type());
                    product.setAmount(amount1);
                    amount.setText(String.valueOf(product.getAmount()) + "%");
//                    dialog.show();
                }
            }
        });
        minus_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount1=product.getAmount();
                if(amount1==0) {
                    return;
                }else
                    amount1-=10;
                product.setAmount(amount1);
                amount.setText(String.valueOf(product.getAmount())+"%");
//                    dialog.show();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPantry.removePantryProduct(product.getId())){
                    System.out.println("\nProduct: "+product.getProduct().getName()+" Delete From Pantry");
                }
                else
                    System.out.println("Error In Delete");

                dialog.cancel();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                adapter.notifyDataSetChanged();

            }
        });
    }
    public interface OnButtonClickListener {
        void onButtonClick();
    }


}
