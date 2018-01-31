package com.example.cassie.myvolts.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.db.DbHelp;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SetFragment extends Fragment {
    View view;

    Unbinder unbinder;

    DbHelp dbHelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_set, container, false);
        }
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
