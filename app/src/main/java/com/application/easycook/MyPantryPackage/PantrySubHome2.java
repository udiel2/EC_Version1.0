package com.application.easycook.MyPantryPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.easycook.Product;
import com.application.easycook.ProfileFragment;
import com.application.easycook.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PantrySubHome2 extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    private HashMap<String,ArrayList<Product>> producthash=new HashMap<>();
    private HashMap<String,ArrayList<PantryProduct>> pantryhash=new HashMap<>();
    private ArrayList<ConstraintLayout> constraintLayouts;


    public PantrySubHome2(ArrayList<ConstraintLayout> constraintLayout) {
        this.constraintLayouts=constraintLayout;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.pantry_home_fragment, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout linearLayout = view.findViewById(R.id.pantryhomelinarlayout);
        for(ConstraintLayout constraintLayout:constraintLayouts){
            linearLayout.addView(constraintLayout);
        }


//        linearLayout.addView(builder.buildLayout("ירקות טריים",builder.hashMap.get("ירקות טריים")));
    }






    }

