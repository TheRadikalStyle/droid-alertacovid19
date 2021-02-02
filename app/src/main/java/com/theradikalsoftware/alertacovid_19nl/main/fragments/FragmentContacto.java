package com.theradikalsoftware.alertacovid_19nl.main.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theradikalsoftware.alertacovid_19nl.R;
import com.theradikalsoftware.alertacovid_19nl.Tools;

import java.util.Calendar;

import io.intercom.android.sdk.Intercom;

public class FragmentContacto extends Fragment {
    int requestCodeCall = 2;
    int counter = 0;
    boolean isEEggAlreadyShow = false;

    public FragmentContacto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentContacto.
     */
    public static FragmentContacto newInstance(String param1, String param2) {
        FragmentContacto fragment = new FragmentContacto();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_contacto, container, false);

        LinearLayout linearLayoutFb = rootview.findViewById(R.id.contacto_cardview_llayout_fb);
        linearLayoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" +
                            "https://www.facebook.com/SecretariaSaludNL/"));
                    startActivity(followIntent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SecretariaSaludNL")));
                    String errorMessage = (e.getMessage() == null) ? "Message is empty" : e.getMessage();
                }

            }
        });
        LinearLayout linearLayoutWebpage = rootview.findViewById(R.id.contacto_cardview_llayout_webpage);
        linearLayoutWebpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.contacto_cardview1_url_webpage)));
                if(Tools.getInstance().isIntentSafe(getActivity(), intent)){
                    startActivity(intent);
                }
            }
        });

        CardView llamar070 = rootview.findViewById(R.id.contacto_cardview2);
        llamar070.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallNumber();
            }
        });

        TextView versionTxt = rootview.findViewById(R.id.contacto_textview_cardview3_appversion);
        versionTxt.setText(GetAppVersion());
        versionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter >= 7){
                    if(!isEEggAlreadyShow){
                        Calendar cal = Calendar.getInstance();
                        int hour = cal.get(Calendar.HOUR);
                        int minute = cal.get(Calendar.MINUTE);
                        if(hour == 3 && minute <= 58){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Dev by:")
                                    .setMessage( "David Ochoa\nCopyright 2020\nMonterrey, Nuevo León, México")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }else{
                            Toast.makeText(getActivity(), "" + ("\ud83d\ude03"), Toast.LENGTH_SHORT).show(); //Show :D emoji
                        }
                        isEEggAlreadyShow = true;
                    }
                    counter = 0;
                }
            }
        });

        Intercom.client().setLauncherVisibility(Intercom.VISIBLE);

        return rootview;
    }

    private void CallNumber(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.CALL_PHONE }, requestCodeCall);
        }else{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(getResources().getString(R.string.contacto_call_number)));
            startActivity(callIntent);
        }
    }

    private String GetAppVersion(){
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "N/A";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("onPermissions: ", String.valueOf(requestCode));
        Log.d("onPermissions: ", String.valueOf(grantResults));
        if(requestCode == requestCodeCall){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallNumber();
            }
        }
    }
}
