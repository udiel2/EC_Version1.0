package com.application.easycook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.databinding.FragmentPantryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class PantryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseProduct> parseProducts= new ArrayList<>();

    FragmentPantryBinding binding;
    StorageReference storageRef;
    FloatingActionButton floatingActionButton;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public PantryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
//    public static PantryFragment newInstance(String param1, String param2) {
//        PantryFragment fragment = new PantryFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = FragmentPantryBinding.inflate(getLayoutInflater());
//        recyclerView = binding.recycleView1;
//        //recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(binding.linarRC.getContext()));
//        adapter=new ParseAdapter(parseProducts,binding.getRoot().getContext());
//        recyclerView.setAdapter(adapter);
//
//        Content content=new Content();
//        content.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry, container, false);

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

        floatingActionButton=view.findViewById(R.id.add_product_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PantryAddProduct());
            }
        });
//        binding.addProductButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                replaceFragment(new PantryAddProduct());
//            }
//        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
}