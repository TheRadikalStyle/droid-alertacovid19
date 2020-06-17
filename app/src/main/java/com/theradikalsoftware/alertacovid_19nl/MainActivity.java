package com.theradikalsoftware.alertacovid_19nl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.intercom.android.sdk.Intercom;

public class MainActivity extends FragmentActivity implements FragmentMap.OnFragmentMapInteractionListener {
    ArrayList<JsonData> dataToMap = new ArrayList<>();
    ArrayList<LocationsData> heatmapDataToMap = new ArrayList<>();
    int chatButtonPaddingBottom = 0;
    final String TAG_MAP = "FRAG_MAP";
    final String TAG_CUESTIONARIO = "FRAG_CUESTIONARIO";
    final String TAG_CONTACTO = "FRAG_CONTACTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetJSONCasos();
        GetJSONHeatmap();

        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottomnavigation_mapa:
                        FragmentMap fragmentMap = new FragmentMap();
                        fragmentMap.data = dataToMap;
                        fragmentMap.heatData = heatmapDataToMap;
                        FragmentChanger(fragmentMap, TAG_MAP);
                        break;
                    case R.id.bottomnavigation_cuestionario:
                        FragmentChanger(new FragmentCuestionario(), TAG_CUESTIONARIO);
                        break;
                    case R.id.bottomnavigation_contacto:
                        FragmentChanger(new FragmentContacto(), TAG_CONTACTO);
                        break;
                }
                return true;
            }
        });
        navView.post(new Runnable() {
            @Override
            public void run() {
                chatButtonPaddingBottom = navView.getMeasuredHeight();
                ConfigureChat();
            }
        });

        FragmentChanger(new FragmentMap(), TAG_MAP);
        navView.setSelectedItemId(R.id.bottomnavigation_mapa);
    }

    public void FragmentChanger(Fragment fragment, String TAG){
        if(!TAG.equals(TAG_CONTACTO)){
            if(InternetChecker()){
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment, TAG);
                fragmentTransaction.commit();
            }
        }else{
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment, TAG);
            fragmentTransaction.commit();
        }
    }

    private boolean InternetChecker(){
        if(Tools.getInstance().isDeviceOnline(this)){
            return true;
        }else{
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.main_activity_dialog_nointernet_title))
                    .setMessage(getResources().getString(R.string.main_activity_dialog_nointernet_message))
                    .setPositiveButton(getResources().getString(R.string.main_activity_dialog_nointernet_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }
    }

    private void ConfigureChat(){
        Intercom.initialize(this.getApplication(), getResources().getString(R.string.intercom_apikey), getResources().getString(R.string.intercom_appid));
        Intercom.client().setBottomPadding(chatButtonPaddingBottom);
        Intercom.client().registerUnidentifiedUser();
        Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE);
    }

    private void GetJSONCasos(){
        RequestQueue volleyRequest = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.url_map_service);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JsonData jData;
                Log.d("JsonData: ", "Alcanzado");

                for(int x = 0; x < response.length(); x++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(x);
                        //Log.d("JObject", jsonObject.toString());

                        jData = new JsonData();

                        if(x == 0){
                            jData.lastModify = jsonObject.getString("lastmodify");
                        }else{
                            jData.municipio = jsonObject.getString("municipio");
                            jData.casosConfirmados = jsonObject.getString("casosconfirmados");
                            jData.ubicacion = jsonObject.getString("latlong");
                        }

                        dataToMap.add(jData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray: ", error.toString());
            }
        });

        volleyRequest.add(request);
    }

    private void GetJSONHeatmap(){
        RequestQueue volleyRequest = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.url_heatmap_service);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                LocationsData jLocData;

                Log.d("JsonDataHeat: ", "Alcanzado");
                for(int x = 0; x < response.length(); x++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(x);
                        //Log.d("JObject", jsonObject.toString());

                        jLocData = new LocationsData();

                        if (x != 0) {
                            String location[];
                            location = jsonObject.getString("latlong").split(",");
                            jLocData.latitude = Double.parseDouble(location[0]);
                            jLocData.longitude = Double.parseDouble(location[1]);
                            heatmapDataToMap.add(jLocData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray: ", error.toString());
            }
        });

        volleyRequest.add(request);
    }

    @Override
    public void onFragmentMapInteraction(boolean needsRefresh) {
        if(needsRefresh){
            if(dataToMap.size() > 0)
                dataToMap.clear();
            if(heatmapDataToMap.size() > 0)
                heatmapDataToMap.clear();
            GetJSONCasos();
            GetJSONHeatmap();

            FragmentMap fragmentMap = new FragmentMap();
            fragmentMap.data = dataToMap;
            fragmentMap.heatData = heatmapDataToMap;
            FragmentChanger(fragmentMap, TAG_MAP);
            /*if(dataToMap.size() > 0){
                FragmentMap fragmentMap = new FragmentMap();
                fragmentMap.data = dataToMap;
                fragmentMap.heatData = heatmapDataToMap;
                FragmentChanger(fragmentMap, TAG_MAP);
            }else{
                Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
