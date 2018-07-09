package com.example.cassie.myvolts.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cassie.myvolts.R;

public class ConfirmFragment extends DialogFragment {

    private EditText mEditText;

    public ConfirmFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ConfirmFragment newInstance(String brand) {
        ConfirmFragment frag = new ConfirmFragment();
        Bundle args = new Bundle();
        args.putString("brand", brand);
//        args.putString("device", "machine");
        frag.setArguments(args);
        return frag;
    }

    String mBrand, mDevice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBrand = getArguments().getString("brand");
//        mDevice = getArguments().getString("device");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_confirm, container, false);

        View brandName = rootView.findViewById(R.id.brand_name);
        ((TextView)brandName).setText(mBrand);

        Button btn = (Button)rootView.findViewById(R.id.confirm_cancel);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        // Get field from view
////        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        // Show soft keyboard automatically and request focus to field
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//    }


}
