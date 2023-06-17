package com.application.easycook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipesDatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "recipe_main.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "recipes_title";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_IMAGE_PATH = "imagPath";
    private static final String COLUMN_AOUTER = "auther";
    private static final String COLUMN_IMAGE_AOUTH = "imgAuth";
    private FirebaseFirestore firestore;


    public RecipesDatabaseHelper(Context context) {
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
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_LINK + " TEXT, "
                + COLUMN_IMAGE_PATH + " TEXT, "
                + COLUMN_AOUTER + " TEXT, "
                + COLUMN_IMAGE_AOUTH + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
        System.out.println("Table created successfully.");
        syncWithFirestore();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void syncWithFirestore() {
        try {
            firestore.collection("Recipes_V1")
//                        .whereEqualTo("name",query_text)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int counter=0;
                            List<DocumentSnapshot> querySnapshots=queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot documentSnapshot:querySnapshots){
                                RecipeTitle recipeTitle =documentSnapshot.toObject(RecipeTitle.class);
                                addRecipeTitle(recipeTitle,documentSnapshot.getId());
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
    public String addRecipeTitle(RecipeTitle recipeTitle,String id) {
//        System.out.print(product.getName()+" | ");
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            // המוצר כבר קיים בטבלה, נתעלם מהוספתו
            cursor.close();
            db.close();
            return id;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_TITLE, recipeTitle.getTitle());
        values.put(COLUMN_DESCRIPTION, recipeTitle.getDescription());
        values.put(COLUMN_LINK, recipeTitle.getLink());
        values.put(COLUMN_IMAGE_PATH, convertArrayListToJson(recipeTitle.getImagPath()));
        values.put(COLUMN_AOUTER, recipeTitle.getAuther());
        values.put(COLUMN_IMAGE_AOUTH, convertArrayListToJson(recipeTitle.getImgAuth()));

        db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
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
    private String convertArrayListToJson(ArrayList<String> arrayList) {
        JSONArray jsonArray = new JSONArray(arrayList);
        return jsonArray.toString();
    }
    public ArrayList<RecipeTitle> getAllRecipeTitles() {
        ArrayList<RecipeTitle> recipeTitles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String id=cursor.getString(0);
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String link = cursor.getString(3);
                String imagePathJson = cursor.getString(4);
                String auther = cursor.getString(5);
                String imageAuthJson = cursor.getString(6);
                ArrayList<String> imagPath = convertJsonToArrayList(imagePathJson);
                ArrayList<String> imgAuth = convertJsonToArrayList(imageAuthJson);


                RecipeTitle recipeTitle = new RecipeTitle(title,id, description, link, imagPath, auther, imgAuth);
                recipeTitles.add(recipeTitle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeTitles;
    }
    public ArrayList<RecipeTitle> getRecipeTitleByName(String text){
        if( text == null|| text.length()<1){
            return null;
        }
//        System.out.println("ID Faild: "+text);
        ArrayList<RecipeTitle> recipeTitles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " LIKE '%" + text + "%' ORDER BY LENGTH(" + COLUMN_TITLE + ") ASC LIMIT 15";
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE '%" + text + "%' LIMIT 15";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                String id=cursor.getString(0);
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String link = cursor.getString(3);
                String imagePathJson = cursor.getString(4);
                String auther = cursor.getString(5);
                String imageAuthJson = cursor.getString(6);
                ArrayList<String> imagPath = convertJsonToArrayList(imagePathJson);
                ArrayList<String> imgAuth = convertJsonToArrayList(imageAuthJson);
                RecipeTitle recipeTitle = new RecipeTitle(title,id, description, link, imagPath, auther, imgAuth);

                recipeTitles.add(recipeTitle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeTitles;

    }
    public RecipeTitle getRecipeTitleById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            String ids=cursor.getString(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String link = cursor.getString(3);
            String imagePathJson = cursor.getString(4);
            String auther = cursor.getString(5);
            String imageAuthJson = cursor.getString(6);
            ArrayList<String> imagPath = convertJsonToArrayList(imagePathJson);
            ArrayList<String> imgAuth = convertJsonToArrayList(imageAuthJson);
            RecipeTitle recipeTitle = new RecipeTitle(title,ids, description, link, imagPath, auther, imgAuth);
            cursor.close();
            db.close();
            return recipeTitle;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

}
