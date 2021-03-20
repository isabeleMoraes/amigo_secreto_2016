package imosystem.amigosecreto;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class telaSorteioInstantaneo extends AppCompatActivity {

    TextView txtNomePai;
    TextView txtNomeFilho;
    Button btnSorteio;
    Button btnProximo;
    Integer cont;

    ArrayList<String> nomes_sorteio;
    ArrayList<String> nomes_randon;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio_instantaneeo);

        txtNomePai = (TextView) findViewById(R.id.nome1);
        txtNomeFilho = (TextView) findViewById(R.id.nome2);
        btnSorteio = (Button) findViewById(R.id.btnSorteio);
        btnProximo = (Button) findViewById(R.id.btnProximo);

        obtemDadosInseridos();

    }

    private void obtemDadosInseridos(){
        //puxa o array e o contador da outra tela.
        bundle = getIntent().getExtras();

        //Esse if verifica se o budle possui o array
        if (bundle.containsKey("Nomes")) {
            nomes_sorteio = bundle.getStringArrayList("Nomes");
            //nomes_randon = bundle.getStringArrayList("Nomes");
            cont = bundle.getInt("Contador") - 1;

            //seta o ultimo nome do array no primeiro campo.
            txtNomePai.setText(nomes_sorteio.get(cont).toString());

            // Faz uma copia do array de nomes para que seja possivel controlar os nomes ja sorteados.
            // Utilizo o clone para que ele crie um novo ponteiro ao invés de utilizar o mesmo.
            nomes_randon = (ArrayList<String>) nomes_sorteio.clone();

            sorteio();
        }
    }

    public void sorteio(){
        boolean sortear = true;

        while (sortear){
            sortear = false;
            try{

                //Embaralha o array doplicado
                Collections.shuffle(nomes_randon);

                for(int i=nomes_randon.size()-1; i>=0; i--){
                    if(nomes_randon.get(i) == nomes_sorteio.get(i)){
                        sortear = true;
                        break;
                    }
                }

            }catch(Exception e){
                sortear = true;
            }
        }
    }

    public void clickBtnSorteio(View v){
        txtNomeFilho.setVisibility(View.VISIBLE);
        btnSorteio.setVisibility(View.INVISIBLE);
        btnProximo.setVisibility(View.VISIBLE);


        txtNomeFilho.setText(nomes_randon.get(cont));
    }


    public void clickBtnProximo(View v){
        txtNomeFilho.setVisibility(View.INVISIBLE);
        btnSorteio.setVisibility(View.VISIBLE);
        btnProximo.setVisibility(View.INVISIBLE);

        cont = cont-1;
        if(cont>=0) {
            txtNomePai.setText(nomes_sorteio.get(cont).toString());

        }else{

            AlertDialog aler;
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Pronto");
            alerta.setMessage("Todos os participantes foram sorteados. Deseja sortear novamente?");

            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    obtemDadosInseridos();
                }
            });

            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //Com esse código, você está voltando para a Activity A dizendo que todas as outras Activities
                    // que estão em cima dela na pilha de execução deverão ser finalizadas.
                    Intent it = new Intent(telaSorteioInstantaneo.this, MainActivity.class);
                    it.putExtra("EXIT", true);
                    startActivity(it);

                    // Responsavel por fechar todas as telas anteriores.
                    finishAffinity();
                }
            });


            //cria o AlertDialog
            aler = alerta.create();

            // Responsavel por bloquear o click fora do alert
            aler.setCanceledOnTouchOutside(false);

            //Exibe
            aler.show();
        }
    }
}