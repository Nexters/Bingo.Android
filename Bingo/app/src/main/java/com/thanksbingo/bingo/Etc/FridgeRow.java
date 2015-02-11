package com.thanksbingo.bingo.Etc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.liveo.ndrawer.entity.Food;


public class FridgeRow extends Fragment {
    private static final String ARG_ROWHEIGHT = "rowheight";

    private int rowHeight;
    private Style style;

    private List<Food> foods;
    private FridgeRowListAdapter mAdapter;

    public enum Style {
        IMAGE, LIST
    }

    public static FridgeRow newInstance(int rowHeight, Style style) {
        FridgeRow fragment = new FridgeRow();
        Bundle args = new Bundle();
        args.putInt(ARG_ROWHEIGHT, rowHeight);
        fragment.setArguments(args);
        fragment.style = style;
        return fragment;
    }

    public FridgeRow() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rowHeight = getArguments().getInt(ARG_ROWHEIGHT);
        }
        foods = new ArrayList<Food>();
        foods.add(new Food("딸기", "2015-01-30"));
        foods.add(new Food("사과", "2015-01-31"));
        foods.add(new Food("참외", "2015-02-01"));
        foods.add(new Food("수박", "2015-02-02"));
        mAdapter = new FridgeRowListAdapter(getActivity(), foods);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        if (style == Style.IMAGE) {
            v = inflateAsImageView(v, inflater, container);

        } else {
            //리스트일 경우
            v = inflateAsListView(v, inflater, container);
        }
        return v;
    }

    private View inflateAsImageView(View v, LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_fridge_row_image, container, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeight);
        v.setLayoutParams(lp);
        return v;
    }

    private View inflateAsListView(View v, LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_fridge_row_list, container, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeight);
        v.setLayoutParams(lp);

        FridgeRowListAdapter adapter = new FridgeRowListAdapter(getActivity(), foods);
        //listview에 adapter를 적용
        ListView view = (ListView)v.findViewById(R.id.fragment_fridge_row_list_listview);
        view.setAdapter(mAdapter);
        return v;
    }
}
