package imosystem.amigosecreto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class telaNomesInstantaneo extends AppCompatActivity {

    EditText edtNome;
    ArrayList<String> nomes;
    ArrayAdapter<String> adapter;
    ListView listView;
    int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instantaneo_pessoa);

        nomes = new ArrayList<String>();
        edtNome = (EditText) findViewById(R.id.edtNome);

        listView = (ListView) findViewById(R.id.listNomesNumeros);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);



        //Responsavel por abrir uma caixa de dialogo no momento que um item for selecionado.
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE,1,Menu.NONE, "Deletar");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(adapter);

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            //Responsavel por abrir uma caixa de dialogo no momento que um item for selecionado.
            /*Note que enviamos 4 parâmetros:
                1º parâmetro: Refere-se ao groupId.
                2º parâmetro: Nesse parâmetro enviamos o ItemId que é justamente o id do menu, ou seja, é por meio dele que identificaremos esse menu.
                3º parâmetro: Nele informamos o order, ou seja, a posição que queremos ordenar o menu.
                4º parâmetro: Indica o nome que será exibido para o menu.*/

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE,1,Menu.NONE, "Deletar");
            }
        });
    }


    // Sobrescrevemos o método onContextItemSelected para dar uma ação para um context menu.
    // Esse metodo retorna o valor super.onContextItemSelected(item) que equivale ao valor true.
    //   Quando retornamos true, informamos que o click para esse menu, será consumido apenas por ele.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = menuInfo.position;

        switch (item.getItemId()) {
            case 1:
                deletar(position);
        }
        return super.onContextItemSelected(item);
    }

    public void btnAdicionar(View v){
        if(edtNome.getText().toString().isEmpty()){
            Toast.makeText(this, "Informe o nome do participante" , Toast.LENGTH_SHORT).show();
        }else {
            nomes.add(edtNome.getText().toString());
            edtNome.setText("");
            adapter.notifyDataSetChanged();

            cont++;
        }
    }


    public void btnContinuar(View v){

        if(nomes.isEmpty() || nomes.size() < 3){
            Toast.makeText(this, "É necessario informar ao menos 3 participantes!", Toast.LENGTH_SHORT).show();
        }else{

            Intent it = new Intent(this, telaSorteioInstantaneo.class);
            it.putStringArrayListExtra("Nomes", nomes);
            it.putExtra("Contador", cont);
            startActivity(it);
        }
    }

    private void deletar(int position) {
        nomes.remove(position);
        adapter.notifyDataSetChanged();
        cont--;
    }
}


