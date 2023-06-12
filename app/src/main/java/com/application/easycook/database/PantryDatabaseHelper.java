package com.application.easycook.database;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.application.easycook.MyPantryPackage.PantryProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PantryDatabaseHelper  extends SQLiteOpenHelper {

    private FirebaseDatabase fireBase=FirebaseDatabase.getInstance("https://ezcook-9b2ab-default-rtdb.europe-west1.firebasedatabase.app");
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String DATABASE_NAME = "mypantry.db";
    private static final int DATABASE_VERSION = 16;
    private static final String TABLE_NAME = "my_pantry";
    private static final String COLUMN_ID = "id";
    private static final String PRODUCT_ID=  "product_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_ENTRY_TIME = "life_time";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_UNIT_WEIGHT = "unit_weight";



    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mAuth = FirebaseAuth.getInstance();
        String email = "udi_test@test.gr";
        String password = "123123";
        printExistingDatabases(context);
        // בדיקה של קיום מסד הנתונים
        File databaseFile = context.getDatabasePath(DATABASE_NAME);
        if (!isTableExists(getWritableDatabase()))
            // קריאה לפונקציה onCreate רק אם מסד הנתונים אינו קיים
            onCreate(getWritableDatabase());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                mAuth.signInWithEmailAndPassword(email, password);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    System.out.println("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    System.out.println("Successfully signed out.");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.signInWithEmailAndPassword(email, password);

    }

    private void printExistingDatabases(Context context) {
        String[] databases = context.databaseList();
        if (databases.length > 0) {
            System.out.println("Existing databases:");
            for (String database : databases) {
                System.out.println(database);
            }
        } else {
            System.out.println("No existing databases found.");
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // בדיקה אם הטבלה כבר קיימת
        if (!isTableExists(db)) {
            // יצירת הטבלה רק אם היא לא קיימת
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " STRING PRIMARY KEY, "
                    + PRODUCT_ID + " TEXT, "
                    + COLUMN_CATEGORY + " TEXT, "
                    + COLUMN_ENTRY_TIME + " TEXT, "
                    + COLUMN_AMOUNT + " TEXT, "
                    + COLUMN_UNIT_WEIGHT + " TEXT"
                    + ")";
            db.execSQL(createTableQuery);
            System.out.println("Table created successfully.");
            importFromFirebase();
        } else {
            System.out.println("Table already exists.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // בדיקה אם טבלה קיימת
    private boolean isTableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + TABLE_NAME + "'", null);
        boolean tableExists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return tableExists;
    }


    public String syncWithFirestore(FirebaseUser user, PantryProduct product) {
        String userId = user.getUid();
        // הוספת המוצר ל-Bucket המתאים ב-Firebase Database
        DatabaseReference productRef=fireBase.getReference();
        DatabaseReference myp=productRef.child("Pantry_db").child(userId).child("my_pantry").push();
        myp.setValue(product);
        String id= String.valueOf(myp.getKey());
        // קוד ההוספה ל-Bucket המקומי
        System.out.println("*******************ID: "+id+" *****************");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(PRODUCT_ID, product.getProduct_id());
        values.put(COLUMN_CATEGORY, product.getCategory());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(product.getDate());
        System.out.println("Formatted Date: " + formattedDate);
        values.put(COLUMN_AMOUNT,product.getAmount());
        values.put(COLUMN_ENTRY_TIME, formattedDate);
        values.put(COLUMN_UNIT_WEIGHT, product.getAmount_type());
        db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public String addProduct(PantryProduct product) {
        FirebaseUser user = mAuth.getCurrentUser();
        System.out.print(product.getProduct_id()+" | ");
        String id = syncWithFirestore(user, product);
        return id;
    }
    public ArrayList<PantryProduct> getPantry(){
        ArrayList<PantryProduct> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String id=cursor.getString(0);
                String product_id=cursor.getString(1);
                String category = cursor.getString(2);
                String dateString = cursor.getString(3); // התאריך המוחזר מהטבלה כטקסט
                Date entryTime = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    entryTime = dateFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int amount=Integer.parseInt(cursor.getString(4));
                String unitWhight = cursor.getString(5);
                PantryProduct pantryProduct = new PantryProduct(id,product_id,category,unitWhight,amount,entryTime);
                products.add(pantryProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public boolean deleteProduct(String id) {
        FirebaseUser user = mAuth.getCurrentUser();
        SQLiteDatabase db = getWritableDatabase();
        DatabaseReference productRef=fireBase.getReference();
        DatabaseReference myp=productRef.child("Pantry_db").child(user.getUid()).child("my_pantry").child(id);
        myp.removeValue();
        int affectedRows = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return affectedRows > 0;
    }
    public void updatePantryProduct(PantryProduct product) {
        FirebaseUser user = mAuth.getCurrentUser();
        updateProductInFirestore(user,product);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, product.getProduct_id());
        values.put(COLUMN_CATEGORY, product.getCategory());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(product.getDate());
        values.put(COLUMN_ENTRY_TIME, formattedDate);
        values.put(COLUMN_AMOUNT,product.getAmount());
        values.put(COLUMN_UNIT_WEIGHT, product.getAmount_type());

        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {product.getId()};

        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }
    public void updateProductInFirestore(FirebaseUser user, PantryProduct product) {
        String userId = user.getUid();
        DatabaseReference productRef = fireBase.getReference().child("Pantry_db").child(userId).child("my_pantry").child(product.getId());
        productRef.setValue(product);
    }

    public void importFromFirebase() {
        DatabaseReference pantryRef = fireBase.getReference().child("Pantry_db").child(mAuth.getCurrentUser().getUid()).child("my_pantry");
        pantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SQLiteDatabase db = getWritableDatabase();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    PantryProduct product = productSnapshot.getValue(PantryProduct.class);
                    String productId = productSnapshot.getKey();


                    // בדיקה האם המוצר כבר קיים ב-mypantry.db
                    String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
                    Cursor cursor = db.rawQuery(selectQuery, new String[]{productId});

                    if (!cursor.moveToFirst()) {
                        // המוצר לא קיים ב-mypantry.db, לכן יש לייבאו
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_ID, productId);
                        values.put(PRODUCT_ID, product.getProduct_id());
                        values.put(COLUMN_CATEGORY, product.getCategory());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = sdf.format(product.getDate());
                        values.put(COLUMN_ENTRY_TIME, formattedDate);
                        values.put(COLUMN_AMOUNT, product.getAmount());
                        values.put(COLUMN_UNIT_WEIGHT, product.getAmount_type());

                        db.insert(TABLE_NAME, null, values);
                        Log.d(TAG, "Imported product: " + productId);
                    } else {
                        Log.d(TAG, "Product already exists: " + productId);
                    }

                    cursor.close();
                }

                db.close();
                Log.d(TAG, "Import completed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to import from Firebase: " + error.getMessage());
            }
        });
    }

}
