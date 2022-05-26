package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
/*import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;*/

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;

public class Registro extends AppCompatActivity {
    TextView tv_CrearCuenta, tv_IniciarSesion;
    EditText ed_Usuario,ed_Clave,ed_Correo;
    /*public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tv_CrearCuenta = findViewById(R.id.tv_CrearCuenta);
        tv_IniciarSesion = findViewById(R.id.tv_IniciarSesion);

        ed_Usuario = findViewById(R.id.ed_Usuario);
        ed_Clave = findViewById(R.id.ed_Clave);
        ed_Correo = findViewById(R.id.ed_Correo);

        CookieManager cookM = new CookieManager();
        CookieManager.setDefault(cookM);

        tv_CrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postRegistro();

            }
        });

        tv_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent inisesion = new Intent(Registro.this, Login.class);
                startActivity(inisesion);*/
                finish();
            }
        });
    }

    void postRegistro(){
        SweetAlertDialog pDialog = new SweetAlertDialog(Registro.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#10032e"));
        pDialog.setTitleText("Creando cuenta");
        pDialog.setCancelable(false);
        pDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://queenchess-backend.herokuapp.com/account/register";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", ed_Correo.getText().toString());
            jsonBody.put("username", ed_Usuario.getText().toString());
            jsonBody.put("password", ed_Clave.getText().toString());
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Caso correcto
                    pDialog.cancel();

                    new SweetAlertDialog(Registro.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Enhorabuena")
                            .setContentText("Su cuenta ha sido creada correctamente")
                            .setConfirmText("Iniciar Sesión")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    /*Intent inisesion = new Intent(Registro.this, Login.class);
                                    startActivity(inisesion);*/
                                    finish();
                                }
                            })
                            .show();
                    /*Toast.makeText(getApplicationContext(),"Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                    Intent inisesion = new Intent(Registro.this, MainActivity.class);
                    startActivity(inisesion);
                    finish();*/


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.cancel();
                    String responseBody = null;
                    try {
                        responseBody = new String(error.networkResponse.data, "utf-8");
                        System.out.println(responseBody);

                        if(responseBody.contains("len on password")){
                            new SweetAlertDialog(Registro.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("La contraseña es demasiado corta")
                                    .setConfirmText("Vale")
                                    .show();
                        }
                        else if(responseBody.contains("Email")){
                            new SweetAlertDialog(Registro.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("El correo introducido no es valido")
                                    .setConfirmText("Vale")
                                    .show();
                        }
                        else if(responseBody.contains("Validation error")){
                            new SweetAlertDialog(Registro.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("El usuario intruducido ya existe")
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
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}