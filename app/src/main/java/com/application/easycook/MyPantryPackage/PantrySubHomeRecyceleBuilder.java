package com.application.easycook.MyPantryPackage;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Range;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.Product;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class PantrySubHomeRecyceleBuilder {

    public HashMap<String, ArrayList<PantryProduct>> hashMap;

    private ArrayList<RecyclerView> recyclerViewArrayList;

    PantrydisplayProductDetails productDetails;
    private PantrydisplayProductDetails.OnButtonClickListener buttonClickListener;
    private LinearLayout linearLayout;
    private ArrayList<String> categoryNames;
    private ArrayList<PantryProduct> expires_soon;
    private LayoutInflater inflater;
    private Context context;
    LocalDateTime localDateTime;
    private PantryAdapter mainAdapter;
    private PantryAdapter mainAdapter2;

    PantrySubHomeRecyceleBuilder(MyPantry myPantry, Context context, LinearLayout linearLayout, LayoutInflater inflater) {
//        linearLayout=new LinearLayout(linearLayout.getContext());
        hashMap = new HashMap<>();
        this.context = context;
        this.inflater = inflater;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.now();
        }
        productDetails = new PantrydisplayProductDetails(context, inflater, myPantry);


        ArrayList<PantryProduct> pantryProducts = myPantry.getPantryList();
        hashMap.put("כל המוצרים", pantryProducts);
        expires_soon=new ArrayList<PantryProduct>();
        for (PantryProduct product : pantryProducts) {
            int duratin = getDuration(product);
            if (duratin != 0) {
                expires_soon.add(product);
            }
            product.getProduct().showProduct();
            String category = product.getCategory();
            if (!hashMap.containsKey(category)) {
                ArrayList<PantryProduct> temp = new ArrayList<>();
                temp.add(product);
                hashMap.put(category, temp);

            } else {
                ArrayList<PantryProduct> p_temp = hashMap.get(category);
                p_temp.add(product);
                hashMap.put(category, p_temp);
            }
        }
        Collections.sort(expires_soon, (a, b) -> getDuration(a) - getDuration(b));
        mainAdapter=new PantryAdapter(pantryProducts,context);

        mainAdapter2=new PantryAdapter(expires_soon,context);
        productDetails.setMain_Adapter(mainAdapter,mainAdapter2);
        linearLayout.addView(buildLayout("פג תוקף בקרוב", expires_soon));
        categoryNames = new ArrayList<>(hashMap.keySet());
        Collections.sort(categoryNames, (a, b) -> hashMap.get(b).size() - hashMap.get(a).size());
        for (String categoryName : categoryNames) {
            // הוסף כאן את הקוד שברצונך לבצע על כל שם הקטגוריה
            System.out.println(categoryName);
            System.out.println(hashMap.get(categoryName).size());
//            if(categoryName.equals("כל המוצרים"))
//                linearLayout.addView(buildLayout(categoryName, hashMap.get(categoryName)));
//            else
//                linearLayout.addView(buildLayout(categoryName, hashMap.get(categoryName)));
           linearLayout.addView(buildLayout(categoryName,hashMap.get(categoryName)));

        }

    }

    public ConstraintLayout buildLayout(String text, ArrayList<PantryProduct> pantryProducts) {

        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        // יצירת TextView
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams textLayoutParams1 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams1.setMargins(0, 8, 16, 20);
//            textLayoutParams1.startToEnd = button.getId();
        textLayoutParams1.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        textLayoutParams1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        textView.setLayoutParams(textLayoutParams1);
        textView.setTextSize(16);
        textView.setPadding(0,10,8,5);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        textView.setGravity(Gravity.START);// המחרוזת מתעדכנת על פי המחרוזת הנוכחית בלולאה


        // יצירת RecyclerView
        RecyclerView recyclerView = new RecyclerView(context);

        // הוספת הטקסט וה-RecyclerView ל-constraintLayout


//        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ConstraintLayout.LayoutParams recycleparam=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        recycleparam.startToEnd=textView.getId();
        recycleparam.setMargins(0, 8, 16, 20);
        recyclerView.setPadding(0,10,8,5);
        recyclerView.setLayoutParams(recycleparam);
        PantryAdapter adapter = new PantryAdapter(pantryProducts, context);
//        adapters.add(adapter);
        recyclerView.setAdapter(adapter);
        constraintLayout.addView(textView);
        constraintLayout.addView(recyclerView);
        adapter.setOnItemClickedListener(new PantryAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int position) {
                Product product = pantryProducts.get(position).getProduct();
                PantryProduct pantryProduct = pantryProducts.get(position);
                System.out.println(pantryProduct.getProduct_id());
                System.out.println(pantryProduct.getId());
                System.out.println("---------Date: " + pantryProduct.getDate());
                productDetails.displayProductDetails(pantryProduct, adapter);
                product.showProduct();

            }
        });


        // כאן תוכל להתאים את ה-constraints ולעצב את ה-layout כרצונך

        return constraintLayout;

    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public int getDuration(PantryProduct product) {
        Point lifetime = product.getProduct().getLife_time();
        Range<Integer> time = new Range<>(lifetime.x, lifetime.y);
        Date date1 = product.getDate();
        Duration duration;
        Long hours = new Long(0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime2 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            duration = Duration.between(localDateTime2, localDateTime);
            hours = duration.toDays();
        }
        System.out.println("------------------------------"+time);
        int days = hours.intValue();
        int duratin_time=0;
        if (time.contains(days)) {
            duratin_time = lifetime.y - days;
            return duratin_time;
        } else if (lifetime.y < days) {
            return duratin_time;
        } else {
            return duratin_time;

        }

    }
    public interface OnButtonClickListener {
        void onButtonClick();
    }
    public void setOnButtonClickListener(PantrydisplayProductDetails.OnButtonClickListener listener) {
        buttonClickListener = listener;
    }

}
