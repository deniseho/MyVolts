package com.example.cassie.myvolts;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by deniseho on 19/09/2018.
 */
public class fetchData extends AsyncTask<Void,Void,JSONArray> {
    String data ="";
    
    @Override
    protected JSONArray doInBackground(Void... params) {
        JSONArray output_arr = new JSONArray();

        try {
//            URL url = new URL("https://api.myjson.com/bins/j5f6b");
            URL url = new URL("https://api.myjson.com/bins/1hcph0");
//            URL url = new URL("https://api.myjson.com/bins/hz4pg");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

//            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = bufferedReader.readLine();
            data += line;
            System.out.println("-------------data: " + data);


            JSONObject obj = new JSONObject(data);
            JSONArray mv_db_arr = obj.getJSONArray("mv_db");
            JSONArray mv_db_arr2 = mv_db_arr.getJSONArray(0);

            for(int i=0; i<mv_db_arr2.length(); i++) {

                JSONObject item = (JSONObject) mv_db_arr2.get(i);
                Iterator<String> keys = item.keys();

                String category = keys.next();
                if(category.equals("product")){

                    String category_val = item.optString(category);

                    System.out.println("---------category");
                    System.out.println(category);
                    System.out.println("---------category_val");
                    System.out.println(category_val);

                    output_arr.put(category);
                }


            }

            System.out.println("=============== output_arr ===============");
            System.out.println(output_arr);



//            for(int i =0 ;i <JA.length(); i++){
//                JSONObject JO = (JSONObject) JA.get(i);
//                singleParsed =  "Name:" + JO.get("device") + "\n"+
//                        "Password:" + JO.get("password") + "\n"+
//                        "Contact:" + JO.get("contact") + "\n"+
//                        "Country:" + JO.get("country") + "\n";
//
//                dataParsed = dataParsed + singleParsed +"\n" ;
//            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output_arr;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        super.onPostExecute(result);
//        MainActivity.data.setText(this.dataParsed);
//        MainActivity.data = (this.data);
    }
}