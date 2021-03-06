package com.theradikalsoftware.alertacovid_19nl.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theradikalsoftware.alertacovid_19nl.main.fragments.FragmentContacto;
import com.theradikalsoftware.alertacovid_19nl.main.fragments.FragmentCuestionario;
import com.theradikalsoftware.alertacovid_19nl.main.fragments.FragmentMap;
import com.theradikalsoftware.alertacovid_19nl.R;
import com.theradikalsoftware.alertacovid_19nl.Tools;
import com.theradikalsoftware.alertacovid_19nl.retrofit.models.CasosModel;
import com.theradikalsoftware.alertacovid_19nl.retrofit.models.InsideJSONModel;
import com.theradikalsoftware.alertacovid_19nl.retrofit.services.CasosService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import io.intercom.android.sdk.Intercom;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements FragmentMap.OnFragmentMapInteractionListener {
    List<InsideJSONModel> data;
    int chatButtonPaddingBottom = 0;
    final String TAG_MAP = "FRAG_MAP";
    final String TAG_CUESTIONARIO = "FRAG_CUESTIONARIO";
    final String TAG_CONTACTO = "FRAG_CONTACTO";
    String lastmodify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetJSONCasos();
        //GetJSONHeatmap();

        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottomnavigation_mapa:
                        FragmentMap map = FragmentMap.newInstance();
                        Bundle bundle =  new Bundle();
                        bundle.putString("bLastModify", lastmodify);
                        bundle.putParcelableArrayList("bData", (ArrayList<? extends Parcelable>) data);

                        FragmentChanger(map, TAG_MAP, bundle);
                        break;
                    case R.id.bottomnavigation_cuestionario:
                        FragmentChanger(new FragmentCuestionario(), TAG_CUESTIONARIO, null);
                        break;
                    case R.id.bottomnavigation_contacto:
                        FragmentChanger(new FragmentContacto(), TAG_CONTACTO, null);
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

        //FragmentChanger(new FragmentMap(), TAG_MAP, null);
        //navView.setSelectedItemId(R.id.bottomnavigation_mapa);
    }

    public void FragmentChanger(Fragment fragment, String TAG, @Nullable Bundle variables){
        if(!TAG.equals(TAG_CONTACTO)){
            if(InternetChecker()){
                if(variables != null)
                    fragment.setArguments(variables);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_services))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CasosService service = retrofit.create(CasosService.class);

        Call<CasosModel> casos = service.getAllCasos();
        casos.enqueue(new Callback<CasosModel>() {
            @Override
            public void onResponse(Call<CasosModel> call, retrofit2.Response<CasosModel> response) {
                Log.d("Retro ->", "Exito");

                CasosModel casos = response.body();
                lastmodify = casos.getLastmodify();
                data = casos.getData();

                FragmentMap map = FragmentMap.newInstance();
                Bundle bundle =  new Bundle();
                bundle.putString("bLastModify", lastmodify);
                bundle.putParcelableArrayList("bData", (ArrayList<? extends Parcelable>) data);

                FragmentChanger(map, TAG_MAP, bundle);
            }

            @Override
            public void onFailure(Call<CasosModel> call, Throwable t) {
                Log.d("Retro ->", "Fail");
            }
        });

        /*RequestQueue volleyRequest = Volley.newRequestQueue(this);
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

        volleyRequest.add(request);*/
    }

    @Override
    public void onFragmentMapInteraction(boolean needsRefresh) {

    }

    /*private void GetJSONHeatmap(){
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
    }*/

    /*@Override
    public void onFragmentMapInteraction(boolean needsRefresh) {
        if(needsRefresh){
            if(dataToMap.size() > 0)
                dataToMap.clear();
            if(heatmapDataToMap.size() > 0)
                heatmapDataToMap.clear();
            GetJSONCasos();
            //GetJSONHeatmap();

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
     //   }
    //}
}
