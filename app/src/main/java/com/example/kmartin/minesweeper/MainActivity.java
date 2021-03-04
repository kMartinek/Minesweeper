package com.example.kmartin.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public cell[][]g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
        /*
        //kako bi se baza podataka spremila samo jednom
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("MainActivity", Boolean.toString(prefs.getBoolean("firstTime", true)));
        if (prefs.getBoolean("firstTime", true)) {
            // <---- run your one time code here
            databaseSetup();

            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }
    protected void databaseSetup()
    {
        Log.e("MainActivity", "banana");
    }*/

    public void ispisi(cell[][]grid, int sirina, int visina)
    {
        for(int k=0; k<g.length; k++)
        {
            String zaispis="";
            for(int m=0; m<g[k].length; m++)
            {
                zaispis+="[";
                zaispis += String.valueOf(g[k][m].brojBombi);
                zaispis+="]";
            }
            Log.e("Generator", zaispis);
        }
    }
    public void klik_lagano(View view) {


        Intent intent = new Intent(this, Igra.class);
        intent.putExtra("brojBombi",5);
        intent.putExtra("sirina",7);
        intent.putExtra("visina",7);
        startActivity(intent);

    }
    public void klik_srednje(View view)
    {
        Intent intent = new Intent(this, Igra.class);
        intent.putExtra("brojBombi",15);
        intent.putExtra("sirina",10);
        intent.putExtra("visina",10);
        startActivity(intent);
    }
    public void klik_tesko(View view)
    {
        Intent intent = new Intent(this, Igra.class);
        intent.putExtra("brojBombi",25);
        intent.putExtra("sirina",10);
        intent.putExtra("visina",10);
        startActivity(intent);
    }
    public void prebaci(View view)
    {
        Intent intent = new Intent(this, Pobjeda.class);
        intent.putExtra("vrijeme", -1 );
        startActivity(intent);

    }
}
