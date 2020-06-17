package com.theradikalsoftware.alertacovid_19nl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        CheckGooglePlayServices();
    }


    private void CheckGooglePlayServices(){
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        if(availability.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            availability.getErrorDialog(this, availability.isGooglePlayServicesAvailable(this), 666, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
        }
    }

    /*
    private void GetJSONCasos(){
        RequestQueue volleyRequest = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.url_map_service);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JsonData jData;

                for(int x = 0; x < response.length(); x++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(x);
                        Log.d("JObject", jsonObject.toString());

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

                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                intent.putExtra("arrayData", dataToMap);
                intent.putParcelableArrayListExtra("arrayData", (ArrayList<? extends Parcelable>) dataToMap);
                startActivity(intent);
                finish();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONArray: ", error.toString());
            }
        });

        //6000 ms for timeout
        request.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    new AlertDialog.Builder(Splashscreen.this)
                            .setTitle("Error de conexión")
                            .setMessage("De momento no es posible conectarnos con nuestros servicios, cierra la app e intenta de nuevo")
                            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    ;
                }
            }
        };

        volleyRequest.add(request);
    }*/

}
