package com.thanksbingo.bingo.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thanksbingo.bingo.R;
import com.thanksbingo.bingo.TabFragment.TabFirstDoor;
import com.thanksbingo.bingo.TabFragment.TabFirstIn;
import com.thanksbingo.bingo.TabFragment.TabSecondDoor;
import com.thanksbingo.bingo.TabFragment.TabSecondIn;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBingoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBingoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBingoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    View v;

    ArrayList<String> howManyRow = null;
    String whatFridge = null;

    private OnFragmentInteractionListener mListener;

    public static MyBingoFragment newInstance() {
        MyBingoFragment fragment = new MyBingoFragment();
        return fragment;
    }

    public MyBingoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = (RelativeLayout) inflater.inflate(R.layout.fragment_fridge_row_image, container, false);

        ListView lv = (ListView) v.findViewById(R.id.main_list);

        Button tab01 = (Button) v.findViewById(R.id.main_tab01);
        Button tab02 = (Button) v.findViewById(R.id.main_tab02);
        Button tab03 = (Button) v.findViewById(R.id.main_tab03);
        Button tab04 = (Button) v.findViewById(R.id.main_tab04);

        tab01.setOnClickListener(mTabClickListener);
        tab02.setOnClickListener(mTabClickListener);
        tab03.setOnClickListener(mTabClickListener);
        tab04.setOnClickListener(mTabClickListener);

        //처음 실행될때 냉장실 안이 선택되어 있는 상태로
        Fragment tabFirstDoor = new TabFirstDoor();
        FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
        transaction2.replace(R.id.tab_container, tabFirstDoor).commit();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View.OnClickListener mTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //냉장실문
                case R.id.main_tab01:
                    Toast.makeText(v.getContext(), "냉장실문", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getChildFragmentManager();

                    Fragment tabFirstDoor = new TabFirstDoor();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.tab_container, tabFirstDoor).commit();

                    break;
                //냉장실안
                case R.id.main_tab02:
                    Toast.makeText(v.getContext(), "냉장실안", Toast.LENGTH_SHORT).show();
                    Fragment tabFirstIn = new TabFirstIn();
                    FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
                    transaction2.replace(R.id.tab_container, tabFirstIn).commit();

                    break;
                //냉동실문
                case R.id.main_tab03:
                    Toast.makeText(v.getContext(), "냉동실문", Toast.LENGTH_SHORT).show();
                    Fragment tabSecondDoor = new TabSecondDoor();
                    FragmentTransaction transaction3 = getChildFragmentManager().beginTransaction();
                    transaction3.replace(R.id.tab_container, tabSecondDoor).commit();

                    break;
                //냉동실 안
                case R.id.main_tab04:
                    Toast.makeText(v.getContext(), "냉동실안", Toast.LENGTH_SHORT).show();
                    Fragment tabSecondIn = new TabSecondIn();
                    FragmentTransaction transaction4 = getChildFragmentManager().beginTransaction();
                    transaction4.replace(R.id.tab_container, tabSecondIn).commit();

                    break;
            }
        }
    };

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
