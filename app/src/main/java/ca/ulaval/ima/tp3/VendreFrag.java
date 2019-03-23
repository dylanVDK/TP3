package ca.ulaval.ima.tp3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

public class VendreFrag extends Fragment {
    private static final String TAG = "formFrag";
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View rootView =
                inflater.inflate(R.layout.form_fragment, parent, false);


        String[] arraySpinner = new String[] {
                "AT", "MA", "RB"
        };
        Spinner s = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        btn = (Button) rootView.findViewById(R.id.SubmitButton);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(rootView.getContext());
                dialog.setContentView(R.layout.login_frag);
                dialog.setTitle("Login");
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOk);
                // if button is clicked, close the custom dialog
                final                EditText mailField = (EditText) dialog.findViewById(R.id.mail);
                final              EditText niField = (EditText) dialog.findViewById(R.id.ni);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("email",  mailField.getText().toString());
                        String email = mailField.getText().toString();
                        String ni = niField.getText().toString();

                        getToken(email,ni, rootView);

                        dialog.dismiss();
                    }
                });
//                if (postOffre(true, rootView) == -1){
//                }
                dialog.show();
            }
        });
        return rootView;
    }

    public String getToken(String email, String ni, final View rootView){

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

                    postOffre(true,rootView, jObject.getJSONObject("content").getString("token"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return "ok";
    }

    public int postOffre(boolean owner, View rootView, String token){
        EditText kmField = (EditText) rootView.findViewById(R.id.km);
        EditText yearField = (EditText) rootView.findViewById(R.id.year);
        EditText priceField = (EditText) rootView.findViewById(R.id.price);
        Spinner trField = (Spinner) rootView.findViewById(R.id.spinner);
        EditText modelField = (EditText) rootView.findViewById(R.id.model);

        final String km2 = kmField.getText().toString();

        final String year2 = yearField.getText().toString();
        final String price2 = priceField.getText().toString();
        final String tr2 = "" + trField.getSelectedItem().toString();
        final String model2 = modelField.getText().toString();
        Log.d("token",token);
        OffrePost ofr = new OffrePost(km2,year2,price2,tr2,model2);
        int km = ofr.getKm();
        int year = ofr.getYear();
        long price= ofr.getPrice();
        String tr = ofr.getTr();
        int model = ofr.getModel();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://159.203.33.206/api/v1/offer/add/").newBuilder();
        String url = urlBuilder.build().toString();

if (
        ((km >= 0) && (km <= 200000000)) &&
                ((year >= 1000) && (year <= 3000)) &&
                ((price >= 1) && (price <= 200000000))
){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("from_owner", owner);
            postdata.put("kilometers", km);
            postdata.put("year", year);
            postdata.put("price", price);
            postdata.put("transmission", tr);
            postdata.put("model", model);
        } catch(JSONException e){
            e.printStackTrace();
        }
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .header("Accept","application/json")
                .header("Content-Type", "application/json")
                .header("Authorization","Basic :" + token)
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

                Log.d("post offre",response.body().toString());

            }
        });
        return 0;
    }
    else
{ return -1;}
    }


  }
