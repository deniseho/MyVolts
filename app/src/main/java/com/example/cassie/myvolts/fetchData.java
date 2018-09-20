package com.example.cassie.myvolts;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by deniseho on 19/09/2018.
 */
public class fetchData extends AsyncTask<Void,Void,Void> {
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    @Override
    protected Void doInBackground(Void... voids) {
        try {
//            URL url = new URL("https://api.myjson.com/bins/j5f6b");
            URL url = new URL("https://api.myjson.com/bins/1hcph0");
//            URL url = new URL("https://api.myjson.com/bins/hz4pg");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
//
//    JSONObject JO1 = new JSONObject(data);
//
//            JSONArray JA = new JSONArray(JO1.get("mv_db"));
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
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

//        MainActivity.data.setText(this.dataParsed);
//        MainActivity.data.setText(this.data);
    }
}