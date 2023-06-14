package com.application.easycook.MyPantryPackage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.Product;
import com.application.easycook.R;
import com.application.easycook.database.DatabaseManager;
import com.application.easycook.databinding.FragmentPantryAddProductBinding;

import java.util.ArrayList;


public class PantryAddProduct extends Fragment {


    SearchView searchView;

    DatabaseManager databaseManager;


    RecyclerView recyclerView;
    private ParseAdapter adapter;

    private ArrayList<Product> selectedProducts = new ArrayList<>();
    private ArrayList<Product> products=new ArrayList<>();
    private static final String API_KEY = "abcdef1234567890";
    private static final String URL = "https://api.superget.co.il/";

    FragmentPantryAddProductBinding binding;
    MyPantry myPantry;

//    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();



    String query_text;



    public PantryAddProduct(MyPantry pantry) {
        myPantry=pantry;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseManager=new DatabaseManager(getContext());
//        pantryDatabaseHelper=new PantryDatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry_add_product, container, false);
    }


    private class Content2 extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;

        }

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentPantryAddProductBinding.inflate(getLayoutInflater());
        PantryAddProduct.Content2 content=new PantryAddProduct.Content2();

        searchView=view.findViewById(R.id.searchAddProduct);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Product> ps=databaseManager.getProductByName(query);
                for(Product p:ps){
                    p.showProduct();
                    products.add(p);
                }
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>1) {
                    products.clear();
                    databaseManager.open();
                    ArrayList<Product> ps=databaseManager.getProductByName(newText);
                    databaseManager.close();
                    if (ps != null) {
                        for (Product p : ps) {
                            p.showProduct();
                            products.add(p);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
                return true;
            }
        });
        recyclerView= view.findViewById(R.id.recycle_add_product);
        //recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter=new ParseAdapter(products,getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickedListener(new ParseAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int position) {
                adapter.toggleSelection(position);
                selectedProducts.clear();
                ArrayList<Product> p=adapter.getSelectedProducts();
                if(!p.isEmpty())
                    for(Product product: p){
                        selectedProducts.add(product);
                    }
            }
        });
//        content=new PantryAddProduct.Content2();
//        content.execute();
        Button Add=view.findViewById(R.id.AddButton);
//        Button log=view.findViewById(R.id.b_log);
//        dbHelper.syncWithFirestore();
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                if(!p.isEmpty())
//                    for(Product product: p){
//                        product.showProduct();
//                    }
//                dbHelper.syncWithFirestore();
                System.out.println("\n-----------------------------------Click--------------------------------\n");
                if(selectedProducts.isEmpty()){
                    System.out.println("Array is Empty!");
                }else
                    for(Product product:selectedProducts) {
                        product.showProduct();
                        myPantry.addProduct(product);
                    }
                replaceFragment(new PantryFragment(myPantry));
//                selectedProducts.clear();
//                products=dbHelper.getAllProducts();
//                PantryProduct pantryProduct=new PantryProduct("03LEWHjegOp0rwyLIQ3x","ירקות טריים","",100,new Date());
//                pantryDatabaseHelper.addProduct(pantryProduct);
//                System.out.println(pantryProduct);
            }
        });

//        Add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firestore.collection("Products")
//                        .whereEqualTo("category","בשר טרי")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Product product=document.toObject(Product.class);
//                                        temps.add(product);
//                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
////                chosenProducts=adapter.getChosenProducts();
////                for(int i =0;i<chosenProducts.size();i++){
////                    myPantry.addProduct(chosenProducts.get(i));
////                    System.out.println(chosenProducts.get(i).getTitle());
////                }
////                System.out.println("add");
////                replaceFragment(new PantryFragment(myPantry));
////                MyAsyncTask task = new MyAsyncTask();
////                task.execute();
//
//            }
//        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }

}