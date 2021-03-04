package com.example.kmartin.minesweeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Igra extends AppCompatActivity {

    cell [][]g;
    int sirina;
    int visina;
    int brBombi;
    int brBombiPoc;
    boolean prvi=false;
    long proteklo;
    int gotovo = 0;
    List<ImageButton> btns= new ArrayList<ImageButton>();

    public void ispisi()
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igra);

        Intent intent = getIntent();
        brBombi = intent.getIntExtra("brojBombi", 10);
        brBombiPoc=brBombi;
        sirina = intent.getIntExtra("sirina", 10);
        visina = intent.getIntExtra("visina", 10);

        //oblik chronometra
        Chronometer chrono = (Chronometer) findViewById(R.id.ch1);
        chrono.setText(String.format("Vrijeme: 000"));
        Generator gen=new Generator(this);
        g=gen.generate(brBombi, sirina, visina);
        ispisi();
        kreirajGumbe(g, sirina, visina);

        TextView et=(TextView)findViewById(R.id.preostale);
        et.setText("Broj bombi: " + Integer.toString(brBombi));
    }



    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        cell polje[]= new cell[sirina*visina];
        int indeks = 0;
        for(int i=0; i<visina; ++i)
        {
            for (int j=0; j< sirina; ++j)
            {
                polje[indeks] = g[i][j];
                indeks++;
            }
        }
        outState.putParcelableArray("grid", polje);
        outState.putLong("vrijeme", proteklo);
        outState.putInt("bombe",brBombi);
    }

    //Za kasniji dohvat informacije
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);


        cell polje[] = (cell[])savedInstanceState.getParcelableArray("grid");
        int indeks = 0;
        for(int i=0; i<visina; ++i)
        {
            for (int j=0; j< sirina; ++j)
            {
                g[i][j] = polje[indeks];
                indeks++;
            }
        }
        brBombi = savedInstanceState.getInt("bombe");
        proteklo = savedInstanceState.getLong("vrijeme");
        prvi=true;
        final Chronometer chrono = (Chronometer) findViewById(R.id.ch1);
        chrono.setBase(SystemClock.elapsedRealtime()-proteklo*1000);

        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                time/=1000;
                if(time>=999) time=999;
                proteklo = time;
                cArg.setText(String.format("Vrijeme: %03d", time));
            }
        });
        chrono.start();

        prikazi();
    }



    private void kreirajGumbe(cell[][]grid, int sirina, int visina)
    {
        TableLayout t=(TableLayout) findViewById(R.id.tl1);
        TableRow.LayoutParams lp= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        for(int i=0; i<visina; i++)
        {
            TableRow tr= new TableRow(this);
            t.addView(tr);
            for(int j=0; j<sirina; j++)
            {

                final ImageButton mybutton= new ImageButton(this);
                mybutton.setTag(Integer.toString(i)+":"+Integer.toString(j));
                mybutton.setImageResource(R.drawable.neotvoren);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                mybutton.setLayoutParams (new TableRow.LayoutParams(width/sirina, width/sirina));
                //mybutton.setMaxHeight(mybutton.getWidth());
                mybutton.setPadding(0, 0, 0, 0);
                mybutton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                mybutton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        buttonClicked(mybutton.getTag().toString());

                    }
                });
                mybutton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClick(mybutton.getTag().toString());
                        return true;
                    }
                });


                /*final Button mybutton= new Button(this);
                mybutton.setTag(Integer.toString(i)+":"+Integer.toString(j));
                mybutton.setText(Integer.toString(grid[i][j].brojBombi));
                mybutton.setTextSize(0);
                mybutton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        buttonClicked(mybutton.getTag().toString());

                    }
                });
                mybutton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClick(mybutton.getTag().toString());
                        return true;
                    }
                }); */


                btns.add(mybutton);
                tr.addView(mybutton);
            }
        }


    }
    public void longClick(String tag)
    {
        if(gotovo==1) {game_over(0); return;}
        String[]niz=tag.split(":");
        //sad nam je x koordinata niz[0], y=niz[1]
        int x=Integer.parseInt(niz[0]);
        int y=Integer.parseInt(niz[1]);

        if(g[x][y].otkriveno==true)
        {
            Generator.dugiKLik(g,x,y,sirina, visina);
            prikazi();
            if(Generator.ProvjeriKraj(g, sirina, visina, brBombiPoc))
            {
                game_over(1);
            };
        }

    }
    public void buttonClicked(String tag)
    {
        if (gotovo==1) {game_over(0); return;}
        if(prvi==false)
        {   //tek ako se radi o prvom kliku u igri, onda možemo pokrenuti sat, nikako prije toga.
            prvi=true;
            final Chronometer chrono = (Chronometer) findViewById(R.id.ch1);
            chrono.setBase(SystemClock.elapsedRealtime()-proteklo*1000);

            chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                public void onChronometerTick(Chronometer cArg) {
                    long time = SystemClock.elapsedRealtime() - cArg.getBase();
                    time/=1000;
                    if(time>=999) time=999;
                    proteklo = time;
                    cArg.setText(String.format("Vrijeme: %03d", time));
                }
            });
            chrono.start();
        }

        String[]niz=tag.split(":");
        //sad nam je x koordinata niz[0], y=niz[1]
        int x=Integer.parseInt(niz[0]);
        int y=Integer.parseInt(niz[1]);
        //sad imamo koordinate gumba na koji je kliknuto

        //treba izvršiti sljedeće provjere
        //ako je kliknuto na već otvoreno polje, onda ne radimo ništa

        if(g[x][y].otkriveno==true){}

        //prvo, je li aktivan radio button za flag ili onaj za klik
        else {
            RadioButton btn = (RadioButton) findViewById(R.id.klikbtn);
            if (btn.isChecked())
            {
                //ako je radiobutton za klik: otvori iz generatora pokriva sve fje
                Generator.Otvori(g, x, y, sirina, visina);
                prikazi();
                if(Generator.ProvjeriKraj(g, sirina, visina, brBombiPoc))
                {
                    game_over(1);
                };

            }
            else
            {
                //ako je radiobutton za flag: je li tu već postavljen flag, ako jest, removaj ga, ako nije, stavi flag
                Generator.zastavica(g,x,y);
                --brBombi;
                TextView et=(TextView)findViewById(R.id.preostale);
                et.setText("Broj bombi: " + Integer.toString(brBombi));
                if(g[x][y].zastavica==false)
                {
                    ++brBombi;
                    ++brBombi;
                    et.setText("Broj bombi: " + Integer.toString(brBombi));
                    /*
                    for(Button b:btns)
                        if(b.getTag().equals(Integer.toString(x)+":"+Integer.toString(y)))
                        {
                            b.setTextSize(0);
                            b.setText(Integer.toString(g[x][y].brojBombi));
                        }
                        */
                    for(ImageButton b:btns)
                        if(b.getTag().equals(Integer.toString(x)+":"+Integer.toString(y)))
                        {

                            b.setImageResource(R.drawable.neotvoren);
                            b.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


                        }
                }
                prikazi();
            }

        }
        //ako je radiobutton za flag: je li tu već postavljen flag, ako jest, removaj ga, ako nije, stavi flag

        Log.e("Igra", "button_clicked"+Integer.toString(x)+niz[1]);
    }

    private void prikazi()
    {
        for(int i=0; i<visina; i++)
            for(int j=0; j<sirina; j++)
            {
                if(g[i][j].eksplodirala)
                {
                    game_over(0);
                    Chronometer chrono = (Chronometer) findViewById(R.id.ch1);
                    chrono.stop();

                    /*
                    for(Button b:btns)
                        if(b.getTag().equals(Integer.toString(i)+":"+Integer.toString(j)))
                        {
                            b.setText("Bum");
                            b.setBackgroundColor(Color.RED);
                        }*/

                    for(ImageButton b:btns)
                        if(b.getTag().equals(Integer.toString(i)+":"+Integer.toString(j)))
                        {
                            b.setImageResource(R.drawable.eksplodirala);
                        }

                }
                if(g[i][j].otkriveno)
                {
                    /*
                    for(Button b:btns)
                    if(b.getTag().equals(Integer.toString(i)+":"+Integer.toString(j)))
                        b.setTextSize(12);
                    */
                    int bombe = g[i][j].brojBombi;
                    for(ImageButton b:btns)
                        if(b.getTag().equals(Integer.toString(i)+":"+Integer.toString(j)))
                        {
                            if(bombe==-1 && g[i][j].eksplodirala==false) b.setImageResource(R.drawable.bomba);
                            if(bombe==0) b.setImageResource(R.drawable.nula);
                            if(bombe==1) b.setImageResource(R.drawable.jedan);
                            if(bombe==2) b.setImageResource(R.drawable.dva);
                            if(bombe==3) b.setImageResource(R.drawable.tri);
                            if(bombe==4) b.setImageResource(R.drawable.cetiri);
                            if(bombe==5) b.setImageResource(R.drawable.pet);
                            if(bombe==6) b.setImageResource(R.drawable.sest);
                            if(bombe==7) b.setImageResource(R.drawable.sedam);
                            if(bombe==8) b.setImageResource(R.drawable.osam);
                        }

                }
                if(g[i][j].zastavica)
                {
                    /*
                    for(Button b:btns)
                        if(b.getTag().equals(Integer.toString(i)+":"+Integer.toString(j)))
                        {
                            b.setTextSize(12);
                            b.setText("F");
                        }
                    */
                    for(ImageButton b:btns)
                        if(b.getTag().equals(Integer.toString(i)+":"+Integer.toString(j)))
                        {
                            b.setImageResource(R.drawable.flag);
                        }
                }
            }
    }
    public void restart()
    {
        Intent mIntent=getIntent();
        finish();
        startActivity(mIntent);
    }

    public void game_over(int x)
    {
        //ako je x==0 onda smo izgubili, inače smo pobijedili
        gotovo = 1;
        if(x==0)
        {
            AlertDialog.Builder builder;
            builder=new AlertDialog.Builder(this);
            builder.setMessage("Želite li početi ispočetka?")
                    .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //u ovom slučaju počinjemo ispočetka
                            restart();
                        }
                    })
                    .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        else {
            Log.e("Generator", "pobjeda!!!!!");
            Chronometer chrono = (Chronometer) findViewById(R.id.ch1);
            chrono.stop();
            Intent intent = new Intent(this, Pobjeda.class);
            intent.putExtra("vrijeme", (int)proteklo );
            intent.putExtra("bombe", brBombiPoc);
            startActivity(intent);
            finish();
        }
    }
}