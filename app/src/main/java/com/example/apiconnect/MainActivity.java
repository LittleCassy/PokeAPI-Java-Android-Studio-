package com.example.apiconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView myIV;
    Button send,fav;
    TextView name;
    EditText inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        myIV = findViewById(R.id.image);
        send = findViewById(R.id.send);
        fav = findViewById(R.id.fav);
        name = findViewById(R.id.nameTF);
        inputName = findViewById(R.id.input);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText().equals("")){
                    Toast.makeText(MainActivity.this, "Rellena el campo para buscar", Toast.LENGTH_SHORT).show();
                }else{
                    APICall(inputName.getText().toString());
                }
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().equals("")){
                    Toast.makeText(MainActivity.this, "Rellena dar like", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        POSTCall();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void APICall(String input){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://pokeapi.co/api/v2/pokemon/"+input.toLowerCase();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            name.setText(jObj.get("name").toString());
                            myIV.setImageBitmap(getBitmapFromURL(jObj.getJSONObject("sprites").get("front_default").toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    protected void POSTCall() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://jsonplaceholder.typicode.com/posts";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id", 1);
        jsonBody.put("title", name.getText().toString());
        jsonBody.put("body", "Liked!");
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Liked!", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}