package com.application.easycook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.application.easycook.RecipesPackage.Recipes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RecipeDetailsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipe_Detail.db";

    private static final int DATABASE_VERSION = 8;
    private static final String TABLE_NAME = "recipes_detail";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMAGE = "main_image";
    private static final String COLUMN_RECIPE_NAME = "recipeName";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_SUB_CATEGORY = "subCategory";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PREPARATION_TIME = "preparationTime";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_NUMBER_DISHES = "numberOfDishes";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_DIFFICULTY_LEVEL = "difficultyLevel";
    private static final String COLUMN_STEPS = "PreparationSteps";

    private FirebaseFirestore firestore;

    public RecipeDetailsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        firestore = FirebaseFirestore.getInstance();
//        onCreate(getWritableDatabase());
        // בדיקה של קיום מסד הנתונים
        File databaseFile = context.getDatabasePath(DATABASE_NAME);
        if (!databaseFile.exists()){
            onCreate(getWritableDatabase());
        }
        // קריאה לפונקציה onCreate רק אם מסד הנתונים אינו קיים


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " STRING PRIMARY KEY, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_IMAGE + " TEXT, "
                + COLUMN_RECIPE_NAME + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_SUB_CATEGORY + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_PREPARATION_TIME + " TEXT, "
                + COLUMN_CALORIES + " TEXT, "
                + COLUMN_NUMBER_DISHES + " TEXT, "
                + COLUMN_INGREDIENTS + " TEXT, "
                + COLUMN_DIFFICULTY_LEVEL + " TEXT,"
                + COLUMN_STEPS + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
        System.out.println("Table created successfully.");
        syncWithFirestore();

    }


    public String addRecipe(Recipes recipe, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_IMAGE, recipe.getMain_image());
        values.put(COLUMN_RECIPE_NAME, recipe.getRecipeName());
        values.put(COLUMN_CATEGORY, recipe.getCategory());
        values.put(COLUMN_SUB_CATEGORY, recipe.getSubCategory());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_PREPARATION_TIME, convertArrayListToJson(recipe.getPreparationTime()));
        values.put(COLUMN_CALORIES, recipe.getCalories());
        values.put(COLUMN_NUMBER_DISHES, recipe.getNumberOfDishes());
        values.put(COLUMN_INGREDIENTS, convertHashMapToJson(recipe.getIngredients()));
        values.put(COLUMN_DIFFICULTY_LEVEL, recipe.getDifficultyLevel());
        values.put(COLUMN_STEPS,convertArrayListToJson(recipe.getPreparationSteps()));
        db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    public void syncWithFirestore() {
        try {
            firestore.collection("Recipes_all_v2")
//                        .whereEqualTo("name",query_text)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int counter=0;
                            List<DocumentSnapshot> querySnapshots=queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot documentSnapshot:querySnapshots){
                                Recipes recipes =documentSnapshot.toObject(Recipes.class);
                                addRecipe(recipes,documentSnapshot.getId());
//                                    product.showProduct();
                                counter++;
                            }
                            System.out.println("Finish Update From FireStore!\n----- "+counter+" Products upload.");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("------------------------ Error to get firestore----------------------" +
                                    e.getMessage());
                        }
                    });;
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public Recipes getRecipeById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            String recipeId=cursor.getString(0);
            String title = cursor.getString(1);
            String image = cursor.getString(2);
            String recipe_name = cursor.getString(3);
            String category = cursor.getString(4);
            String sub_category = cursor.getString(5);
            String  description= cursor.getString(6);
            ArrayList<String> preparationTime = convertJsonToArrayList(cursor.getString(7));
            String calories = cursor.getString(8);
            String numberOfDishes = cursor.getString(9);
            HashMap<String,String> ingredients = convertJsonToHashMap(cursor.getString(10));
            String difficultyLevel = cursor.getString(11);
            ArrayList<String> steps=convertJsonToArrayList(cursor.getString(12));

            Recipes recipe = new Recipes(recipeId,ingredients, title, image, category, recipe_name, sub_category, numberOfDishes, description, preparationTime,difficultyLevel,calories,steps);
            cursor.close();
            db.close();
            return recipe;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }
    private String convertHashMapToJson(HashMap<String, String> hashMap) {
        JSONObject jsonObject = new JSONObject(hashMap);
        return jsonObject.toString();
    }

    private HashMap<String, String> convertJsonToHashMap(String json) {
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                hashMap.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }
    private String convertArrayListToJson(ArrayList<String> arrayList) {
        JSONArray jsonArray = new JSONArray(arrayList);
        return jsonArray.toString();
    }

    private ArrayList<String> convertJsonToArrayList(String json) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    public ArrayList<Recipes> getRecipesByName(String text){
        if( text == null|| text.length()<1){
            return null;
        }
        ArrayList<Recipes> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_RECIPE_NAME + " LIKE '%" + text + "%' ORDER BY LENGTH(" + COLUMN_RECIPE_NAME + ") ASC LIMIT 15";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                String recipeId=cursor.getString(0);
                String title = cursor.getString(1);
                String image = cursor.getString(2);
                String recipe_name = cursor.getString(3);
                String category = cursor.getString(4);
                String sub_category = cursor.getString(5);
                String  description= cursor.getString(6);
                ArrayList<String> preparationTime = convertJsonToArrayList(cursor.getString(7));
                String calories = cursor.getString(8);
                String numberOfDishes = cursor.getString(9);
                HashMap<String,String> ingredients = convertJsonToHashMap(cursor.getString(10));
                String difficultyLevel = cursor.getString(11);
                ArrayList<String> steps=convertJsonToArrayList(cursor.getString(12));
                Recipes recipe = new Recipes(recipeId,ingredients, title, image, category, recipe_name, sub_category, numberOfDishes, description, preparationTime,difficultyLevel,calories,steps);
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipes;

    }
}
