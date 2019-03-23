package ca.ulaval.ima.tp3;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnnoncesFrag extends Fragment {
    public View rootView;

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.liste_mes_offres, container, false));
        TextView tw = (TextView) getRootView().findViewById(R.id.titleOffre);
        tw.setText("Mes Offres");
        final Dialog dialog = new Dialog(getRootView().getContext());
        dialog.setContentView(R.layout.login_frag);
        dialog.setTitle("Login");
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOk);

        final EditText mailField = (EditText) dialog.findViewById(R.id.mail);
        final EditText niField = (EditText) dialog.findViewById(R.id.ni);
        dialogButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String email = mailField.getText().toString();
                                                String ni = niField.getText().toString();
                                                getToken(email,ni);
                                                dialog.dismiss();
                                            }
                                        });
        dialog.show();
        return getRootView();
    }

    public void getToken(String email, String ni) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://159.203.33.206/api/v1/account/login/").newBuilder();
        String url = urlBuilder.build().toString();


        JSONObject postdata = new JSONObject();
        try {
            postdata.put("email", email);
            postdata.put("identification_number", ni);
        } catch(JSONException e){
            e.printStackTrace();
        }
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .header("Accept","application/json")
                .header("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();

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
                    String str = response.body().string();
                    JSONObject jObject = new JSONObject(str);
                    String token = jObject.getJSONObject("content").getString("token");
                    mesOffres(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void mesOffres(String token){

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://159.203.33.206/api/v1/offer/mine").newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("accept","application/json")
                .header("Authorization","Basic :" + token)
                .url(url)
                .build();

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
//                                                    TextView tw = (TextView) getRootView().findViewById(R.id.textViewAPI);
//                                                    tw.setText(response.body().string());
                                                    ArrayList<String> responseData = new ArrayList<>();
                                                    ListView offres = getRootView().findViewById(R.id.list_marques);
                                                    String str =response.body().string();
                                                    JSONObject jObject = new JSONObject(str);
                                                    JSONArray jsonArray = jObject.getJSONArray("content");
                                                    Log.d("log",jsonArray.getString(0));

                                                    if (jsonArray != null) {
                                                        int len = jsonArray.length();
                                                        for (int i = 0; i < len; i++) {
                                                        String tmp = jsonArray.getJSONObject(i).getJSONObject("model").getString("name");
                                                        Log.d("debugLog",tmp);
                                                            //String str = jsonArray.getJSONObject(i).getJSONArray("model").getJSONArray(1).getJSONObject(1).toString();
                                                            responseData.add(tmp);
                                                        }

                                                        Log.d("data reponse",responseData.get(0));

                                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getRootView().getContext(), R.layout.item_list, responseData);
                                                        offres.setAdapter(adapter);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
//
//                                                offres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                                    public void onItemClick(AdapterView<?> parent, View getRootView(), int position, long id) {
//                                                        Object item = offres.getItemAtPosition(position);
//                                                    }
//                                                });

                                            }
                                        }
        );
    }
}
