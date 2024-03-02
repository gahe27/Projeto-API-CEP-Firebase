package com.example.prova2b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CadastrarActivity extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("imoveis");

    private EditText edt_nomeprop;
    private EditText edt_telefone;
    private EditText edt_cep;
    private EditText edt_cidade;
    private EditText edt_uf;
    private EditText edt_bairro;
    private EditText edt_rua;
    private EditText edt_numero;
    private EditText edt_diaria;

    private Button btn_cadastrar;
    private Button btn_voltar;

    private Button btn_pesquisarcep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        edt_nomeprop = findViewById(R.id.edt_nomeprop);
        edt_telefone = findViewById(R.id.edt_telefone);
        edt_cep = findViewById(R.id.edt_cep);
        edt_cidade = findViewById(R.id.edt_cidade);
        edt_uf = findViewById(R.id.edt_uf);
        edt_bairro = findViewById(R.id.edt_bairro);
        edt_rua = findViewById(R.id.edt_rua);
        edt_numero = findViewById(R.id.edt_numero);
        edt_diaria = findViewById(R.id.edt_diaria);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_voltar = findViewById(R.id.btn_voltar);
        btn_pesquisarcep = findViewById(R.id.btn_pesquisarcep);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeprop = edt_nomeprop.getText().toString();
                String telefone = edt_telefone.getText().toString();
                String cep = edt_cep.getText().toString();
                String cidade = edt_cidade.getText().toString();
                String uf = edt_uf.getText().toString();
                String bairro = edt_bairro.getText().toString();
                String rua = edt_rua.getText().toString();
                String numero = edt_numero.getText().toString();
                String diaria = edt_diaria.getText().toString();

                if(!TextUtils.isEmpty(nomeprop) && !TextUtils.isEmpty(telefone) && !TextUtils.isEmpty(cep) && !TextUtils.isEmpty(cidade) &&
                        !TextUtils.isEmpty(uf) && !TextUtils.isEmpty(bairro) && !TextUtils.isEmpty(rua) && !TextUtils.isEmpty(numero) && !TextUtils.isEmpty(diaria)){

                    Imovel i = new Imovel(nomeprop, telefone, cep, cidade, uf, bairro, rua, numero, diaria);
                    i.setIdImovel(reference.push().getKey());
                    reference.child(i.getIdImovel()).setValue(i);
                    Intent intent = new Intent(CadastrarActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(CadastrarActivity.this, "Confirme se todos os campos foram preenchidos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastrarActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_pesquisarcep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cep = edt_cep.getText().toString();

                if(!TextUtils.isEmpty(cep)) {
                    String url = "https://viacep.com.br/ws/" + cep + "/json/";

                    new FetchJsonTask().execute(url);
                }else{
                    Toast.makeText(CadastrarActivity.this, "Digite um valor para o CEP!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class FetchJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String urlString = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder jsonResult = new StringBuilder();

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResult.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return jsonResult.toString();
        }

        @Override
        protected void onPostExecute(String json) {
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);

                    String cidade = jsonObject.optString("localidade", "");
                    String uf = jsonObject.optString("uf", "");
                    String bairro = jsonObject.optString("bairro", "");
                    String rua = jsonObject.optString("logradouro", "");

                    if(!TextUtils.isEmpty(cidade) && !TextUtils.isEmpty(uf) && !TextUtils.isEmpty(bairro) && !TextUtils.isEmpty(rua)){
                        edt_cidade.setText(cidade);
                        edt_uf.setText(uf);
                        edt_bairro.setText(bairro);
                        edt_rua.setText(rua);
                    }else{
                        edt_cidade.setText(cidade);
                        edt_uf.setText(uf);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(CadastrarActivity.this, "Erro ao buscar dados pelo CEP.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
