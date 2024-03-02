package com.example.prova2b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth userAuth;
    private ListView lv_imoveis;
    private Button btn_cadastrar;
    private Button btn_sair;
    private Button btn_excluirconta;


    ArrayAdapter<String> adapter;

    public String dadosString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        userAuth = FirebaseAuth.getInstance();

        user = userAuth.getCurrentUser();

        lv_imoveis = findViewById(R.id.lv_imoveis);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_sair = findViewById(R.id.btn_sair);
        btn_excluirconta = findViewById(R.id.btn_excluirconta);

        if(user == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            preenche_listview();
        }

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, CadastrarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAuth.signOut();
                Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_excluirconta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                       Toast.makeText(PrincipalActivity.this, "Usuário removido com sucesso.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(PrincipalActivity.this, "Falha ao remover o usuário.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        lv_imoveis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idImovelClicado = (String) lv_imoveis.getItemAtPosition(i);

                DatabaseReference imoveisRef = FirebaseDatabase.getInstance().getReference().child("imoveis");

                imoveisRef.child(idImovelClicado).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            String idImovel = snapshot.child("idImovel").getValue(String.class);
                            String nomeProp = snapshot.child("nomeProp").getValue(String.class);
                            String telefone = snapshot.child("telefone").getValue(String.class);
                            String CEP = snapshot.child("cep").getValue(String.class);
                            String cidade = snapshot.child("cidade").getValue(String.class);
                            String uf = snapshot.child("uf").getValue(String.class);
                            String bairro = snapshot.child("bairro").getValue(String.class);
                            String rua = snapshot.child("rua").getValue(String.class);
                            String numero = snapshot.child("numero").getValue(String.class);
                            String diaria = snapshot.child("diaria").getValue(String.class);

                            Intent intent = new Intent(PrincipalActivity.this, ConsultarActivity.class);

                            intent.putExtra("idImovel", idImovel);
                            intent.putExtra("nomeProp", nomeProp);
                            intent.putExtra("telefone", telefone);
                            intent.putExtra("cep", CEP);
                            intent.putExtra("cidade", cidade);
                            intent.putExtra("uf", uf);
                            intent.putExtra("bairro", bairro);
                            intent.putExtra("rua", rua);
                            intent.putExtra("numero", numero);
                            intent.putExtra("diaria", diaria);

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PrincipalActivity.this, "Erro ao selecionar imóvel.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors
                    }
                });
            }
        });

    }

    public void preenche_listview(){
        DatabaseReference imoveis = FirebaseDatabase.getInstance().getReference().child("imoveis");

        Query dadosImoveis = imoveis.orderByChild("idImovel");

        ArrayList<String> i = new ArrayList<>();

        dadosImoveis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                i.clear();

                for(DataSnapshot dados: snapshot.getChildren()){
                    Imovel imovel = dados.getValue(Imovel.class);
                    i.add(imovel.getIdImovel());
                }

                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, i);
                lv_imoveis.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}