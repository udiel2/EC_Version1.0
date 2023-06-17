package com.application.easycook.MyPantryPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.Product;
import com.application.easycook.R;
import com.application.easycook.databinding.FragmentPantryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class PantryFragment extends Fragment {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private ArrayList<Product> Plist= new ArrayList<>();
    private ArrayList<PantryProduct> pantryProducts= new ArrayList<>();
    MyPantry myPantry;

    FragmentPantryBinding binding;
    StorageReference storageRef;
    FloatingActionButton floatingActionButton;

    ArrayList<ConstraintLayout> constraintLayouts;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public PantryFragment(MyPantry pantry) {
        myPantry=pantry;

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
//        Plist = HomeActivity.myPantry.getProductList();
//        for(int i=0;i<Plist.size();i++){
//            Product p=Plist.get(i);
//            parseProducts.add(new ParseProduct(p.getImageName(),p.getName()));
//            System.out.println(p.getName());
//        }
//        Plist = myPantry.getProductList();
        pantryProducts=myPantry.getPantryList();
//        binding = FragmentPantryBinding.inflate(getLayoutInflater());
//        recyclerView = binding.recycleView1;
        //recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(binding.linarRC.getContext()));
//        adapter=new ParseAdapter(Plist,binding.getRoot().getContext());
//        recyclerView.setAdapter(adapter);
//
//        Content content=new Content();
//        content.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Plist = HomeActivity.myPantry.getProductList();
//        for(int i=0;i<Plist.size();i++){
//            Product p=Plist.get(i);
//            parseProducts.add(new ParseProduct(p.getImageName(),p.getName()));
//            System.out.println(p.getName());
//        }
        return inflater.inflate(R.layout.pantry_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Plist = myPantry.getProductList();
//        PantrySubHomeRecyceleBuilder builder=new PantrySubHomeRecyceleBuilder(myPantry,getContext(),getLayoutInflater());
//        this.constraintLayouts=builder.main_constraint;
//        PantrySubHome2 pantrySubHome=new PantrySubHome2(constraintLayouts);
//        replaceSubFragment(pantrySubHome);
//        builder.setOnButtonClickListener(new PantrydisplayProductDetails.OnButtonClickListener() {
//            @Override
//            public void onButtonClick() {
//                PantryFragment pantrySubHome=new PantryFragment(myPantry);
//                replaceFragment(pantrySubHome);
//            }
//        });
        replaceSubFragment(new PantrySubHome(myPantry));
        floatingActionButton=view.findViewById(R.id.floating_add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PantryAddProduct(myPantry));
            }
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
    private void replaceSubFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.pantry_sub_fragment,fragment);
        fragmentTransaction.commit();
    }
    public void refresh(){
        PantrySubHome2 pantrySubHome=new PantrySubHome2(constraintLayouts);
        replaceSubFragment(pantrySubHome);

    }
}