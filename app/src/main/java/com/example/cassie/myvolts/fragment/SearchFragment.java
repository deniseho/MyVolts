//package com.example.cassie.myvolts.fragment;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.cassie.myvolts.R;
//import com.example.cassie.myvolts.SearchActivity;
//import com.example.cassie.myvolts.TabActivity;
//import com.example.cassie.myvolts.TypeActivity;
//import com.example.cassie.myvolts.util.TestUtil;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
//import static android.app.Activity.RESULT_OK;
//
////import com.example.cassie.myvolts.TypeActivity;
//
///**
// * Created by cassie on 23/05/2017.
// */
//public class SearchFragment extends Fragment{
//    View view;
//    @BindView(R.id.search)
//    TextView search;
//    @BindView(R.id.brand)
//    TextView brandTextView;
//    @BindView(R.id.device)
//    TextView deviceTextView;
//    @BindView(R.id.model)
//    TextView modelTextView;
//
//    Unbinder unbinder;
//
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.fragment_search, container,false);
//        }
//        unbinder = ButterKnife.bind(this, view);
//        sharedPreferences = getActivity().getSharedPreferences("base64", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//
//        init();
//
//        return view;
//    }
//
//    public void init(){
//        Bundle bundle=this.getArguments();
//        if(bundle != null) {
//            String made = bundle.getString("madeInput");
//            String type = bundle.getString("deviceInput");
//            String model = bundle.getString("modelInput");
//
//            if (made != null && type != null && model != null) {
//                brandTextView.setText(made);
//                deviceTextView.setText(type);
//                modelTextView.setText(model);
//            }
//        }
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick({R.id.brand, R.id.device, R.id.model, R.id.sbut,R.id.search})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.brand:
//                TestUtil.storeSearchClicks(sharedPreferences, editor, "brandClick");
//                Intent pp=new Intent(getActivity(), TypeActivity.class);
//                startActivityForResult(pp,200);
//                break;
//            case R.id.device:
//                TestUtil.storeSearchClicks(sharedPreferences, editor, "deviceClick");
//                String p=brandTextView.getText().toString();
//                if(!TextUtils.isEmpty(p)){
//                    Intent device = new Intent(getActivity(), TypeActivity.class);
//                    device.putExtra("made",p);
//                    startActivityForResult(device,300);
//                }else{
//                    Toast.makeText(getActivity(),"Please select a manufaturer",Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//            case R.id.model:
//                TestUtil.storeSearchClicks(sharedPreferences, editor, "modelClick");
//                String ppp=brandTextView.getText().toString();
//                String pdevice=deviceTextView.getText().toString();
//
//                if(TextUtils.isEmpty(ppp)){
//                    Toast.makeText(getActivity(),"Please select a Brand",Toast.LENGTH_SHORT).show();
//                }else if(TextUtils.isEmpty(pdevice)){
//                    Toast.makeText(getActivity(),"Please select a device",Toast.LENGTH_SHORT).show();
//                }else{
//                    Intent model = new Intent(getActivity(), TypeActivity.class);
//                    model.putExtra("made",ppp);
//                    model.putExtra("device", pdevice);
//                    startActivityForResult(model,400);
//                }
//
//                break;
//            case R.id.sbut:
//                doSearch();
//                break;
//            case R.id.search:
//                TestUtil.storeSearchClicks(sharedPreferences, editor, "searchBoxClick");
//                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                startActivity(intent);
//                break;
//        }
//    }
//
//    private void doSearch() {
//        String made=brandTextView.getText().toString();
//        String device=deviceTextView.getText().toString();
//        String model=modelTextView.getText().toString();
//        if(TextUtils.isEmpty(made)){
//            Toast.makeText(getActivity(),"Please select a Manufaturer",Toast.LENGTH_SHORT).show();
//            return;
//        }else if(TextUtils.isEmpty(device)){
//            Toast.makeText(getActivity(),"Please select a Device",Toast.LENGTH_SHORT).show();
//            return;
//        }else if(TextUtils.isEmpty(model)) {
//            Toast.makeText(getActivity(),"Please select a Model",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(getActivity(), TabActivity.class);
//        intent.putExtra("made",made);
//        intent.putExtra("type",device);
//        intent.putExtra("model",model);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode != Activity.RESULT_CANCELED) {
//            String rs = data.getStringExtra("result");
//            if (requestCode == 200 && resultCode == RESULT_OK) {
//                brandTextView.setText(rs);
//                deviceTextView.setText("");
//            }
//            if (requestCode == 300 && resultCode == RESULT_OK) {
//                deviceTextView.setText(rs);
//            }
//
//            if (requestCode == 400 && resultCode == RESULT_OK) {
//                modelTextView.setText(rs);
//            }
//
//        }
//    }
//
//
//}
