package com.example.nation_info_json;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> listContents;
    Button btnFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetData().execute("http://api.geonames.org/countryInfoJSON?username=mobilenationinfo");
        btnFind = findViewById(R.id.btnFind);

    }

    public class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("geonames");
                int length = arr.length();
                listContents = new ArrayList<String>(length);
                for (int i = 0; i < length; i++) {
                    JSONObject country = arr.getJSONObject(i);

                    String x = country.getString("countryName");
                    listContents.add(x);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            PutDataIntoListView(listContents);
        }


    }

    private void PutDataIntoListView(List<String> nationList) {

        ListView myListView = findViewById(R.id.listViewCountries);
        myListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nationList));


    }

    public void ButtonClick(View v) {
        Intent intent = new Intent(this, NationInfo.class);
        startActivity(intent);
    }


}