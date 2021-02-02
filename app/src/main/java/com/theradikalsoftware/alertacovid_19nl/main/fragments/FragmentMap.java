package com.theradikalsoftware.alertacovid_19nl.main.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.theradikalsoftware.alertacovid_19nl.main.models.LocationsData;
import com.theradikalsoftware.alertacovid_19nl.R;
import com.theradikalsoftware.alertacovid_19nl.main.recycler.MyAdapter;
import com.theradikalsoftware.alertacovid_19nl.retrofit.models.InsideJSONModel;

import java.util.ArrayList;
import java.util.List;

import io.intercom.android.sdk.Intercom;

public class FragmentMap extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    TextView lastModifyTXV;
    LinearLayout bottomsheet;
    CardView cardviewLastModify;
    RecyclerView recyclerviewDataDetail;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter mAdapter;
    Button refreshData;
    TextView totalesConfirmados;


    String lastModify;
    List<InsideJSONModel> data;

    private OnFragmentMapInteractionListener mCallback;


    public FragmentMap() {
        // Required empty public constructor
    }

    public static FragmentMap newInstance() {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lastModify = getArguments().getString("bLastModify");
            data = getArguments().getParcelableArrayList("bData");
        }

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = rootView.findViewById(R.id.fragment_map_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        lastModifyTXV = rootView.findViewById(R.id.fragment_map_textview_text_lastmodif);
        cardviewLastModify = rootView.findViewById(R.id.fragment_map_cardview_container_lastmodif);
        totalesConfirmados = rootView.findViewById(R.id.fragment_map_textview_casosconfirmados_totales);

        refreshData = rootView.findViewById(R.id.fragment_map_refreshdata);
        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mCallback.onFragmentMapInteraction(true);
            }
        });

        recyclerviewDataDetail = rootView.findViewById(R.id.fragment_map_recyclerview_datadetail);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerviewDataDetail.setLayoutManager(layoutManager);
        recyclerviewDataDetail.setHasFixedSize(true);

        bottomsheet = rootView.findViewById(R.id.fragment_map_linearlayout_bottomsheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
                }else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    Intercom.client().setLauncherVisibility(Intercom.Visibility.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentMapInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onResume();
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.6915233,-100.3192888), 12.0f));
        googleMap.setMinZoomPreference(7.0f);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if(data != null){
            if(data.size() == 0){
                refreshData.setVisibility(View.VISIBLE);
            }else{
                DrawMarkers(data, googleMap);
                SetResumenOnBottomSheet(data);
            }
        }else{ refreshData.setVisibility(View.VISIBLE); }
    }

    private void DrawMarkers(List<InsideJSONModel> data, GoogleMap googleMap) {
        ArrayList<LocationsData> heatData = new ArrayList<>();
        LocationsData locData;

        if(lastModify != null)
            lastModifyTXV.setText(lastModify);

        for (InsideJSONModel inside : data) {
            googleMap.addMarker(new MarkerOptions()
                    .position(inside.getLatLongObject())
                    .title(inside.getMunicipio())
                    .snippet(String.format(String.format(getResources().getString(R.string.fragment_map_markersnippet_confirmados),  inside.getCasosconfirmados())))
            );

            locData = new LocationsData();
            locData.setLatitude(inside.getLatitudeDouble());
            locData.setLongitude(inside.getLongitudeDouble());
            heatData.add(locData);
        }

        if(heatData.size() > 0)
            DrawHeatMap(heatData, googleMap);
    }

    private void DrawHeatMap(ArrayList<LocationsData> location, GoogleMap googleMap) {
        HeatmapTileProvider mProvider;
        TileOverlay mOverlay;
        List<LatLng> list = new ArrayList<>();

        for(int c = 0; c < location.size(); c++){
            LatLng latLng = new LatLng(location.get(c).getLatitude(), location.get(c).getLongitude());
            list.add(latLng);
        }

        for(int v = 0; v < list.size(); v++){
            Log.d("List", list.get(0).toString());
        }

            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .radius(50)
                    .opacity(0.9)
                    .build();
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private void SetResumenOnBottomSheet(List<InsideJSONModel> data) {
        mAdapter = new MyAdapter(data);
        recyclerviewDataDetail.setAdapter(mAdapter);

        int confirmadosTotales = 0;

        for (InsideJSONModel inside: data) {
            confirmadosTotales = confirmadosTotales + inside.getCasosconfirmados();
        }
        totalesConfirmados.setText(String.format(getResources().getString(R.string.fragment_map_bottomsheet_totales), confirmadosTotales));
    }

    public interface OnFragmentMapInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentMapInteraction(boolean needsRefresh);
    }
}