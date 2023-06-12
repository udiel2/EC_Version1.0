package com.application.easycook;

import static android.content.ContentValues.TAG;

import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBaseLoader extends AppCompatActivity {
    private WebView webView;
    private Button singin;
    private Button load_html;
    private RecyclerView recyclerView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<Product> productsList = new ArrayList<>();
    private HashMap<String, ArrayList<Product>> hashMap;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ElementAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    public DataBaseLoader() {


    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_loader);
        hashMap = new HashMap<>();
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.list_items_to_data);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ElementAdapter(productsList, this);
        recyclerView.setAdapter(adapter);
        storage = FirebaseStorage.getInstance();
        singin = findViewById(R.id.b_log);
        load_html = findViewById(R.id.updatehtml);
//        webView = findViewById(R.id.wv1);
        mAuth = FirebaseAuth.getInstance();
        String email = "udi_test@test.gr";
        String password = "123123";
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuth.signInWithEmailAndPassword(email, password);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        /////////////////////////////////// Srech in database////////////////////////////////////////////////////
//        load_html.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firestore.collection("Product")
//                        .whereEqualTo("name", "גזר")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        document
//                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//            }
//        });
//        load_html.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firestore.collection("Products").whereArrayContains("name", "גזר").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                            snapshot.getData();
//                            Product product = snapshot.toObject(Product.class);
//                            productsList.add(product);
//                            product.showProduct();
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//
//            }
//        });
//            }
//                firestore.document("/Database/Products")
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//                                    for (String collection : Objects.requireNonNull(documentSnapshot.getData()).keySet()) {
//                                        firestore.collection("Database")
//                                                .document("Products")
//                                                .collection(collection)
//                                                .whereArrayContains("name", partialProductName)
//                                                .get()
//                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                                                            // עיבוד תוצאות החיפוש
//                                                            Product product = documentSnapshot.toObject(Product.class);
//                                                            productsList.add(product);
//                                                            System.out.println("P Add");
//                                                            product.showProduct();
////////                                                            adapter.notifyDataSetChanged();
////////                                                            // השתמש במוצר המתאים
//                                                        }
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
////                                                        // טיפול בשגיאה במידה והחיפוש נכשל
//                                                    }
//                                                });
//                                    }
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // טיפול בשגיאה במידה והגישה למסמך נכשלה
//                            }
//                        });
////
//            }
//        });
//    }
//        singin.setOnClickListener(new View.OnClickListener()
//
//    {
//        @Override
//        public void onClick (View v){
//        System.out.println("Click");
//        for (Product p : productsList
//        ) {
//
//            p.showProduct();
//
//        }
//    }
//    });
        ///////////////////////////////////////////////////////////////////////// יצירת מסד נתונים//////////////////////////////////////////////////////
        load_html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storage.getReference().child("html_shufersal").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference fileRef : listResult.getItems()) {
//                            if (fileRef.getName().endsWith(".html")) {
//                            if (fileRef.getName().endsWith(".html") && !fileRef.getName().contains("bazek-frozen-milk")) {/////bazek-frozen-milk.html -- no need
                            if (fileRef.getName().contains(".html")) {
                                fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String html = new String(bytes);
                                        String f_name = fileRef.getName();
                                        System.out.println("Category Start!!\nName: " + f_name + "\n");
                                        Document document = Jsoup.parse(html);
//                                            System.out.println(document);
                                        MyBackgroundTask backgroundTask = new MyBackgroundTask(new MyBackgroundTask.Callback() {
                                            @Override
                                            public void onProductReady(Product product) {
//                                                product.showProduct();
                                                productsList.add(product);
                                                ///////////////////////////////////////////////////////////////
//                                                System.out.println(product.getCategory());
//                                                System.out.println(product.getUnit_type());
                                                /////////////////////////////////////////////////////////////////<<<UPLOADTOFIRESTORE>>>>>>>>>>>
//                                                firestore.collection("All_Products").add(product);
                                                /////////////////////////////////////////////////////////
//                                                firestore.collection("Database").document("Products").collection(product.getImagePath().get(0)).add(product).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                        Toast.makeText(getBaseContext(), "SucssesTo ptut", Toast.LENGTH_SHORT).show();
//                                                        System.out.println("\nSucsses to Set : " + product.getName());
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//
//                                                    }
//                                                });
                                            }
                                            @Override
                                            public void onAllProductsReady() {
                                                if (productsList.isEmpty()) {
                                                    System.out.println("\nEmpty ArryList Finish!!!\n");
                                                } else {
                                                    String c = productsList.get(0).getCategory();
                                                    System.out.println("Category Finish!!\nName: " + productsList.get(0).getImagePath().get(0) + "\n");
                                                    ArrayList<Product> temp = new ArrayList<>(productsList);
                                                    hashMap.put(productsList.get(0).getImagePath().get(0), temp);
                                                    productsList.clear();
                                                }

                                                // פעולות לאחר סיום תהליך הקבלת המוצרים
                                                // ...
                                            }
                                        });backgroundTask.onPostExecute(document);
                                    }
                                });
                            }
                        }
                        //////////////////////////////////  Finish loop!!!////////////////////////////////


                    }

                    ;
                });
            }

            ;
        });
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("****************************finishAll**************************");
                System.out.println("\n Hash Size: " + hashMap.size());
                int counter=0;
                for (HashMap.Entry<String, ArrayList<Product>> entry : hashMap.entrySet()) {
                    String key = entry.getKey();
                    ArrayList<Product> productList = entry.getValue();
                    // ניתן לבצע כאן פעולות על המפתח (key) או על רשימת המוצרים (productList)
                    // לדוגמה, נדפיס את המפתח ואת רשימת המוצרים:
                    System.out.println("---------\nCategory: " + key);
                    System.out.println("Products: " + productList.size()+"\n--------");
                    counter+=productList.size();
                }
                System.out.println("\n Total Products: " + counter);
                CollectionReference query = firestore.collection("All_Products");
                AggregateQuery countQuery = query.count();
                countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot = task.getResult();
                            Log.d(TAG, "Count: " + snapshot.getCount());
                        } else {
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
            }
        });
    }
        //////////////////////////////////////////////////////////
        //                                                adapter.notifyDataSetChanged();
//                                                DatabaseReference libraryRef = database.getReference().child("library");
//                                                DatabaseReference newProductRef = libraryRef.push();
//                                                newProductRef.setValue(product);
//                                                StorageReference imageRef = storage.getReference().child("html_shufersal").child(product.getImagePath().get(0)).child(product.getImagePath().get(1));
//                                                final long ONE_pic = 200 * 200;
//                                                imageRef.getBytes(ONE_pic)
//                                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                                            @Override
//                                                            public void onSuccess(byte[] bytes) {
//                                                                StorageReference destinationFileRef = storage.getReference().child("Product_icons").child(product.getImagePath().get(0)).child(product.getImagePath().get(1));
//                                                                destinationFileRef.putBytes(bytes)
//                                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                                            @Override
//                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                                                // ההעתקה הצליחה
//                                                                                // ניתן להוסיף כאן פעולות נוספות או לטפל בקובץ המעותק באופן רצוי
//                                                                            }
//                                                                        })
//                                                                        .addOnFailureListener(new OnFailureListener() {
//                                                                            @Override
//                                                                            public void onFailure(@NonNull Exception exception) {
//                                                                                System.out.println("!!!!!!!!!!!!!Faild to upload pic!!!!!!!!!!!\n------->"+product.getName());
//                                                                                // כשההעתקה נכשלה
//                                                                                // ניתן לטפל בשגיאה כאן
//                                                                            }
//                                                                        });
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception exception) {
//                                                                // טעינת התמונה נכשלה, טפל בזה כראוי
//                                                            }
//                                                        });
//                                            }
//
//                                            @Override
//                                            public void onAllProductsReady() {
//                                                if (productsList.isEmpty()) {
//                                                    System.out.println("\nEmpty ArryList Finish!!!\n");
//                                                } else {
//                                                    String c = productsList.get(0).getCategory();
//                                                    System.out.println("Category Finish!!\nName: " + productsList.get(0).getImagePath().get(0) + "\n");
//                                                    ArrayList<Product> temp = new ArrayList<>(productsList);
//                                                    hashMap.put(productsList.get(0).getImagePath().get(0), temp);
//                                                    productsList.clear();
//                                                }
//
//                                                // פעולות לאחר סיום תהליך הקבלת המוצרים
//                                                // ...
//                                            }
//                                        });
//        singin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signInWithEmailAndPassword(email, password);
//            }
//        });
//        load_html.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                storage.getReference();
//                StorageReference fileRef = storage.getReference().child("html_shufersal/tavlinim.html");
//                fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
////                        System.out.println("0-0-0-0-0-0-0-0-0");
//                        String html = new String(bytes);
////                        System.out.println(html);
//                        Document document = Jsoup.parse(html);
//                        System.out.println("0-0-0-0-0-0-0-0-0");
//                        MyBackgroundTask backgroundTask = new MyBackgroundTask(new MyBackgroundTask.Callback() {
//                            @Override
//                            public void onProductReady(Product product) {
//                                product.showProduct();
//                                System.out.print("0");
//                                productsList.add(product);
//                                adapter.notifyDataSetChanged();
//
//                                // השתמש ב-Product שהתקבל כאן
//                                // ...
//                            }
//
//                            @Override
//                            public void onAllProductsReady() {
//
//                                // פעולות לאחר סיום תהליך הקבלת המוצרים
//                                // ...
//                            }
//                        });
//                        backgroundTask.onPostExecute(document);
//
//
//                        // כאן ניתן לבצע פעולות נוספות על המסמך document
//
//                        // קרא ל-callback.onPostExecute() עם ה-document המוכן
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Error downloading file: " + e.getMessage());
//                        // טיפול בשגיאה בהורדת הקובץ
//                        // ...
//                    }
//                });
//            }
//        });
//                                        backgroundTask.onPostExecute(document);
//                                        // כאן ניתן לבצע פעולות נוספות על המסמך document
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.e(TAG, "Error downloading file: " + e.getMessage());
//                                        // טיפול בשגיאה בהורדת הקובץ
//                                        // ...
//                                    }
//                                }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<byte[]> task) {
//                                        System.out.println("****************************finishAll**************************");
//                                        for (HashMap.Entry<String, ArrayList<Product>> entry : hashMap.entrySet()) {
//                                            String key = entry.getKey();
//                                            ArrayList<Product> productList = entry.getValue();
//                                            // ניתן לבצע כאן פעולות על המפתח (key) או על רשימת המוצרים (productList)
//                                            // לדוגמה, נדפיס את המפתח ואת רשימת המוצרים:
//                                            System.out.println("מפתח: " + key);
//                                            System.out.println("מספר מוצרים: " + productList.size());
//                                            System.out.println("Size: " + hashMap.size());
//                                        }
//
//                                    }
//                                });
//                            }
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Error listing files: " + e.getMessage());
//                        // טיפול בשגיאה ברשימת הקבצים
//                        // ...
//                    }
//                });


        public static class MyBackgroundTask extends AsyncTask<Uri, Void, Document> {

            private static final String TAG = "MyBackgroundTask";

            private Callback callback;

            public MyBackgroundTask(Callback callback) {
                this.callback = callback;
            }

            @Override
            protected Document doInBackground(Uri... uris) {
                Uri uri = uris[0];
                try {
                    return Jsoup.connect(uri.toString()).get();
                } catch (IOException e) {
                    Log.e(TAG, "Error downloading HTML: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Document document) {
                if (document != null) {
                    ArrayList<String> cat_names = new ArrayList<>();
                    //////////////////////// product number #searchResults_count_label
                    String p_number = document.select("#searchResults_count_label").text();
                    System.out.println("\n----------->Product Number: " + p_number + " <-----------------\n");
                    System.out.println(document.select("#wrapper").select("div.gridCart.gridFilters").select("section.mainTabSection").select("h1").text());
                    String category = document.select("#wrapper").select("div.gridCart.gridFilters").select("section.mainTabSection").select("h1").text();
                    if (category.length() == 0) {
                        System.out.println("\n**************Empty Value!**************\n" + "\n--------------->Category<--------------\n");
                        return;
                    } else if (category.equals("בשר טרי")) {
                        Boolean dont = false;
//                        System.out.println("\n--------------------------Start-----------------------\n");
//                    Elements data = document.select("#wrapper");
                        Elements data = document.select("ul#mainProductGrid");
//                    System.out.println(data);
                        product_loop:
                        for (Element li : data.select("li[data-food]")) {
                            /////////////////////image selector : #mainProductGrid > li:nth-child(1) > div > a > img
//                        System.out.println("\n\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n"+li+"\n\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
                            String unit_type = li.attr("data-selling-method");
                            ///////// unit type
                            if (!unit_type.equals("BY_WEIGHT")) {
//                            System.out.println("\n**************Empty Value!**************\n");
//                            System.out.println("             Value: unit_type\n                Category: "+category);
                                continue;
                            }
                            String imgsrc2 = li.select("div").select("div-wrapper-top-product").select("a").select("img").attr("src");
                            String imgsrc = li.select("a.imgContainer").select("img").attr("src");
//                        System.out.println(imgsrc);
                            if (imgsrc.length() == 0) {
                                System.out.println("\n**************Empty Value!**************\n" + "\n------------------->ImageSRC is Missed<-----------------\n    " + category + "Category: ");
                                continue;
                            }
                            String[] splitStrings = imgsrc.split("/");
                            ArrayList<String> resultStrings = new ArrayList<>();
                            // הוספת התוצאות ל-ArrayList
                            for (int i = 1; i < splitStrings.length; i++) {
                                resultStrings.add(splitStrings[i]);
                            }
                            if (resultStrings.size() != 2) {
                                if (category.equals("מוצרים לאפיה ובישול")) {
                                    String name = resultStrings.get(resultStrings.size() - 1);
                                    String path = "cooking_product-milk_files";
                                    resultStrings.clear();
                                    resultStrings.add(path);
                                    resultStrings.add(name);
                                } else {
                                    System.out.println("\n**************IMagePath Error Value!**************\n");
                                    System.out.println("\n---------------->No 2: ImageSRC<------------------\n " + category + "     Category: ");
//                                System.out.println();System.out.println("imgesrc:  "+resultStrings);
                                    continue;
                                }
                            }
//                        System.out.println(li.attr("data-selling-method"));

                            String barcod = li.attr("data-product-code");
                            Matcher matcher = Pattern.compile("(\\d+)").matcher(barcod);
                            if (matcher.find()) {
                                barcod = matcher.group(1);
                                //                        System.out.println(matcher.group(1));
                            }
                            ///////////barcod
                            if (barcod.length() == 0) {
                                System.out.println("\n**************Empty Value!**************\n");
                                System.out.println("             Value: Bar-cod\n                Category: " + category);
                                continue;
                            }
                            Elements textContainer = li.select("div.textContainer");
                            String name = textContainer.select("div.text.description").select("span").text();
//                        System.out.println("Name: "+name);
                            /////////name
                            if (name.length() == 0) {
                                System.out.println("\n**************Empty Value!**************\n");
                                System.out.println("             Value: Name\n                Category: " + category);
                                continue;
                            }
                            String[] nameStrings = name.split(" ");
                            String edit_name = "";
                            // הוספת התוצאות ל-ArrayList
                            for (int i = 0; i < nameStrings.length; i++) {
                                if (nameStrings[i].contains("כשר") || nameStrings[i].contains("טרי") || nameStrings[i].contains("עצמי") || nameStrings[i].contains("מוכשר") || nameStrings[i].contains("יבוא") || nameStrings[i].contains("/"))
                                    continue;
                                edit_name = edit_name + " " + nameStrings[i];
                            }
                            for (String n : cat_names) {
                                if (edit_name.equals(n)) {
                                    continue product_loop;
                                }
                            }
                            if (edit_name.length() == 0)
                                continue product_loop;
                            cat_names.add(edit_name);
                            //                    System.out.println(li.select("div").select("div.textContainer").select("div").select("div.line").select("span.number").text());
                            String price = textContainer.select("div.line").select("span.number").text();
                            ///////////////////price
                            if (price == "") {
                                System.out.println("\n**************Empty Value!**************\n");
                                System.out.println("             Value: Price\n                Category: " + category);
                                price = "0";
                            }
                            Product p = new Product("0",edit_name, resultStrings, barcod, new Point(1, 4), category, unit_type, price, "ק\"ג","?");
                            callback.onProductReady(p);

                        }
                    } else {
                        Boolean dont = false;
                        Elements data = document.select("ul#mainProductGrid");
                        product_loop:
                        for (Element li : data.select("li[data-food]")) {
                            String imgsrc2 = li.select("div").select("div-wrapper-top-product").select("a").select("img").attr("src");
                            String imgsrc = li.select("a").select("img").attr("src");
                            if (imgsrc == "") {
                                //                        System.out.println("\n**************Empty Value!**************\n");
                                //                        System.out.println("                Value: ImageSRC\n                Category: "+category);
                                continue;
                            }
                            String[] splitStrings = imgsrc.split("/");
                            ArrayList<String> resultStrings = new ArrayList<>();
                            // הוספת התוצאות ל-ArrayList
                            for (int i = 1; i < splitStrings.length; i++) {
                                resultStrings.add(splitStrings[i]);
                            }
                            if (resultStrings.size() != 2) {
                                if (category .equals("מוצרים לאפיה ובישול")){
                                    String name = resultStrings.get(resultStrings.size() - 1);
                                    String path = "cooking_product-milk_files";
                                    resultStrings.clear();
                                    resultStrings.add(path);
                                    resultStrings.add(name);
                                } else {
                                    System.out.println("\n**************IMagePath Error Value!**************\n");
                                    System.out.println("             No 2: ImageSRC\n                Category: " + category);
                                    System.out.println();
                                    System.out.println("imgesrc:  " + resultStrings);
                                    continue;
                                }
                            }
                            String barcod = li.attr("data-product-code");
                            Matcher matcher = Pattern.compile("(\\d+)").matcher(barcod);
                            if (matcher.find()) {
                                barcod = matcher.group(1);
                                //                        System.out.println(matcher.group(1));
                            }
                            ///////////barcod
                            if (barcod == "") {
                                System.out.println("\n**************Empty Value!**************\n");
                                System.out.println("             Value: Bar-cod\n                Category: " + category);
                                break;
                            }
                            String unit_type = li.attr("data-selling-method");
                            ///////// unit type
                            if (unit_type == "") {
                                System.out.println("\n**************Empty Value!**************\n");
                                System.out.println("             Value: unit_type\n                Category: " + category);
                                break;
                            }
                            Elements textContainer = li.select("div.textContainer");

                            String name = textContainer.select("div.text.description").select("span").text();
                            /////////name
//                            System.out.println(name);
                            if (name == "") {
                                System.out.println("\n**************Empty Value!**************\n");
                                System.out.println("             Value: Name\n                Category: " + category);
                                break;
                            }

                            Point range;
                            if (category == "מוצרי עוף והודו") {
                                range = new Point(1, 4);
                                if (name.contains("ארוז") || name.contains("קפוא"))
                                    continue product_loop;
                            } else if (category == "ירקות טריים" || category.contains("פירות")) {
                                range = new Point(4, 20);
                                if (name.contains("ארוז") || name.contains("קפוא") || name.contains("אורגני") || name.contains("מארז") || name.contains("אפייה")
                                        || name.contains("סגירה") || name.contains("רוטב"))
                                    continue product_loop;
                            } else if (category == "מוצרי בשר, עוף ודגים קפואים") {
                                range = new Point(100, 180);

                            } else if (category=="מוצרי יסוד ותבלינים") {
                                if(name.contains("תערובת")||name.contains("תיבול")){
                                    continue product_loop;
                                }
                                range = new Point(300, 400);
                            } else if (category=="סוכר") {
                                if(name.contains("אורגני")||name.contains("דק")||name.contains("גולד")||name.contains("קלאסי")){
                                    continue product_loop;
                                }
                                range = new Point(300, 400);
                            } else if (category=="מוצרי חלב וביצים") {
                                if(name.contains("חופש")||name.contains("אומגה")||name.contains("פעם")||name.contains("פלוס")||name.contains("אורגני")||name.contains("ארוז")) {
                                    continue product_loop;
                                }
                                range = new Point(5, 30);
                            } else if (category=="גבינות מעדנייה") {
                                if(name.contains(".")|| name.contains("ארוז")|| name.contains("בייבי")|| name.contains("כדור")
                                        || name.contains("קלאסי")|| name.contains("מיושנ")||name.contains("ליטא")||name.contains("מגורד")){
                                    continue product_loop;
                                }
                                range = new Point(10, 40);
                            } else if (category == "מוצרים לאפיה ובישול") {
                                range = new Point(100, 130);
                            } else if (category.contains("שימורים")) {
                                range = new Point(200, 300);
                            } else {
                                if (name.contains("ארוז") || name.contains("אורגני") || name.contains("אריז") || name.contains("מארז")
                                        || name.contains("סגירה") || name.contains("רוטב")) {
                                    continue product_loop;
                                }
                                range = new Point(120, 250);
                            }
                            String[] nameStrings = name.split(" ");
                            String edit_name = "";
//                         הוספת התוצאות ל-ArrayList
                            for (int i = 0; i < nameStrings.length; i++) {
                                if (category.contains("ודגים קפואים")) {
                                    if (nameStrings[i].contains("קפוא") || nameStrings[i].contains("0")) {
                                        continue;
                                    }
                                } else if (category.contains("מוצרי חלב וביצים")){
                                    if(nameStrings[i].contains("מהדרין")||nameStrings[i].contains("שופרסל")) {
                                        continue;
                                    }
                                } else if (category.contains("גבינות מעדנייה")){
                                        if (i==0 && (!nameStrings[i].contains("גבינ") )){
                                            edit_name="גבינת";
                                        }
                                    if(nameStrings[i].contains("מהדרין")||nameStrings[i].contains("שופרסל")) {
                                        continue;
                                    }
                                }else if (category.contains("מוצרי יסוד ותבלינים")){
                                    if(nameStrings[i].contains("מיכל")||nameStrings[i].contains("שקית")||nameStrings[i].contains("שופרסל")){
                                        continue;
                                    }

                                } else if (category.contains("מלח")) {
                                    if(nameStrings[i].contains("שופרסל") || nameStrings[i].contains("טבעי")){
                                        continue ;
                                    }
                                } else if (category.contains("סוכר")) {
                                    if(nameStrings[i].contains("שופרסל") || nameStrings[i].contains("צנצנת")){
                                        continue ;
                                    }

                                } else if (category.contains("מוצרי עוף והודו") || category.contains("דגים טריים")) {
                                    if (nameStrings[i].contains("0") || nameStrings[i].contains("ק\"ג") || nameStrings[i].contains("קג") ||
                                            nameStrings[i].contains("טחינה") || nameStrings[i].contains(".") || nameStrings[i].contains("שופרסל") ||
                                            nameStrings[i].contains("גדול") || nameStrings[i].contains("נקי") || nameStrings[i].contains("שלם")
                                            || nameStrings[i].contains("עצמי") || nameStrings[i].contains("פרימיום") || nameStrings[i].contains("עטרה") || nameStrings[i].contains("משק") || nameStrings[i].contains("ארצי") ||
                                            nameStrings[i].contains("טרי") || nameStrings[i].contains("כ-") ||
                                            nameStrings[i].length() < 2) {
                                        continue;
                                    }
                                } else if (nameStrings[i].contains("שופרסל") || nameStrings[i].contains("טרי") || nameStrings[i].contains("עצמי") || nameStrings[i].contains("מוכשר") || nameStrings[i].contains("יבוא")
                                        || nameStrings[i].contains("מיקס") || nameStrings[i].contains("גרם") || nameStrings[i].contains("כ-") || nameStrings[i].contains("כ -") || nameStrings[i].contains(".") || nameStrings[i].contains("מובחר")
                                        || nameStrings[i].contains("שטופ") || nameStrings[i].contains("יחידה") || nameStrings[i].contains("/")) {
                                    continue;
                                }
                                edit_name = edit_name + " " + nameStrings[i];
                            }

                            if (edit_name.length() == 0) {
                                System.out.println("Name is NULL!!");
                                continue;
                            }
                            for (String n : cat_names) {
                                if (name.equals(n)) {
                                    continue product_loop;
                                }
                            }
                            cat_names.add(edit_name);
///////////////////////////////////////////document.select("#docs > div:eq(1) > h4 > a").attr("href"); div/div[3]/div[1]/div/span[1]
                            String unit_weight = textContainer.select("div > div.labelsListContainer > div.smallText > div > span:nth-child(1)").text();
                            if (unit_weight.length() == 0) {
                                unit_weight = "?";
                            }
                            //                    System.out.println(li.attr("data-selling-method"));
                            //                    System.out.println(li.select("div").select("div.textContainer").select("div").select("div.line").select("span.number").text());
                            /////////////////////bran name 2
                            String unit_brand=textContainer.select("div > div.labelsListContainer > div.smallText > div > span:nth-child(2)").text();
                            if(unit_brand==""){
                                unit_brand="?";
                            }
                            ///////////////////price
                            String price = textContainer.select("span.price").select("span.number").text();
                            if (price == "") {
//                            price=textContainer.select("span.price").select("span.number").text();
//                            System.out.println("\n**************Empty Value!**************\n");
//                            System.out.println("             Value: Price\n                Category: "+category);
                                price = "?";
                            }


                            Product p = new Product("0",edit_name, resultStrings, barcod, range, category, unit_type, price, unit_weight,unit_brand);
                            callback.onProductReady(p);

                        }

                    }
                }
                callback.onAllProductsReady();
            }

            public interface Callback {
                void onProductReady(Product product);

                void onAllProductsReady();
            }
        }
        ;


    }



///////////////////////////////////////////////////////
//import com.google.firebase.storage.FirebaseStorage;
//        import com.google.firebase.storage.StorageReference;
//        import com.google.android.gms.tasks.OnSuccessListener;
//        import com.google.android.gms.tasks.OnFailureListener;
//
//// ...
//
//// קבועים שמכילים מיקום הקובץ המקורי והיעד
//private static final String SOURCE_PATH = "source_folder/source_file.jpg";
//private static final String DESTINATION_PATH = "destination_folder/destination_file.jpg";
//
//// קבועים שמכילים שמות התיקיות ב-Firebase Storage
//private static final String BUCKET_NAME = "your_bucket_name";
//private static final String STORAGE_FOLDER = "your_storage_folder";
//
//// ...
//
//// אתחול חיבור ל-Firebase Storage
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//
//// מציאת קובץ המקור
//        StorageReference sourceFileRef = storageRef.child(STORAGE_FOLDER + "/" + SOURCE_PATH);
//
//// מציאת התיקיה היעד
//        StorageReference destinationFolderRef = storageRef.child(STORAGE_FOLDER);
//
//// העתקת הקובץ לתיקיה היעד
//        sourceFileRef.getFile(destinationFolderRef.child(DESTINATION_PATH))
//        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//@Override
//public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//        // ההעתקה הצליחה
//        // ניתן להוסיף כאן פעולות נוספות או לטפל בקובץ המעותק באופן רצוי
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception exception) {
//        // כשההעתקה נכשלה
//        // ניתן לטפל בשגיאה כאן
//        }
//        });
