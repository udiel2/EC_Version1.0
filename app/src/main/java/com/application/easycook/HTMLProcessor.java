package com.application.easycook;

import static android.content.ContentValues.TAG;

import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class
HTMLProcessor {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference dataRef;
    private ExecutorService executor;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private List<String> htmls=new ArrayList<>();
    FirebaseAuth mAuth;

    public HTMLProcessor() {
        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        htmls.add("bazek-frozen-milk");
        htmls.add("bazek-frozen-parve");
        htmls.add("chiken-fresh");
        htmls.add("cooking_product-milk");
        htmls.add("eshel");
        htmls.add("fhis-chiken-meet-frozen");
        htmls.add("fish-fresh");
        htmls.add("fruit-fresh");
        htmls.add("jous-priniv");
        htmls.add("meat-fresh");
        htmls.add("oils");
        htmls.add("orez_pasta");
        htmls.add("poding");
        htmls.add("seeds_and_pinats");
        htmls.add("shamenet");
        htmls.add("shimorim-1");
        htmls.add("shmarim");
        htmls.add("shokochips");
        htmls.add("sirops");
        htmls.add("sops-powder");
        htmls.add("sumsum_powder");
        htmls.add("tavlinim");
        htmls.add("veg-freshveg-fresh");
        htmls.add("veg-meet-freez");
        htmls.add("eshel");
        htmls.add("eshel");
        htmls.add("eshel");

        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        mAuth = FirebaseAuth.getInstance();
        String email = "udi_test@test.gr";
        String password = "test123";
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuth.signInWithEmailAndPassword(email, password);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener((Executor) this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            System.out.println("מחובר");
//                        } else {
//                            Log.e("HTMLProcessor", "Failed to get current user.");
//                        }
//                    } else {
//                        Log.e("HTMLProcessor", "Failed to sign in: " + task.getException().getMessage());
//                    }
//                });
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            database = FirebaseDatabase.getInstance("https://ezcookversion1-default-rtdb.europe-west1.firebasedatabase.app");
            dataRef = database.getReference(); // Replace "your_collection_name" with your desired collection name

            // Create a fixed-size thread pool with multiple threads
            int numThreads = Runtime.getRuntime().availableProcessors(); // Adjust the number of threads based on your requirements
            executor = Executors.newFixedThreadPool(numThreads);
            // do your stuff
        } else {
            System.out.println("not get user!!!");
            // Initialize Firebase Realtime Database

        }


    }

    public void processHTML() {
        // Assuming your HTML file is stored in Firebase Storage with a specific path
//        StorageReference fileRef = storageReference.child("html_shufersal/tavlinim.html"); // Replace "path_to_your_html_file.html" with the actual path to your HTML file in Firebase Storage

        for(String category:htmls){
            StorageReference fileRef = storageReference.child("html_shufersal/"+category+".html");
            fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                String r=category;
                String html = new String(bytes);
                Document doc = Jsoup.parse(html);
                Elements data = doc.select("main#main");

                for (Element li : data.select("section.tileSection3 li")) {
                    if (!(li.attr("data-product-name").toString()=="")) {
                        processElementsBatch2(li,r);
                    }
                }
                // Shut down the executor and wait for all tasks to complete
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    Log.e("HTMLProcessor", "Error waiting for tasks to complete: " + e.getMessage());
                }

                Log.d("HTMLProcessor", "HTML processing completed.");
            }).addOnFailureListener(exception -> {
                Log.e("HTMLProcessor", "Error downloading HTML file: " + exception.getMessage());
            });
            System.out.println(category);
        }
//        gs://ezcookversion1.appspot.com/html_shufersal/tavlinim.html
        // Download the HTML file and process it
//        fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
//            String html = new String(bytes);
//            Document doc = Jsoup.parse(html);
//
//            // Extract all elements with tag "IL"
//
////            System.out.println(doc);
//            // Split the elements into multiple batches for parallel processing
////            List<List<Element>> elementBatches = splitElementsIntoBatches(elements);
//            Elements data = doc.select("main#main");
////            String title = doc.select("main#main").select("div.text.description").select("strong").text();
////            System.out.println(data);
//            for (Element li : data.select("section.tileSection3 li")) {
//                executor.submit(() -> processElementsBatch(li));
//                if (!(li.attr("data-product-name")=="")) {
////                    System.out.println(li.attr("data-product-name"));
////                    System.out.println(li.select("div.text.description").text());
//                    processElementsBatch(li);
//                }
////                String title2 = li.select("div.text.description").select("strong").text();
//////                System.out.println(title2);
////                if(title2=="")
////                    continue;
////                String imgsrc = li.select("a").select("img").attr("src");
//////                String imgsrc = li.select("img").attr("src");
////                String bCod=li.attr("data-product-code");
////                Matcher matcher = Pattern.compile("(n*)").matcher(bCod);
////                int bar_cod = 0;
////                if (matcher.find()) {
////                    bar_cod= Integer.parseInt(matcher.group(1));
////                }
////                String categoy="vegetables";
////                Point life_time=new Point(7,20);
////                Product p=new Product(title2,imgsrc,li.id(), bar_cod,life_time,categoy);
////                p.showProduct();
//////                String imgsrc1 = li.select("img").attr("src");
//////                String title1 = li.attr("data-product-name");
//////                System.out.println(imgsrc1);
////
////                dataRef.child("Product").child("vegetables").setValue(p);
////                toastMessage("Adding " + newFood + " to database...");
////                dataRef.push().setValue(p);// Assuming you have a "Property" class to hold the properties
//
//
//
//
//
////                String imgsrc = li.select("img").attr("src");
////                String title = li.attr("data-product-name");
////                if (imgsrc == "" || imgsrc==null || title==null)
////                    continue;
//////                        titels.add(title);
//////                        imag.add(imgsrc);
////                System.out.println(imgsrc);
////                System.out.println(title);
////                dataRef.push().setValue(new Product(imgsrc, title, li.id()));// Assuming you have a "Property" class to hold the properties
//
//            }
//
//////             Submit each batch of elements to the executor for processing
////            for (List<Element> batch : elementBatches) {
////                executor.submit(() -> processElementsBatch(batch));
////            }
//
//            // Shut down the executor and wait for all tasks to complete
//            executor.shutdown();
//            try {
//                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//            } catch (InterruptedException e) {
//                Log.e("HTMLProcessor", "Error waiting for tasks to complete: " + e.getMessage());
//            }
//
//            Log.d("HTMLProcessor", "HTML processing completed.");
//        }).addOnFailureListener(exception -> {
//            Log.e("HTMLProcessor", "Error downloading HTML file: " + exception.getMessage());
//        });
    }

    private List<List<Element>> splitElementsIntoBatches(Elements elements) {
        // Split the elements into multiple batches for parallel processing
        int batchSize = 100; // Adjust the batch size based on your requirements
        int numElements = elements.size();
        List<List<Element>> elementBatches = new ArrayList<>();
        for (int i = 0; i < numElements; i += batchSize) {
            int endIndex = Math.min(i + batchSize, numElements);
            List<Element> batch = elements.subList(i, endIndex);
            elementBatches.add(batch);
        }
        return elementBatches;

    }
    private void processElementsBatch2(Element element,String category) {

        String title = element.select("div.text.description").text();
        if(title=="")
            return;
        String imgsrc = element.select("img").attr("src");
        String bCod=element.attr("data-product-code");
        String w_methud=element.attr("data-selling-method").toString();

        ArrayList<String> a=new ArrayList<>();
        a.add(imgsrc);
        Matcher matcher = Pattern.compile("(\\d+)").matcher(bCod);
//        Integer bar_cod = 0;
        if (matcher.find()) {
            bCod = matcher.group(1);
        }
        Point life_time;
//        String categoy="spices";
        if (category=="chiken-fresh"||category=="meat-fresh"||category=="fish-fresh")
            life_time=new Point(1,3);
        else if (category=="tavlinim"||category=="sumsum_powder"||category=="sirops"||category=="oils") {
            life_time=new Point(150,400);
        } else
            life_time=new Point(4,14);
        String[] splitStrings = imgsrc.split("/");
//        Product p=new Product(title,a, bCod,life_time,category,w_methud,"0","");
//
//        System.out.println(w_methud);
//        if(w_methud.equals("BY_WEIGHT")||w_methud.equals("BY_UNIT")){
//            p.showProduct();
//            dataRef.child("Products").child(category).push().setValue(p);// Assuming you have a "Property" class to hold the properties
//
//        }
 }


    private void processElementsBatch(Element element) {
        // Iterate over the elements and extract the desired properties
//        for (Element element : elements) {
//            String property1 = element.select("img").attr("src"); // Replace "property1" with the name of the first property you want to extract
//            String property2 = element.attr("property2"); // Replace "property2" with the name of the second property you want to extract
//            // Add the properties to Firebase Realtime Database
////            System.out.println("src: "+property1+"\n"+"kokoko: "+property2);
//            dataRef.child(element.id()).setValue(new Product(property1, property2, "test"));// Assuming you have a "Property" class to hold the properties
//        }
        try {
            String title = element.select("div.text.description").text();
            if(title=="")
                return;
            String imgsrc = element.select("img").attr("src");
            String bCod=element.attr("data-product-code");
            String w_methud=element.attr("data-selling-method").toString();
//        if(w_methud!="BY_WEIGHT"){
//            return;
//        }

            Matcher matcher = Pattern.compile("(\\d+)").matcher(bCod);
//        Integer bar_cod = 0;
            if (matcher.find()) {
                bCod = matcher.group(1);
//            try {
//                bar_cod = Integer.parseInt(bCod);
//            }
//            catch (NumberFormatException e) {
//                System.out.println(e.getMessage());
//                bar_cod = 0;
//            }
//            bar_cod= Integer.parseInt(matcher.group(0).toString());
            }
            String categoy="spices";

            Point life_time=new Point(300,500);
//            Product p=new Product(title,imgsrc, bCod,life_time,categoy,w_methud,"0");
//        p.showProduct();
//        dataRef.child("Product").child("vegetables").push().setValue(p);// Assuming you have a "Property" class to hold the properties
//            System.out.println(w_methud);
//            if(w_methud.equals("BY_WEIGHT")){
//                p.showProduct();
//                dataRef.child("Product").child("spices").push().setValue(p);// Assuming you have a "Property" class to hold the properties
//
//            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        /////////////////////////////////////////////////////////////////////////////////////
//        String title = element.select("div.text.description").text();
//        if(title=="")
//            return;
//        String imgsrc = element.select("img").attr("src");
//        String bCod=element.attr("data-product-code");
//        String w_methud=element.attr("data-selling-method").toString();
////        if(w_methud!="BY_WEIGHT"){
////            return;
////        }
//
//        Matcher matcher = Pattern.compile("(\\d+)").matcher(bCod);
////        Integer bar_cod = 0;
//        if (matcher.find()) {
//            bCod = matcher.group(1);
////            try {
////                bar_cod = Integer.parseInt(bCod);
////            }
////            catch (NumberFormatException e) {
////                System.out.println(e.getMessage());
////                bar_cod = 0;
////            }
////            bar_cod= Integer.parseInt(matcher.group(0).toString());
//        }
//        String categoy="spices";
//
//        Point life_time=new Point(300,500);
//        Product p=new Product(title,imgsrc, bCod,life_time,categoy,w_methud);
////        p.showProduct();
////        dataRef.child("Product").child("vegetables").push().setValue(p);// Assuming you have a "Property" class to hold the properties
//        System.out.println(w_methud);
//        if(w_methud.equals("BY_WEIGHT")){
//            p.showProduct();
//            dataRef.child("Product").child("spices").push().setValue(p);// Assuming you have a "Property" class to hold the properties
//
//        }
        ////////////////////////////////////////////////////////////////////
//            return;
//        }

//        String imgsrc = element.select("img").attr("src");
        // div with class=masthead
        // Add the properties to Firebase Realtime Database
//            System.out.println("src: "+property1+"\n"+"kokoko: "+property2);
//        dataRef.push().setValue(new Product(title,imgsrc,element.id(),bar_cod,life_time,categoy);// Assuming you have a "Property" class to hold the properties
    }
}
