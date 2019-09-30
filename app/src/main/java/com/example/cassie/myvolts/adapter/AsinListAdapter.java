package com.example.cassie.myvolts.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.dto.DeviceData;
import com.example.cassie.myvolts.util.DigitUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cassie on 23/06/2017.
 */

public class AsinListAdapter extends BaseAdapter {
    private List<DeviceData> list;
    private Context mContext;
    private String searchStr;

    private Animation animation;

    private Map<Integer, Boolean> isFrist;

    public AsinListAdapter(List<DeviceData> list, Context mContext, String searchStr) {
        this.list = list;
        this.mContext = mContext;
        this.searchStr = searchStr;

        animation = AnimationUtils.loadAnimation(mContext, R.anim.list_view_annimation);
        isFrist = new HashMap<Integer, Boolean>();
    }

    public void setDatas(List<DeviceData> datas) {
        this.list = datas;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.asin_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        if (isFrist.get(position) == null || isFrist.get(position)) {
            convertView.startAnimation(animation);
            isFrist.put(position, false);
        }


        final DeviceData e = list.get(position);

        holder.name.setText(e.getName());
        holder.brand.setText(e.getManufacturer());
        int chageTextColor;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimaryDark));

        String mDrawableName = DigitUtil.getNumericPid(e.getP_id());
        int resID = mContext.getResources().getIdentifier("pi" + mDrawableName , "drawable", mContext.getPackageName());
        if(resID != 0)
            holder.img.setImageResource(resID);


        //getProfileImage g = new getProfileImage(holder.img);
        //g.execute("https://goo.gl/images/oglZKy");

        return convertView;
    }


    public class getProfileImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView img;

        public getProfileImage(ImageView iv) {
            this.img = iv;
        }

        @Override
        protected Bitmap doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            return ImageLoader.getInstance().loadImageSync(arg0[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null) {
                img.setImageBitmap(result);
            }

        }
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.img)
        ImageView img;

        @BindView(R.id.brand)
        TextView brand;

        @BindView(R.id.x)
        TextView x;

        @BindView(R.id.y)
        TextView y;

        @BindView(R.id.z)
        TextView z;

        @BindView(R.id.asin)
        TextView asin;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
