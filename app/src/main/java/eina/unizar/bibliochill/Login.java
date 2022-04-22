package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

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
                finish();
            }
        });

        tv_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable(){
                    public void run() {
                        try {
                            if(postIniciarSesion().contains("200")){
                                Intent inisesion = new Intent(Login.this, MainActivity.class);
                                startActivity(inisesion);
                                finish();
                            }
                            else{
                                Toast.makeText(Login.this, "Mensaje error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();


            }
        });
    }


    String postIniciarSesion() throws IOException{
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", ed_Usuario.getText().toString())
                .addFormDataPart("password", ed_Clave.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("https://queenchess-backend.herokuapp.com/account/login")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}