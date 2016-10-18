package com.example.amira.nav;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;


/**
 * Created by ahmed on 14/10/16.
 */

public class MainFragment extends Fragment {


    public MainFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }
}
