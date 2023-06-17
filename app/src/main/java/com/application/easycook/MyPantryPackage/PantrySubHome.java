package com.application.easycook.MyPantryPackage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.application.easycook.ProfileFragment;
import com.application.easycook.R;
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PantrySubHome extends Fragment {


    private RecyclerView recyclerView;
    List<String> stringsList=new ArrayList<>();
    private PantryAdapter adapter;
    private ArrayList<PantryAdapter> adapters;
    private ArrayList<PantryProduct> pantryProducts= new ArrayList<>();
    Date date = new Date(); // התאריך העדכני

    // יצירת אובייקט LocalDateTime מתאריך הראשון
    LocalDateTime localDateTime1 ;
    MyPantry myPantry;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PantrySubHome(MyPantry myPantry) {
        this.myPantry=myPantry;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        PantrySubHomeRecyceleBuilder builder=new PantrySubHomeRecyceleBuilder(myPantry,getContext(),linearLayout,getLayoutInflater());
        builder.setOnButtonClickListener(new PantrydisplayProductDetails.OnButtonClickListener() {
            @Override
            public void onButtonClick() {
                System.out.println("fdsfdsfsdgasdfgadfdsffsdg");
            }
        });
//        linearLayout.addView(builder.buildLayout("ירקות טריים",builder.hashMap.get("ירקות טריים")));
    }




    void setRecyclerView(Context context,HashMap<String,ArrayList<PantryProduct>> pantryhash){

    }

    }

