package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Registro extends AppCompatActivity {
    TextView tv_CrearCuenta, tv_IniciarSesion;
    EditText ed_Usuario,ed_Clave,ed_Correo;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tv_CrearCuenta = findViewById(R.id.tv_CrearCuenta);
        tv_IniciarSesion = findViewById(R.id.tv_IniciarSesion);

        ed_Usuario = findViewById(R.id.ed_Usuario);
        ed_Clave = findViewById(R.id.ed_Clave);
        ed_Correo = findViewById(R.id.ed_Correo);

        tv_CrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable(){
                    public void run() {
                        try {
                            if(postRegistro().contains("200")){
                                Intent inisesion = new Intent(Registro.this, MainActivity.class);
                                startActivity(inisesion);
                                finish();
                            }
                            else{
                                Toast.makeText(Registro.this, "Mensaje error", Toast.LENGTH_SHORT).show();
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

        tv_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inisesion = new Intent(Registro.this, Login.class);
                startActivity(inisesion);
                finish();
            }
        });
    }

    String postRegistro() throws IOException {
        
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", ed_Correo.getText().toString())
                .addFormDataPart("username", ed_Usuario.getText().toString())
                .addFormDataPart("password", ed_Clave.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("https://queenchess-backend.herokuapp.com/account/register")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}