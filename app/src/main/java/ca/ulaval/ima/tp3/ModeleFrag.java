package ca.ulaval.ima.tp3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModeleFrag extends Fragment {
    private int marqueId;
    private String marqueName;
    private Marques marque;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO les pages ici
        Bundle bundle = this.getArguments();
        marque = bundle.getParcelable("mrq");
        this.marqueId = marque.getId() + 1;
        this.marqueName = marque.getName();
        final View rootView = inflater.inflate(R.layout.liste_modele, container, false);
        final ListView marques = rootView.findViewById(R.id.list_modele);
        final ArrayList<String> responseData = new ArrayList<String>();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://159.203.33.206/api/v1/brand/" + marqueId + "/models").newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .header("accept","application/json")
                .url(url)
                .build();


        TextView tw = (TextView) rootView.findViewById(R.id.textViewAPImodele);
        tw.setText(marqueName);

final String str = marqueName;
        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(Call call, final Response response) throws IOException {
                                                if (!response.isSuccessful()) {
                                                    throw new IOException("Unexpected code " + response);
                                                }
                                                final String tmp = str;
                                                try {
                                                    JSONObject jObject = new JSONObject(response.body().string());
                                                    JSONArray jsonArray = jObject.getJSONArray("content");
                                                    if (jsonArray != null) {
                                                        int len = jsonArray.length();
                                                        for (int i = 0; i < len; i++) {
                                                            Log.d("Marques", jsonArray.getJSONObject(i).getJSONObject("brand").getString("name"));
                                                            Log.d("Model = ",(jsonArray.getJSONObject(i)).getString("name"));
                                                            responseData.add(jsonArray.getJSONObject(i).getString("name"));
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
        );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.item_modele, responseData);

        marques.setAdapter(adapter);
        marques.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View rootView, int position, long id) {
                Object item = marques.getItemAtPosition(position);
            }
        });
//        new Marques(rootView).execute("http://159.203.33.206/api/v1/brand/");
        return rootView;
    }
}
