package com.example.prova2b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;


public class ConsultarActivity extends AppCompatActivity {

    private EditText edt_nomeprop;
    private EditText edt_telefone;
    private EditText edt_cep;
    private EditText edt_cidade;
    private EditText edt_uf;
    private EditText edt_bairro;
    private EditText edt_rua;
    private EditText edt_numero;
    private EditText edt_diaria;
    private EditText edt_temperatura;
    private EditText edt_umidade;
    private EditText edt_previsao;
    private Button btn_excluirimovel;
    private Button btn_voltar;

    public String idImovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        edt_nomeprop = findViewById(R.id.edt_nomeprop);
        edt_telefone = findViewById(R.id.edt_telefone);
        edt_cep = findViewById(R.id.edt_cep);
        edt_cidade = findViewById(R.id.edt_cidade);
        edt_uf = findViewById(R.id.edt_uf);
        edt_bairro = findViewById(R.id.edt_bairro);
        edt_rua = findViewById(R.id.edt_rua);
        edt_numero = findViewById(R.id.edt_numero);
        edt_diaria = findViewById(R.id.edt_diaria);
        edt_temperatura = findViewById(R.id.edt_temperatura);
        edt_umidade = findViewById(R.id.edt_umidade);
        edt_previsao = findViewById(R.id.edt_previsao);
        btn_excluirimovel = findViewById(R.id.btn_excluirimovel);
        btn_voltar = findViewById(R.id.btn_voltar);

        Intent intent = getIntent();

        idImovel = intent.getStringExtra("idImovel");
        edt_nomeprop.setText("Prop.: "+intent.getStringExtra("nomeProp"));
        edt_telefone.setText("Tel.: "+intent.getStringExtra("telefone"));
        edt_cep.setText("CEP: "+intent.getStringExtra("cep"));
        edt_cidade.setText("Cidade: "+intent.getStringExtra("cidade"));
        edt_uf .setText("UF: "+intent.getStringExtra("uf"));
        edt_bairro.setText("Bairro: "+intent.getStringExtra("bairro"));
        edt_rua.setText("Rua: "+intent.getStringExtra("rua"));
        edt_numero.setText("Número: "+intent.getStringExtra("numero"));
        edt_diaria.setText("Diária: R$" + intent.getStringExtra("diaria"));

        WeatherApiClient weatherApiClient = new WeatherApiClient();

        weatherApiClient.getWeatherData(this, intent.getStringExtra("cidade"), new WeatherApiClient.WeatherDataListener() {
            @Override
            public void onSuccess(WeatherData weatherData) {
                // Faça algo com os dados climáticos
                WeatherData.Main main = weatherData.getMain();
                WeatherData.Weather[] weather = weatherData.getWeather();

                if (main != null && weather != null && weather.length > 0) {
                    double temperatura = main.getTemp();
                    double umidade = main.getHumidity();
                    String descricao = weather[0].getDescription();

                    edt_temperatura.setText("Temperatura: "+Double.toString(temperatura)+"ºC");
                    edt_umidade.setText("Umidade do ar: "+Double.toString(umidade)+"%");
                    edt_previsao.setText("Descrição: "+descricao);

                } else {

                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        btn_excluirimovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerImovel(idImovel);
            }
        });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultarActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static class WeatherApiClient {

        private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
        private static final String API_KEY = "ddea4e822c0d579b9d59ac9b4c8df78e";

        private final AsyncHttpClient client = new AsyncHttpClient();

        public interface WeatherDataListener {
            void onSuccess(WeatherData weatherData);
            void onFailure(String errorMessage);
        }

        public void getWeatherData(Context context, String location, WeatherDataListener listener) {
            RequestParams params = new RequestParams();
            params.put("q", location);
            params.put("appid", API_KEY);
            params.put("units", "metric");

            client.get(context, BASE_URL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody);
                        Gson gson = new Gson();
                        WeatherData weatherData = gson.fromJson(response, WeatherData.class);
                        listener.onSuccess(weatherData);
                    } catch (Exception e) {
                        listener.onFailure("Erro ao processar os dados do clima.");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    listener.onFailure("Erro na requisição: " + error.getMessage());
                }
            });
        }
    }

    public static class WeatherData {

        private Main main;
        private Weather[] weather;

        public static class Main {
            private double temp;
            private double humidity;

            public double getTemp() {
                return temp;
            }

            public double getHumidity() {
                return humidity;
            }
        }

        public static class Weather {
            private String description;

            public String getDescription() {
                return description;
            }
        }

        public Main getMain() {
            return main;
        }

        public Weather[] getWeather() {
            return weather;
        }
    }

    private void removerImovel(String itemString) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("imoveis");

        Query query = itemsRef.orderByChild("idImovel").equalTo(itemString);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ConsultarActivity.this, "Imóvel removido com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConsultarActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Ocorreu um erro ao excluir o item
                            Toast.makeText(ConsultarActivity.this, "Não foi possível remover o imóvel!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Ocorreu um erro na consulta
            }
        });
    }
}