package com.example.eventapp.ui.map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.eventapp.Event;
import com.example.eventapp.EventOnMap;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.ui.home.HomeFragment;
import com.example.eventapp.ui.home.HomeViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private DashboardViewModel dashboardViewModel;
    private GoogleMap mMap;
    private List<LatLng> places;
    private List<Event> events;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);




        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng camera = DashboardViewModel.getCamera(getContext());
                List<EventOnMap >list_eom = DashboardViewModel.getCoords(HomeFragment.current_list,getContext());
                for(EventOnMap c : list_eom){
                    double latitude = c.getX();
                    double longitude = c.getY();
                    // Add a marker in Sydney and move the camera
                    if(latitude != 0.0 || longitude != 0.0){
                        LatLng marker = new LatLng(	latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(marker).title(c.getId()));
                    }


                }



                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.hideInfoWindow();
                        final Bundle data = new Bundle();
                        data.putString("id", marker.getTitle());
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.event, data);
                        return false;
                    }
                });
/*
                places.add(new LatLng(55.754724, 37.621380));
                places.add(new LatLng(55.760133, 37.618697));
                places.add(new LatLng(55.764753, 37.591313));
                places.add(new LatLng(55.728466, 37.604155));

                MarkerOptions[] markers = new MarkerOptions[places.size()];
                for (int i = 0; i < places.size(); i++) {
                    markers[i] = new MarkerOptions()
                            .position(places.get(i));
                    mMap.addMarker(markers[i]);
                }
 */

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(camera)
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            }
        });
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        places = new ArrayList<>();

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(55.754724, 37.621380);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));



        places.add(new LatLng(55.754724, 37.621380));
        places.add(new LatLng(55.760133, 37.618697));
        places.add(new LatLng(55.764753, 37.591313));
        places.add(new LatLng(55.728466, 37.604155));

        MarkerOptions[] markers = new MarkerOptions[places.size()];
        for (int i = 0; i < places.size(); i++) {
            markers[i] = new MarkerOptions()
                    .position(places.get(i));
            mMap.addMarker(markers[i]);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}