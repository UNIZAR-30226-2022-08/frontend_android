package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Login extends AppCompatActivity {
    TextView tv_Registarse, tv_IniciarSesion;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    EditText ed_Usuario,ed_Clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tv_Registarse = findViewById(R.id.tv_Registrar);
        tv_IniciarSesion = findViewById(R.id.tv_IniciarSesion);

        ed_Usuario = findViewById(R.id.ed_Usuario);
        ed_Clave = findViewById(R.id.ed_Clave);

        tv_Registarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inisesion = new Intent(Login.this, Registro.class);
                startActivity(inisesion);
                //finish();
            }
        });

        tv_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postIniciarSesion();

                SharedPreferences.Editor editor = getSharedPreferences("BiblioChill", MODE_PRIVATE).edit();
                editor.putString("Usuario", "Iván");
                editor.putString("Correo", ed_Usuario.getText().toString());
                editor.apply();


            }
        });
    }




    void postIniciarSesion(){
        SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#10032e"));
        pDialog.setTitleText("Iniciando sesión");
        pDialog.setCancelable(false);
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://queenchess-backend.herokuapp.com/account/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", ed_Usuario.getText().toString());
            jsonBody.put("password", ed_Clave.getText().toString());
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Caso correcto
                    pDialog.cancel();

                    Intent inisesion = new Intent(Login.this, MainActivity.class);
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

                        if(responseBody.contains("User not found")){
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("No hay una cuenta asociada a ese correo")
                                    .setConfirmText("Vale")
                                    .show();
                        }
                        else if(responseBody.contains("Invalid password")){
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("La contraseña introducida es incorrecta")
                                    .setConfirmText("Vale")
                                    .show();
                        }
                        else if(responseBody.contains("error")){
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Algo está fallando")
                                    .setConfirmText("Vale")
                                    .show();
                        }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}