package com.thanksbingo.bingo.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thanksbingo.bingo.Activities.FridgeCustom;
import com.thanksbingo.bingo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {


    View v;
    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recipe, container, false);
        // Inflate the layout for this fragment
        Button btn_custom = (Button)v.findViewById(R.id.btn_fridge_custom);
        btn_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FridgeCustom.class);
                startActivity(i);

            }
        });
        return v;
    }


}
