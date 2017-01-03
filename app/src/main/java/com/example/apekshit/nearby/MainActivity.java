package com.example.apekshit.nearby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
ListView l;
    ArrayAdapter<String> ad;
    ArrayList<object> al;
    double latitude;
    double longitude;
    String lat,longi;
    final String APIKey="AIzaSyD25GUWpAIuBY2TDACuhTKamB42Wt9fRSw";
    myAdapter myadapter;
    ArrayList<String> name,open,vicinity;
    String fullurl;
    String extra="";
    AppLocationService appLocationService;
    ArrayList<Double> latplace;
    ArrayList<Double> longplace;
ProgressBar pb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appLocationService = new AppLocationService(
                this);
        pb= (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        GPSTracker gps = new GPSTracker(this);
        Intent i=getIntent();
      extra=i.getStringExtra("EXTRA");
       l = (ListView) findViewById(R.id.listView);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent j=new Intent(view.getContext(),MapsActivity.class);
              j.putExtra("LATITUDE",latplace.get(position));
              j.putExtra("LONGITUDE", longplace.get(position));
              j.putExtra("NAME",name.get(position));
                startActivity(j);
            }
        });
        name=new ArrayList<>();
        open=new ArrayList<>();
        vicinity=new ArrayList<>();
        latplace=new ArrayList<>();
        longplace=new ArrayList<>();
        if(gps.canGetLocation()){
           latitude= gps.getLatitude(); // returns latitude
            longitude=gps.getLongitude(); // returns longitude}
            lat = String.valueOf(latitude);
            longi = String.valueOf(longitude);
            Toast.makeText(this,lat+","+longi,Toast.LENGTH_LONG).show();
        }else{

            Location nwLocation = appLocationService
                    .getLocation(LocationManager.NETWORK_PROVIDER);

            if (nwLocation != null) {
                double latitude = nwLocation.getLatitude();
                double longitude = nwLocation.getLongitude();
                Toast.makeText(
                        getApplicationContext(),
                        "Mobile Location (NW): \nLatitude: " + latitude
                                + "\nLongitude: " + longitude,
                        Toast.LENGTH_LONG).show();
                lat = String.valueOf(latitude);
                longi = String.valueOf(longitude);
            } else {
                showSettingsAlert("NETWORK");
            }
        }
            new async().execute();
    }
    /*
    public double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);
        return distance;

    }*/
    String temp;

     class async extends AsyncTask<Object,Void,String> {
        @Override
        protected String doInBackground(Object[] params) {
            fullurl="https://maps.googleapis.com/maps/api/place/search/json?location=" + lat + "," + longi + extra+"&rankby=distance&sensor=true&key=" + APIKey;
            Log.e("EXTRAS",fullurl);

            temp = makecall(fullurl);
            return "";
        }


         @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


         @Override
        protected void onPostExecute(String st) {
             super.onPostExecute(st);
            if (temp == null) {
                Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();
            } else {
                al=(ArrayList)parseGoogleParse(temp);
                if(al.size()==0){
                    Toast.makeText(getApplicationContext(),"No Places found", Toast.LENGTH_LONG).show();
                }
                for(int i=0;i<al.size();i++){
                  latplace.add(al.get(i).getLatitude());
                   longplace.add(al.get(i).getLongitude());
                    Log.e("LATLNG",latplace+","+longplace);
                    name.add(al.get(i).getName());
                    vicinity.add("Vicinity:"+al.get(i).getVicinity());
                    open.add("Open Now:" + al.get(i).getOpenNow());
                    Log.e("NAME",name.get(i));
                 }


                myadapter = new myAdapter(MainActivity.this,name,open,vicinity);
                l.setAdapter(myadapter);
                pb.setVisibility(View.INVISIBLE);

            }
        }
    }
public static String makecall(String url){
int responsecode;
    try {
        URL urlreq = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlreq.openConnection();
        connection.connect();
        responsecode = connection.getResponseCode();
        InputStream is=connection.getInputStream();
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            String reply = sb.toString();
             Log.e("STRING",reply);
            return reply;
        }


        /*
        if (responsecode == HttpURLConnection.HTTP_OK){
            InputStream is=connection.getInputStream();
            Reader r=new InputStreamReader(is);
            int content_length=connection.getContentLength();
            Log.e("ARRAYSIZe",content_length+"");
            int hasread=0;
            char[] array=new char[content_length];
            while(hasread<content_length) {
                hasread+=r.read(array,hasread,content_length-hasread);
            }*/
          /* String replystring=new String(array);
            return replystring;
        }*/
    }
catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
return "";
}

    private static ArrayList<object> parseGoogleParse(final String response){


        ArrayList<object> temp1 = new ArrayList();
        try{
            JSONObject jso=new JSONObject(response);
            if(jso.has("results")){
                JSONArray jsa=jso.getJSONArray("results");
                for(int i=0;i<jsa.length();i++){
                object o=new object();
                 if(jsa.getJSONObject(i).has("name")){
                     o.setName(jsa.getJSONObject(i).getString("name"));
                     o.setVicinity(jsa.getJSONObject(i).getString("vicinity"));
                 }
                   if(jsa.getJSONObject(i).has("geometry")){
                       JSONObject loc=jsa.getJSONObject(i).getJSONObject("geometry");
                       if(loc.has("location")){
                           JSONObject latlng=loc.getJSONObject("location");
                           o.setLatitude(latlng.getDouble("lat"));
                           o.setLongitude(latlng.getDouble("lng"));
                       }


                   }
                    if (jsa.getJSONObject(i).has("opening_hours")) {
                        if (jsa.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                            if (jsa.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                o.setOpenNow("YES");
                            } else {
                                o.setOpenNow("NO");
                            }
                        }
                    } else {
                        o.setOpenNow("Not Known");
                    }
                    if (jsa.getJSONObject(i).has("types")) {
                        JSONArray typesArray = jsa.getJSONObject(i).getJSONArray("types");

                        for (int j = 0; j < typesArray.length(); j++) {
                            o.setCategory(typesArray.getString(j) + ", " + o.getCategory());
                        }
                    }
               temp1.add(o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    return temp1;
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }




}
