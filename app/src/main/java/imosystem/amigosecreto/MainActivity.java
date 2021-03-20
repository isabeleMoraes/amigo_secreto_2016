package imosystem.amigosecreto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    //OBSERVAÇÃO IMPORTATNTE
    // É preciso ajustar o layout para que fique bonito em qualquer resolução de tela.


    Button btnsms;
    Button btnInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsms = (Button) findViewById(R.id.sorteioSms);
        btnsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent it = new Intent(MainActivity.this, sms_pessoas.class);
                startActivity(it);
            }
        });

        btnInst = (Button) findViewById(R.id.sorteioInst);
        btnInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, telaNomesInstantaneo.class);
                startActivity(it);
            }
        });
    }
}
