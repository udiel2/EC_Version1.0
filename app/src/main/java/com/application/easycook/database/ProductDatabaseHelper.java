package com.application.easycook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import androidx.annotation.NonNull;

import com.application.easycook.Product;
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
import java.util.List;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products_main.db";
    private static final int DATABASE_VERSION = 14;
    private static final String TABLE_NAME = "all_products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE_PATHS = "image_paths";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_UNIT_TYPE = "unit_type";
    private static final String COLUMN_BAR_COD = "bar_cod";
    private static final String COLUMN_LIFE_TIME = "life_time";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_UNIT_WEIGHT = "unit_weight";
    private static final String COLUMN_UNIT_BRAND = "unit_brand";
    private FirebaseFirestore firestore;

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        firestore = FirebaseFirestore.getInstance();
        printExistingDatabases(context);
//        onCreate(getWritableDatabase());
        // בדיקה של קיום מסד הנתונים
        File databaseFile = context.getDatabasePath(DATABASE_NAME);
        if (!databaseFile.exists()){
            onCreate(getWritableDatabase());
        }
            // קריאה לפונקציה onCreate רק אם מסד הנתונים אינו קיים


    }
    private void printExistingDatabases(Context context) {
        String[] databases = context.databaseList();
        if (databases.length > 0) {
//            System.out.println("Existing databases:");
            for (String database : databases) {
//                System.out.println(database);
            }
        } else {
            System.out.println("No existing databases found.");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " STRING PRIMARY KEY, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_IMAGE_PATHS + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_UNIT_TYPE + " TEXT, "
                + COLUMN_BAR_COD + " TEXT, "
                + COLUMN_LIFE_TIME + " TEXT, "
                + COLUMN_PRICE + " TEXT, "
                + COLUMN_UNIT_WEIGHT + " TEXT,"
                + COLUMN_UNIT_BRAND + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
        System.out.println("Table created successfully.");
        syncWithFirestore();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
//        System.out.println("Upgrade Database: "+DATABASE_NAME);
    }
    // בדיקה אם טבלה קיימת
    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'", null);
        boolean tableExists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return tableExists;
    }

    public void syncWithFirestore() {
        try {
            firestore.collection("All_Products_V2")
//                        .whereEqualTo("name",query_text)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int counter=0;
                                List<DocumentSnapshot> querySnapshots=queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot documentSnapshot:querySnapshots){
                                    Product product=documentSnapshot.toObject(Product.class);
                                    addProduct(product,documentSnapshot.getId());
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

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String id=cursor.getString(0);
                String name = cursor.getString(1);
                String imagePathJson = cursor.getString(2);
                ArrayList<String> imagePath = convertJsonToArrayList(imagePathJson);
                String category = cursor.getString(3);
                String unitType = cursor.getString(4);
                String barCod = cursor.getString(5);
                Point lifeTime = convertJsonToPoint(cursor.getString(6));
                String price = cursor.getString(7);
                String unitWeight = cursor.getString(8);
                String branUnit=cursor.getString(9);

                Product product = new Product(id,name, imagePath, barCod, lifeTime, category, unitType, price, unitWeight,branUnit);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
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

    private String convertPointToJson(Point point) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("x", point.x);
            jsonObject.put("y", point.y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private Point convertJsonToPoint(String json) {
        Point point = new Point();
        try {
            JSONObject jsonObject = new JSONObject(json);
            point.x = jsonObject.getInt("x");
            point.y = jsonObject.getInt("y");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return point;
    }
    public String addProduct(Product product,String id) {
//        System.out.print(product.getName()+" | ");
        SQLiteDatabase db = getWritableDatabase();
        // בדיקה אם המוצר כבר קיים בטבלה
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            // המוצר כבר קיים בטבלה, נתעלם מהוספתו
            cursor.close();
            db.close();
            return id;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_IMAGE_PATHS, convertArrayListToJson(product.getImagePath()));
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_UNIT_TYPE, product.getUnit_type());
        values.put(COLUMN_BAR_COD, product.getBar_cod());
        values.put(COLUMN_LIFE_TIME, convertPointToJson(product.getLife_time()));
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_UNIT_WEIGHT, product.getUnit_weight());
        values.put(COLUMN_UNIT_BRAND, product.getUnit_brand());
        db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    public boolean deleteProduct(String id) {
        SQLiteDatabase db = getWritableDatabase();
        int affectedRows = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return affectedRows > 0;
    }
    public ArrayList<Product> getProductByName(String text){
        if( text == null|| text.length()<1){
            return null;
        }
//        System.out.println("ID Faild: "+text);
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE '%" + text + "%' ORDER BY LENGTH(" + COLUMN_NAME + ") ASC LIMIT 15";
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE '%" + text + "%' LIMIT 15";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                String id=cursor.getString(0);
                String name = cursor.getString(1);
                String imagePathJson = cursor.getString(2);
                ArrayList<String> imagePath = convertJsonToArrayList(imagePathJson);
                String category = cursor.getString(3);
                String unitType = cursor.getString(4);
                String barCod = cursor.getString(5);
                Point lifeTime = convertJsonToPoint(cursor.getString(6));
                String price = cursor.getString(7);
                String unitWeight = cursor.getString(8);
                String unit_brand = cursor.getString(9);
                Product product = new Product(id,name, imagePath, barCod, lifeTime, category, unitType, price, unitWeight,unit_brand);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;

    }
    public Product getProductById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            String productId=cursor.getString(0);
            String name = cursor.getString(1);
            String imagePathJson = cursor.getString(2);
            ArrayList<String> imagePath = convertJsonToArrayList(imagePathJson);
            String category = cursor.getString(3);
            String unitType = cursor.getString(4);
            String barCod = cursor.getString(5);
            Point lifeTime = convertJsonToPoint(cursor.getString(6));
            String price = cursor.getString(7);
            String unitWeight = cursor.getString(8);
            String unit_brand = cursor.getString(9);

            Product product = new Product(productId, name, imagePath, barCod, lifeTime, category, unitType, price, unitWeight, unit_brand);
            cursor.close();
            db.close();
            return product;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }
}