package com.example.kmartin.minesweeper;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Pobjeda extends AppCompatActivity {


    long trenutni_id = -1;
    int tezina = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pobjeda);

        Intent intent = getIntent();
        int vrijeme = intent.getIntExtra("vrijeme", -1);
        int brBombi = intent.getIntExtra("bombe", 0);
        if(brBombi==5) tezina = 1;
        else if(brBombi==15) tezina = 2;
        else if(brBombi ==25) tezina = 3;

        DBAdapter db = new DBAdapter(this);


        if(vrijeme != -1)
        {
            db.open();
            trenutni_id = db.insertHighScore(vrijeme, tezina);
            db.close();
            TextView tv = (TextView)findViewById(R.id.textView);
            tv.setText("Čestitamo! Pobijedili ste s rezultatom " + Integer.toString(vrijeme));

        }
        else
        {
            TextView tv = (TextView)findViewById(R.id.textView);
            tv.setText("Najbolji rezultati");
        }

        //dohvaćanje svih rezultata
        db.open();
        int br = 0;
        Cursor c = db.getAllResults();

        if(tezina == 0){
            for(int x= 1; x<=3; ++x)
            {
                br = 0;
                c = db.getAllResults();

                TableLayout tl2 = (TableLayout) findViewById(R.id.tl2);
                TableRow tezinaigre = new TableRow(this);
                tl2.addView(tezinaigre);
                TextView tez = new TextView(this);
                if(x==1) tez.setText("LAGANO");
                if(x==2) tez.setText("SREDNJE");
                if(x==3) tez.setText("TEŠKO");
                tezinaigre.addView(tez);


                if (c.moveToFirst()) {
                    do {

                        if (c.getInt(3) == x) {
                            ++br;
                            TableLayout tl = (TableLayout) findViewById(R.id.tl2);
                            TableRow tr = new TableRow(this);
                            tl.addView(tr);
                            TextView tv2 = new TextView(this);
                            tv2.setText(br + ". " + c.getString(1));
                            TextView tv3 = new TextView(this);
                            tv3.setText("   " + c.getString(2));
                            tr.addView(tv2);
                            tr.addView(tv3);
                        }
                    } while (c.moveToNext() && br < 6);
                }
            }
        }


        if (c.moveToFirst())
        {
            do {

                //rezultat=c.getString(1);
                //vrijeme=c.getString(2);
                if(c.getInt(3) == tezina)
                {
                    ++br;
                    TableLayout tl = (TableLayout) findViewById(R.id.tl2);
                    TableRow tr = new TableRow(this);
                    tl.addView(tr);
                    TextView tv2 = new TextView(this);
                    tv2.setText(br + ". " + c.getString(1));
                    TextView tv3 = new TextView(this);
                    tv3.setText("   " + c.getString(2));
                    tr.addView(tv2);
                    tr.addView(tv3);
                }
            } while (c.moveToNext() && br <11);
        }
        db.close();

        /*
        //---add a contact---
        db.open();
        long id = db.insertContact("Wei-Meng Lee", "weimenglee@learn2develop.net");
        id = db.insertContact("Mary Jackson", "mary@jackson.com");
        db.close();



        //--get all contacts---
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();



        //---get a contact---
        db.open();
        Cursor cu = db.getContact(2);
        if (cu.moveToFirst())
            DisplayContact(cu);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();



        //---update contact---
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();



        //---delete a contact---
        db.open();
        if (db.deleteContact(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();

        */
    }
}

