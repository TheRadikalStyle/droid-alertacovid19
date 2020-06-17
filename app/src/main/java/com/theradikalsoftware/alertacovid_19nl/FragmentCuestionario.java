package com.theradikalsoftware.alertacovid_19nl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.intercom.android.sdk.Intercom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCuestionario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCuestionario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    public FragmentCuestionario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCuestionario.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCuestionario newInstance(String param1, String param2) {
        FragmentCuestionario fragment = new FragmentCuestionario();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args); */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
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
