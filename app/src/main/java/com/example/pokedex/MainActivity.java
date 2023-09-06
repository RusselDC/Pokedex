package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView pokeImage;
    EditText pokeSearch;
    Button searchBtn,clearBtn;
    TextView pokeName,pokeType,pokeIndex,pokeHealth,pokeAttack,pokeDefense,pokeSA,pokeSD,pokeSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    public void initialize()
    {
        pokeImage = findViewById(R.id.pokeImage);
        pokeSearch = findViewById(R.id.pokeSearch);
        searchBtn = findViewById(R.id.searchBtn);
        clearBtn = findViewById(R.id.clearBtn);
        pokeName = findViewById(R.id.pokeName);
        pokeType = findViewById(R.id.pokeType);
        pokeIndex = findViewById(R.id.pokeIndex);
        pokeHealth = findViewById(R.id.pokeHealth);
        pokeAttack = findViewById(R.id.pokeAttack);
        pokeDefense = findViewById(R.id.pokeDefense);
        pokeSA = findViewById(R.id.pokeSA);
        pokeSD = findViewById(R.id.pokeSD);
        pokeSpeed = findViewById(R.id.pokeSpeed);
        String pokemon = pokeSearch.getText().toString().toLowerCase();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = pokeSearch.getText().toString().toLowerCase();
                if(search != null){
                    apicall(getApplicationContext(),search);

                }else{
                    Toast.makeText(MainActivity.this, "Search field is empty", Toast.LENGTH_SHORT).show();
                }


            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearField();
            }
        });


    }

    public void clearField()
    {
        Picasso.get()
                .load("https://i.pinimg.com/originals/51/69/ee/5169ee51e0f8b57fe115a822b4188f8d.png")
                .into(pokeImage);
        pokeName.setText("");
        pokeIndex.setText("#");
        pokeType.setText("");

        pokeHealth.setText("");
        pokeAttack.setText("");
        pokeDefense.setText("");
        pokeSA.setText("");
        pokeSD.setText("");
        pokeSpeed.setText("");
        Toast.makeText(this, "Charmander cleared the fields for you", Toast.LENGTH_SHORT).show();
    }

    public void apicall(Context c, String name)
    {
        RequestQueue request = Volley.newRequestQueue(c);
        request.start();
        String url = "https://pokeapi.co/api/v2/pokemon/"+name;
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET,url,null,
                response -> {
                    try {
                        //name
                        String pokemoname = response.getString("name");
                        //index
                        JSONArray gameI = response.getJSONArray("game_indices");
                        JSONObject zero = gameI.getJSONObject(0);
                        int index = zero.getInt("game_index");
                        //pokemon type
                        JSONArray types = response.getJSONArray("types");
                        JSONObject i = types.getJSONObject(0);
                        JSONObject type = i.getJSONObject("type");
                        String pokesType = type.getString("name");
                        //image
                        JSONObject sprites = response.getJSONObject("sprites");
                        JSONObject other = sprites.getJSONObject("other");
                        JSONObject off = other.getJSONObject("official-artwork");
                        String imageUrl = off.getString("front_default");
                        //getting object and array indexes
                        JSONArray stats = response.getJSONArray("stats");
                        JSONObject zerostat = stats.getJSONObject(0);
                        JSONObject onestat = stats.getJSONObject(1);
                        JSONObject twostat = stats.getJSONObject(2);
                        JSONObject threestat = stats.getJSONObject(3);
                        JSONObject fourstat = stats.getJSONObject(4);
                        JSONObject fivestat = stats.getJSONObject(5);

                        //stat value
                        String health = zerostat.getString("base_stat");
                        String attack = onestat.getString("base_stat");
                        String defense = twostat.getString("base_stat");
                        String Sattack = threestat.getString("base_stat");
                        String Sdefense = fourstat.getString("base_stat");
                        String speed = fivestat.getString("base_stat");



                        pokeName.setText(pokemoname.toUpperCase());
                        pokeIndex.setText("#"+index);
                        pokeType.setText(pokesType.toUpperCase());
                        Picasso.get()
                                .load(imageUrl)
                                .into(pokeImage);
                        pokeHealth.setText(health);
                        pokeAttack.setText(attack);
                        pokeDefense.setText(defense);
                        pokeSA.setText(Sattack);
                        pokeSD.setText(Sdefense);
                        pokeSpeed.setText(speed);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                        pokeName.setText("CANT BE FOUND");
                        pokeIndex.setText("0");
                        pokeType.setText("NONE");
                        Picasso.get()
                        .load("https://mystickermania.com/cdn/stickers/pokemon/squirtle-flower-512x512.png")
                        .into(pokeImage);
                        pokeHealth.setText("0");
                        pokeAttack.setText("0");
                        pokeDefense.setText("0");
                        pokeSA.setText("0");
                        pokeSD.setText("0");
                        pokeSpeed.setText("0");
                        Toast.makeText(c, "Squirtle cant find the pokemon you are looking for :'<", Toast.LENGTH_SHORT).show();
        });
        request.add(json);
    }

}