package imosystem.amigosecreto.recursos;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import imosystem.amigosecreto.R;
import imosystem.amigosecreto.entidades.Pessoa;

public class adapterContatoPersonalizado extends BaseAdapter {

    private final List<Pessoa> contatos;
    private final Activity activity;

    public adapterContatoPersonalizado(Activity activity, List<Pessoa> contatos) {
        this.contatos = contatos;
        this.activity = activity;
    }

    //Responsavel por retornar a quantidade de itens da lista.
    @Override
    public int getCount() {
        return contatos.size();
    }

    //Retorna um item da lista de acordo com a posição passada.
    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.lista_contato_personalizada,parent,false);
        Pessoa contato = contatos.get(position);

        //pegando as referências das Views
        TextView nome = (TextView)
                view.findViewById(R.id.lista_contato_personalizada_nome);
        TextView telefone = (TextView)
                view.findViewById(R.id.lista_contato_personalizada_telefone);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.lista_contato_personalizada_imagem);

        //populando as Views
        nome.setText(contato.getNome());
        telefone.setText(contato.getCelular());
        imagem.setImageResource(R.drawable.user);

        return view;
    }
}
