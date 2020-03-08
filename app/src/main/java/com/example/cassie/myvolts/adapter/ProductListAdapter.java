package com.example.cassie.myvolts.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.dto.ProductData;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductListAdapter extends BaseAdapter{
	private List<ProductData> list;
	private Context mContext;
    private String searchStr;

    private Animation animation;

    private Map<Integer, Boolean> isFrist;


    public ProductListAdapter(List<ProductData> list, Context mContext, String searchStr) {
		this.list = list;
		this.mContext = mContext;
        this.searchStr = searchStr;

        animation = AnimationUtils.loadAnimation(mContext, R.anim.list_view_annimation);
        isFrist = new HashMap<Integer, Boolean>();

    }

    public void setDatas(List<ProductData> datas) {
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
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();


        if (isFrist.get(position) == null || isFrist.get(position)) {
            convertView.startAnimation(animation);
            isFrist.put(position, false);
        }


        final ProductData e = list.get(position);

        if(null != e.getProductId() && !"".equals(e.getProductId())){
            new GetProductImage(holder.img)
                    .execute(e.getFile());
        }

        if(searchStr != null && !searchStr.equals("")){
        String[] text = searchStr.trim().split(" ");
        int chageTextColor;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimaryDark));

        SpannableStringBuilder builder = new SpannableStringBuilder(
                e.getName());
        if (text.length > 0) {
                for(int i = 0; i < text.length; i++) {
                    chageTextColor = e.getName().toLowerCase().indexOf(text[i].toLowerCase());
                    if (chageTextColor != -1) {
                        redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorAccent));
                        builder.setSpan(redSpan, chageTextColor, chageTextColor
                                        + text[i].length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
            }
            holder.name.setText(builder);
        } else
            holder.name.setText(e.getName());
        }

        return convertView;
	}

    public class GetProductImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetProductImage(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.img)
        ImageView img;



        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
