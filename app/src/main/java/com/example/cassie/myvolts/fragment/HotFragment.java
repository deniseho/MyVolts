package com.example.cassie.myvolts.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.adapter.HotAdapter;
import com.example.cassie.myvolts.db.DbHelp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HotFragment extends Fragment {
    View view;

    Unbinder unbinder;
    @BindView(R.id.datalist)
    ListView datalist;

    HotAdapter hotAdapter;
    DbHelp dbHelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hot, container,false);
        }
        unbinder = ButterKnife.bind(this, view);
        hotAdapter=new HotAdapter(getActivity());

        dbHelp = new DbHelp(getActivity());

        hotAdapter.setDatas(dbHelp.getHot());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
