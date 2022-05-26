package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    TextView tv_CerrarSesion;
    LinearLayout v_Asincronos,v_Sincronos;
    ImageView iv_Perfil;

    String nombre;
    int monedas=0, elo=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MyPreference", MODE_PRIVATE);
        nombre = prefs.getString("Usuario", "Iván");

        iv_Perfil = findViewById(R.id.IV_Perfil);
        tv_CerrarSesion = findViewById(R.id.tv_Cerrar_Sesion);
        v_Asincronos = findViewById(R.id.v_Asincronos);







        iv_Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perfil = new Intent(MainActivity.this, Perfil.class);
                startActivity(perfil);
            }
        });
        tv_CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCerrarSesion();
            }
        });




        v_Asincronos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent asincronos = new Intent(MainActivity.this, Jugar.class);
                startActivity(asincronos);
            }
        });
    }


    void postCerrarSesion(){
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#10032e"));
        pDialog.setTitleText("Cerrando sesión");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://queenchess-backend.herokuapp.com/account/logout";
        JSONObject jsonBody = new JSONObject();
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Caso correcto
                pDialog.cancel();

                Intent inisesion = new Intent(MainActivity.this, Login.class);
                startActivity(inisesion);
                finish();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                String responseBody = null;
                try {
                    responseBody = new String(error.networkResponse.data, "utf-8");
                    System.out.println(responseBody);

                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Error de cookies")
                            .setConfirmText("Vale")
                            .show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return com.android.volley.Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }

}