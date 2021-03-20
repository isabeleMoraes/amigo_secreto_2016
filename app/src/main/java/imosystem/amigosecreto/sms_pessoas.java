package imosystem.amigosecreto;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import imosystem.amigosecreto.entidades.Pessoa;
import imosystem.amigosecreto.recursos.adapterContatoPersonalizado;

public class sms_pessoas extends AppCompatActivity {

    private ArrayList<Pessoa> pessoas;
    private adapterContatoPersonalizado adapter;
    private ListView listView;
    private EditText edtNome;
    private EditText edtTelefone;
    private Button btnContatos;
    private Button btnAdicionar;
    private Button btnContinuar;
    private ListView lstNames;
    static final int REQUEST_SELECT_CONTACT = 1;

    private int cont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_pessoas);

        edtNome = (EditText) findViewById(R.id.edtNome);

        edtTelefone = (EditText) findViewById(R.id.edtTelefone);

        btnContatos = (Button) findViewById(R.id.btnContatos);

        btnAdicionar = (Button) findViewById(R.id.btnAdd);

        btnContinuar = (Button) findViewById(R.id.btnContinuar);

        listView = (ListView) findViewById(R.id.listNomesNumeros);

        pessoas = new ArrayList<>();

        adapter = new adapterContatoPersonalizado(this, pessoas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tudo sempre passar pelo onResume, então setamos o adapter no list aqui.
        listView.setAdapter(adapter);

        //Responsavel por abrir uma caixa de dialogo no momento que um item for selecionado.
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

    private void deletar(int position) {
        pessoas.remove(position);
        adapter.notifyDataSetChanged();
        cont--;
    }

    public void btnAdicionar(View v){
        String nome= edtNome.getText().toString();
        String telefone =  edtTelefone.getText().toString();
        if(nome.isEmpty() || telefone.isEmpty()){
            Toast.makeText(this, "Informe o nome e telefone do participante" , Toast.LENGTH_SHORT).show();
        }else {

            pessoas.add(new Pessoa(nome,telefone));
            edtNome.setText("");
            edtTelefone.setText("");
            adapter.notifyDataSetChanged();

            /*System.out.println(nomes);*/
            cont++;
        }
    }

    public void clickBtnContatos(View v){

        showContacts();

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void clickBtnContinuar(View v){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 100);
        }else{

            if(pessoas.size()>0){
                SmsManager smsManager = SmsManager.getDefault();
                for(int i=0; i<pessoas.size();i++){
                    smsManager.sendMultipartTextMessage (pessoas.get(i).getCelular(), null,
                            smsManager.divideMessage("Ola "+pessoas.get(i).getNome()+", essa mensagem está sendo enviada de meu aplicativo para fins de teste. Agradeço a paciencia:) !!!"),
                            null, null);
                }
                Toast.makeText(this, "\n" +"Mensagens Enviadas!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "\n" +"Informe ao menos um contato!", Toast.LENGTH_LONG).show();
            }

        }


        // Por WhatsApp
        /*Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.putExtra("jid", "55" + (991957580) + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Essa mensagem está sendo enviada de meu aplicativo :) !!!");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);*/

    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            //selecionarContatos();
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            selecionarContatos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //showContacts();
            } else {
                Toast.makeText(this, "\n" +"Até você conceder a permissão, não podemos exibir os nomes", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selecionarContatos() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            // Do something with the selected contact at contactUri

            Cursor cursor = getContentResolver().query(contactUri,null,null,null,null);

            while(cursor.moveToNext()){
                String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));

                criaPessoa(id, nome);
            }

        }
    }

    public void criaPessoa(int id, final String nome){
        final ArrayList<String> numeros = new ArrayList<>();
        ArrayAdapter<String> adapterDialog = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numeros);

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="+id,
                null,null);
        while(cursor.moveToNext()) {
            String numero = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
            //System.out.println(nome + "N: " + numero);
            numeros.add(numero);
        }

        if(numeros.size()>1){
            // Necessario para bloquear o click fora.
            AlertDialog alerta;
            AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Selecione o numero desejado!");

            dialog.setAdapter(adapterDialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("Numero selecionado: " + numeros.get(which));

                    Pessoa pessoa = new Pessoa(nome, numeros.get(which));
                    pessoas.add(pessoa);
                    adapter.notifyDataSetChanged();
                    cont++;
                }
            });

            dialog.setNegativeButton("Cancelar", null);

            //Coloco o dialog dentor do alerta para poder bloquear o click fora.
            alerta = dialog.create();

            // Responsavel por bloquear o click fora do alert
            alerta.setCanceledOnTouchOutside(true);

            dialog.show();
        }else{
            Pessoa pessoa = new Pessoa(nome, numeros.get(0));
            pessoas.add(pessoa);
            adapter.notifyDataSetChanged();
            cont++;
        }
    }
}
