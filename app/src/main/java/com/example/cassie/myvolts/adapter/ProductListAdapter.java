package com.example.cassie.myvolts.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.util.DigitUtil;
import com.example.cassie.myvolts.util.HttpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
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
            if(e.getProductId().equals("1") || e.getProductId().equals("2") ||
                    e.getProductId().equals("3") || e.getProductId().equals("4")){
                int resID = mContext.getResources().getIdentifier("prod" + e.getProductId() , "drawable", mContext.getPackageName());
                if(resID != 0)
                    holder.img.setImageResource(resID);
            }else {
                GetProductImgURL getProductImgURL = new GetProductImgURL(holder.img, DigitUtil.getNumericPid(e.getProductId()));
                getProductImgURL.execute(e.getProductId());
            }
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
        }else{
            holder.name.setText(e.getName());
        }

        return convertView;
	}
	
	/*public class getProfileImage extends AsyncTask<String, Void, Bitmap> {

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
	}*/

    public class GetProductImgURL extends AsyncTask<String, Void, String> {


        private final String id;

        private final ImageView img;

        public GetProductImgURL(ImageView iv, String id) {
            this.img = iv;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String url = "http://api.myjson.com/bins/1hcph0";//"http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.55/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0ASELECT%20%20%3Fimages%0AWHERE%20%0A%7B%20%0A%0A%20%3C" + arg0[0] + "%3E%20%3AhasImageFile%20%3Fimages%20.%0A%0A%7D%0A&limit=100&infer=true&";

            System.out.println(url);
            String result = "";
            try {
                result = HttpUtils.doGet(url);
            }catch(RuntimeException rt){

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            System.out.println(result);

            Document doc = null;

            if (null != result && !"".equals(result)) {
                try {
                    doc = DocumentHelper.parseText(result);
                    Element rootElt = doc.getRootElement();
                    Iterator iter = rootElt.elementIterator("results");

                    while (iter.hasNext()) {
                        Element resultRecord = (Element) iter.next();
                        Iterator itersElIterator = resultRecord.elementIterator("result");

                        while (itersElIterator.hasNext()) {
                            Element itemEle = (Element) itersElIterator.next();
                            Iterator literLIterator = itemEle.elementIterator("binding");
                            while (literLIterator.hasNext()) {
                                Element ele = (Element) literLIterator.next();
                                if ("images".equals(ele.attributeValue("name"))) {
                                    String imgs = ele.elementTextTrim("literal");
                                    String url = "http://frodo.digidave.co.uk/files/images/product/" + id + "/4/" + imgs;

                                    ImageLoader.getInstance().displayImage(url, img);
                                }
                                break;
                            }
                            break;
                        }
                        break;
                    }


                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
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
