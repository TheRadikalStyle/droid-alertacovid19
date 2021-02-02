package com.theradikalsoftware.alertacovid_19nl.main.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.theradikalsoftware.alertacovid_19nl.R;

import io.intercom.android.sdk.Intercom;


public class FragmentCuestionario extends Fragment {
    public FragmentCuestionario() {
        // Required empty public constructor
    }

    public static FragmentCuestionario newInstance(String param1, String param2) {
        FragmentCuestionario fragment = new FragmentCuestionario();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cuestionarioView = inflater.inflate(R.layout.fragment_cuestionario, container, false);

        WebView webview = cuestionarioView.findViewById(R.id.fragment_cuestionario_webview);
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(request.equals("this")){
                    //DO something
                }
                return true;
            }
        };

        webview.setWebViewClient(webViewClient);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(getResources().getString(R.string.url_cuestionario));

        Intercom.client().setLauncherVisibility(Intercom.VISIBLE);

        return cuestionarioView;
    }
}
