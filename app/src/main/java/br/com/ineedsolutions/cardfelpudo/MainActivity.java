package br.com.ineedsolutions.cardfelpudo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    String[] listaNomes = {"Felpudo","Fofura","Lesmo","Bugado","Uruca","iOS","Android","Realidade Aumentada"};
    int[] listaIcones = {R.drawable.felpudo,R.drawable.fofura,R.drawable.lesmo,R.drawable.bugado,R.drawable.uruca,R.drawable.ios,R.drawable.realidade_aumentada};
    String[] listaDescricoes = {"Felpudo","Fofura","Lesmo","Bugado","Uruca","iOS","Android","Realidade Aumentada"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayAdapter<String> meuAdaptador = new ArrayAdapter<String>(getApplicationContext(),
//                android.R.layout.simple_expandable_list_item_1,android.R.id.text1,listaNomes);
//        ListView minhaLista = findViewById(R.id.minhaLista);
//        minhaLista.setAdapter(meuAdaptador);
//        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "" +listaNomes[i] , Toast.LENGTH_SHORT).show();
//            }
//        });
        ListView minhaLista = findViewById(R.id.minhaLista);
        MeuAdaptador meuAdaptador;;
        meuAdaptador = new MeuAdaptador(getApplicationContext(), R.layout.minha_celula);

        int i = 0 ;
        for(String nome:listaNomes){
            DadosPersonagem dadosPersonagem;
            dadosPersonagem = new DadosPersonagem(listaIcones[i],listaNomes[i],listaDescricoes[i]);
        }

        minhaLista.setAdapter(meuAdaptador);

    }
}
class ViewPersonagem{
    ImageView icone;
    TextView titulo;
    TextView descricao;
}
class DadosPersonagem{
    private  int icone;
    private  String titulo;
    private String descricao;

    public DadosPersonagem(int icone, String nome, String descricao) {
        this.icone = icone;
        this.titulo = nome;
        this.descricao = descricao;
    }

    public int getIcone() {
        return icone;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }
}
class  MeuAdaptador extends ArrayAdapter{
    @Override
    public void add(@Nullable Object object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View minhaView;
        minhaView = convertView;
        ViewPersonagem viewPersonagem;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            minhaView = inflater.inflate(R.layout.minha_celula,parent,false);
            viewPersonagem = new ViewPersonagem();
            viewPersonagem.icone = (ImageView) minhaView.findViewById(R.id.meuIcone);
            viewPersonagem.titulo = (TextView) minhaView.findViewById(R.id.meuTitulo);
            viewPersonagem.descricao = (TextView) minhaView.findViewById(R.id.descricao);

            minhaView.setTag(viewPersonagem);
        }else {
            viewPersonagem = (ViewPersonagem) minhaView.getTag();
        }
        DadosPersonagem dadosPersonagem;
        dadosPersonagem = (DadosPersonagem)this.getItem(position);

        viewPersonagem.icone.setImageResource(dadosPersonagem.getIcone());
        viewPersonagem.titulo.setText(dadosPersonagem.getTitulo());
        viewPersonagem.descricao.setText(dadosPersonagem.getDescricao());

        return minhaView;
    }

    public MeuAdaptador(@NonNull Context context, int resource) {
        super(context, resource);
    }
}