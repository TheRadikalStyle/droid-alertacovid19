package com.theradikalsoftware.alertacovid_19nl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.theradikalsoftware.alertacovid_19nl.main.MainActivity;

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
}
