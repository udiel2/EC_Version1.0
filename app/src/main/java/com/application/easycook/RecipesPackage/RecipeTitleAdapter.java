package com.application.easycook.RecipesPackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.MyPantryPackage.MyPantry;
import com.application.easycook.R;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RecipeTitleAdapter extends RecyclerView.Adapter<RecipeTitleAdapter.ViewHolder> {
    LocalDateTime localDateTime;
    Context context;
    private MyPantry myPantry;
    private RecipeTitleAdapter.OnItemClickedListener onItemClickedListener;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ArrayList<RecipeTitle> recipeTitles;

    private ArrayList<Recipes> recipes;
    public RecipeTitleAdapter(ArrayList<Recipes> recipes,ArrayList<RecipeTitle> recipeTitles, Context context, MyPantry myPantry) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime= LocalDateTime.now();
        }
        this.recipes=recipes;
        this.myPantry=myPantry;
        this.recipeTitles=recipeTitles;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipetitleitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeTitle recipeTitle =recipeTitles.get(position);
        Recipes recipe=recipes.get(position);

        TextView title=holder.name;
        title.setText(recipeTitle.getTitle());
        TextView totaltime=holder.totaltime;
        TextView diff=holder.diflevel;
        String total=recipe.getPreparationTime().get(1);
        total=total.replace("כולל","");
        totaltime.setText(total);
        String dif=recipe.getDifficultyLevel();
        diff.setText(dif);
        StorageReference imageRef = storage.getReference().child("Foody_recipes").child(recipeTitle.getImagPath().get(1)).child(recipeTitle.getImagPath().get(2));
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

    }

    @Override
    public int getItemCount() {
        return recipeTitles.size();
    }

    public void setOnItemClickedListener(RecipeTitleAdapter.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView diflevel;
        TextView totaltime;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipetitleitam_imageView);
            name = itemView.findViewById(R.id.recipetitleitam_name);
            diflevel= itemView.findViewById(R.id.recipetitleitam_diflevel);
            totaltime=itemView.findViewById(R.id.recipetitleitam_totaltime);
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
