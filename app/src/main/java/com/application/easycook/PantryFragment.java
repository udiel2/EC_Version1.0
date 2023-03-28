package com.application.easycook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.databinding.FragmentPantryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PantryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PantryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseProduct> parseProducts= new ArrayList<>();

    FragmentPantryBinding binding;
    StorageReference storageRef;
    FloatingActionButton floatingActionButton;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PantryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PantryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PantryFragment newInstance(String param1, String param2) {
        PantryFragment fragment = new PantryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        binding = FragmentPantryBinding.inflate(getLayoutInflater());
//        recyclerView = binding.recycleView1;
//        //recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(binding.linarRC.getContext()));
//        adapter=new ParseAdapter(parseProducts,binding.getRoot().getContext());
//        recyclerView.setAdapter(adapter);
//
//        Content content=new Content();
//        content.execute();
//        floatingActionButton=binding.addProductButton;
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                replaceFragment(new PantryAddProduct());
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry, container, false);
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
                String url = "https://www.shufersal.co.il/online/he/";
                url=url + "חלב וביצים";
                Document doc;
                doc = Jsoup.connect(url).get();

                Elements data = doc.select("main#main");
                ///System.out.println("totalPost:" + data.html());



                int size = data.size();
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
                for (int i = 0; i < size; i++) {
                    String imgsrc = data.select("section.tileSection3").select("ul.tileContainer").select("a.imgContainer").eq(i).select("img").attr("src");
                    System.out.println("lllllllllllll: "+imgsrc);
                    String title = data.select("section.tileSection3").select("ul.tileContainer").select("a.imgContainer").eq(i).attr("data-product-name");
                    //imgsrc = "https://res.cloudinary.com/misradia/image/upload/c_scale,f_auto,h_440/images/items/20938/original-42467.JPEG";
                    parseProducts.add(new ParseProduct(imgsrc, title));
                    Log.d("items", "img: " + imgsrc + " title: " + title);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding = FragmentPantryBinding.inflate(getLayoutInflater());
        RecyclerView recyclerView= view.findViewById(R.id.recycleView1);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ParseAdapter(parseProducts,getContext());
        recyclerView.setAdapter(adapter);


        Content content=new Content();
        content.execute();
        binding.addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PantryAddProduct());
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
}