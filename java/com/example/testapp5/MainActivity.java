package com.example.testapp5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<Items> list;
    private ItemAdpter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();
        // getDataFromJson();
        // new GetDataFromApi().execute("https://jsonplaceholder.typicode.com/posts");
        new GetDataFromApi().execute("https://jsonplaceholder.typicode.com/users");
    }

    private void getDataFromJson() {
        JSONArray array = null;
        try {
            array = new JSONArray(Utils.data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                Items items = new Items();
                // items.setmTitle(row.getString("title"));
                //  items.setmBody(row.getString("body"));
                list.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adpter.notifyDataSetChanged();
    }

    private void initRecyclerView() {

        list = new ArrayList<>();
        adpter = new ItemAdpter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adpter);
    }

    public class GetDataFromApi extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            StringBuilder builder = null;
            try {
                URL url = new URL(strings[0]);
                Log.d("tag", "doInBackground: " + strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray array = null;
            try {
                array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject elerow = array.getJSONObject(i);
                    Items items = new Items();
                    items.setName(elerow.getString("name"));
                    items.setEmail(elerow.getString("email"));
                    items.setPhone(elerow.getString("phone"));
                    items.setWebsite(elerow.getString("website"));


                    JSONObject eleCompany = elerow.getJSONObject("company");
                    Items.CompanyBean companyBean = new Items.CompanyBean();
                    companyBean.setName(eleCompany.getString("name"));
                    companyBean.setCatchPhrase(eleCompany.getString("catchPhrase"));
                    companyBean.setBs(eleCompany.getString("bs"));
                    items.setCompany(companyBean);


                    JSONObject eleAddress = elerow.getJSONObject("address");
                    Items.AddressBean addressBean = new Items.AddressBean();
                    addressBean.setStreet(eleAddress.getString("street"));
                    addressBean.setSuite(eleAddress.getString("suite"));
                    addressBean.setCity(eleAddress.getString("city"));
                    addressBean.setZipcode(eleAddress.getString("zipcode"));


                    JSONObject eleGeo = eleAddress.getJSONObject("geo");
                    Items.AddressBean.GeoBean geoBean = new Items.AddressBean.GeoBean();
                    geoBean.setLat(eleGeo.getString("lat"));
                    geoBean.setLng(eleGeo.getString("lng"));

                    addressBean.setGeo(geoBean);

                    items.setAddress(addressBean);

                    list.add(items);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adpter.notifyDataSetChanged();

        }
    }

}
