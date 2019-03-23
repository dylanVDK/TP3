package ca.ulaval.ima.tp3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

public class OffreFrag extends Fragment {
    FragmentManager fm;

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO les pages ici
        final View rootView = inflater.inflate(R.layout.liste_marques, container, false);
        final ListView marques = rootView.findViewById(R.id.list_marques);
        final ArrayList<String> responseData = new ArrayList<String>();
        setFm(getChildFragmentManager());
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://159.203.33.206/api/v1/brand").newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("accept","application/json")
                .url(url)
                .build();


        TextView tw = (TextView) rootView.findViewById(R.id.textViewAPI);
        tw.setText("Marques");

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
                                                try {
//                                                    TextView tw = (TextView) rootView.findViewById(R.id.textViewAPI);
//                                                    tw.setText(response.body().string());
                                                        JSONObject jObject = new JSONObject(response.body().string());
                                                        JSONArray jsonArray = jObject.getJSONArray("content");

                                                        if (jsonArray != null) {
                                                            int len = jsonArray.length();
                                                            for (int i = 0; i < len; i++) {
                                                                responseData.add(jsonArray.getJSONObject(i).getString("name"));
                                                            }
                                                        }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                }
                                            }
                                        );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.item_list, responseData);
        marques.setAdapter(adapter);

        marques.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View rootView, int position, long id) {
                Object item = marques.getItemAtPosition(position);
                Marques mrq = new Marques(item.toString(), position);
                ModeleFrag mf = new ModeleFrag();
                Bundle bundle = new Bundle();

                bundle.putParcelable("mrq", mrq);
                mf.setArguments(bundle);
                FragmentTransaction ft = getFm().beginTransaction().add(R.id.constraintLayout,mf);
                ft.addToBackStack(null).show(mf).commit();
                Log.d("item clicked",item.toString());
            }
        });
//        new Marques(rootView).execute("http://159.203.33.206/api/v1/brand/");
        return rootView;
    }
}
