package eina.unizar.bibliochill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {
    TextView tv_Elo,tv_Monedas,tv_Correo;
    EditText ed_Usuario,ed_Clave,ed_ConfirmarClave;
    ImageView iv_atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iv_atras = findViewById(R.id.iv_atras);
        iv_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}