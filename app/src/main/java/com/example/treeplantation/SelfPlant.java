package com.example.treeplantation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.location.Geocoder;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelfPlant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfPlant extends Fragment {

    EditText country_user,state_user,radius_user,address_user;
    Button plantnow;
    private static final String BASE_URL = "https://tree-plantation-delta.vercel.app/";
    private SharedPreferences sharedPreferences;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public SelfPlant() {
        // Required empty public constructor
    }
    public static SelfPlant newInstance(String param1, String param2) {
        SelfPlant fragment = new SelfPlant();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_plant, container, false);
        fetchNearbyPlaces("india", "maharashtra", 200, 19.9975, 73.7898);
        return view;
    }

    private void fetchNearbyPlaces(String country, String state, int radius, double latitude, double longitude) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String idToken = sharedPreferences.getString("IDToken", null);

        if (idToken == null) {
            Toast.makeText(getContext(), "ID Token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://tree-plantation-delta.vercel.app/common/getNearbyPlaces").newBuilder();
        urlBuilder.addQueryParameter("country", country);
        urlBuilder.addQueryParameter("state", state);
        urlBuilder.addQueryParameter("radius", String.valueOf(radius));
        urlBuilder.addQueryParameter("lat", String.valueOf(latitude));
        urlBuilder.addQueryParameter("lon", String.valueOf(longitude));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer "+idToken)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Unexpected code " + response, Toast.LENGTH_SHORT).show());
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    Intent intent = new Intent(getContext(),NearbyPlace.class);
                    intent.putExtra("responseData",responseData);
                    startActivity(intent);

                } catch (JSONException e) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to parse response", Toast.LENGTH_SHORT).show());
                }

            }
        });
    }
}