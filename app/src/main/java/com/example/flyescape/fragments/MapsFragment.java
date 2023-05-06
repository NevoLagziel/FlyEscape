package com.example.flyescape.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flyescape.App;
import com.example.flyescape.R;
import com.example.flyescape.model.Score;
import com.example.flyescape.utilities.DataManager;
import com.example.flyescape.utilities.MySPv3;
import com.example.flyescape.utilities.SignalGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private GoogleMap gMap;

    private ArrayList<Marker> markers;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            String json = MySPv3.getInstance().getString(App.KEY,"");
            DataManager dataManager = new Gson().fromJson(json,DataManager.class);
            ArrayList<Score> scores = dataManager.getScores();
            markers = new ArrayList<>();
            if(scores == null)
                return;
            for(int i=0;i<scores.size();i++){
                LatLng location = new LatLng(scores.get(i).getX(), scores.get(i).getY());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title(scores.get(i).getPlace() + " - "+scores.get(i).getName().toUpperCase()+", Score : "+scores.get(i).getScore());
                Marker marker = googleMap.addMarker(markerOptions);
                markers.add(marker);
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    public void zoomOnRecord(int rank, double x, double y) {
        SignalGenerator.getInstance().vibrate(100);

        LatLng point = new LatLng(x, y);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        markers.get(rank-1).showInfoWindow();

    }
}
