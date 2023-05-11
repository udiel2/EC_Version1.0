package com.application.easycook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.databinding.FragmentPantryAddProductBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PantryAddProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PantryAddProduct extends Fragment {


    SearchView searchView;
    RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseProduct> parseProducts= new ArrayList<>();
    private ArrayList<ParseProduct> chosenProducts= new ArrayList<>();

    FragmentPantryAddProductBinding binding;



    String query_text;



    public PantryAddProduct() {

        // Required empty public constructor
    }


    public static PantryAddProduct newInstance(String param1, String param2) {
        PantryAddProduct fragment = new PantryAddProduct();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry_add_product, container, false);
    }


    private class Content extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.shufersal.co.il/online/he/search?text=";
                url=url + query_text;
                Document doc;
                doc = Jsoup.connect(url).get();

                Elements data = doc.select("main#main");



                parseProducts.clear();
                for(int i =0;i<chosenProducts.size();i++){
                    parseProducts.add(chosenProducts.get(i));
                }

                int size = data.size();
                if(data.contains("section.tileSection3")){

                }
                Elements main_data= data.select("section.tileSection3");
                for (Element li:data.select("section.tileSection3 li")){
                    String imgsrc = li.select("a").select("img").attr("src");
                    System.out.println("lllllllllllll: "+imgsrc);
                    String title = li.select("div.text.description").select("strong").text();
                    if(!imgsrc.contains("http"))
                        continue;
                    parseProducts.add(new ParseProduct(imgsrc, title));
                    Log.d("items", "img: " + imgsrc + " title: " + title);

                }
//                for (int i = 0; i < size; i++) {
//                    String imgsrc = data.select("section.tileSection3").select("ul.tileContainer").select("a.imgContainer").eq(i).select("img").attr("src");
//                    System.out.println("lllllllllllll: "+imgsrc);
//                    String title = data.select("section.tileSection3").select("ul.tileContainer").select("a.imgContainer").eq(i).attr("data-product-name");
//                    //imgsrc = "https://res.cloudinary.com/misradia/image/upload/c_scale,f_auto,h_440/images/items/20938/original-42467.JPEG";
//                    parseProducts.add(new ParseProduct(imgsrc, title));
//                    Log.d("items", "img: " + imgsrc + " title: " + title);
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;

        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PantryAddProduct.Content content=new PantryAddProduct.Content();

        searchView=view.findViewById(R.id.searchAddProduct);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                PantryAddProduct.Content content=new PantryAddProduct.Content();
                if (newText.isEmpty()) {
                    query_text = "גזר";
                    return false;
                }
                else
                    query_text=newText;

                chosenProducts=adapter.getChosenProducts();

                content.execute();
                return true;
            }
        });
        recyclerView= view.findViewById(R.id.recycle_add_product);
        //recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter=new ParseAdapter(parseProducts,getContext());
        recyclerView.setAdapter(adapter);
        content=new PantryAddProduct.Content();
        content.execute();
        binding = FragmentPantryAddProductBinding.inflate(getLayoutInflater());
//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chosenProducts=adapter.getChosenProducts();
//            }
//        });
        Button Add=binding.button2;
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i =0;i<chosenProducts.size();i++){
                    HomeActivity.myPantry.addProduct(chosenProducts.get(i));
                }

                chosenProducts=adapter.getChosenProducts();

            }
        });





    }
}