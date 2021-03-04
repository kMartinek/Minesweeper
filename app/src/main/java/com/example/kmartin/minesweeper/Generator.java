package com.example.kmartin.minesweeper;

import android.content.Context;

import java.util.Random;

/**
 * Created by kmartin on 2/6/19.
 */

public class Generator {

    private static Context context;
    public Generator(Context c)
    {
        this.context=c;
    }

    public static cell [][] generate(int brojBombi, int sirina, int visina)
    {
        //Timer time=new Timer();
        long starttime=0;
        //kreiramo dvodimenzionalno polje zadane sirine i visine
        cell[][] grid = new cell[sirina][visina];
        for (int i = 0; i < sirina; i++)
            grid[i] = new cell[visina];
        for(int i=0; i<sirina; i++)
            for(int j=0; j<visina; j++)
                grid[i][j]=new cell(i, j);

        //i u njega trpamo bombe na nasumična mjesta, za to nam treba ovaj random broj
        Random r = new Random();
        while (brojBombi>0)
        {
            int x=r.nextInt(sirina);
            int y=r.nextInt(visina);
            //ako je na to mjesto već postavljena bomba, onda ne možemo staviti novu bombu
            if(grid[x][y].bomba==false)
            {
                grid[x][y].bomba=true;
                brojBombi--;
            }
        }


        //trebamo za svako mjesto izbrojati bombe koje se nalaze oko njega kako bismo mogli napisati koji broj stoji pored bombe,
        //tome će nam služiti funkcija izbrojSusjedneBombe
        for(int k=0; k<sirina; k++)
        {
            for (int m = 0; m < visina; m++) {
                grid[k][m].brojBombi = izbrojSusjedneBombe(grid, k, m, sirina, visina);
                //Log.e("Generator", "["+grid[k][m]+"]");//ovo je samo za provjeru da vidim radi li i čini mi se ok, al ne ispise lijepo pa ćemo ispocetka
            }
        }

        return grid;

    }


    private static int izbrojSusjedneBombe(cell[][]grid, int x, int y, int sirina, int visina)
    {
        if(grid[x][y].bomba==true)return -1;//ako je na polju koje promatramo bomba, to nas ne zanima

        int brojac=0;
        //za polja okolo ovog kojeg promatramo provjeravamo je li mina na njima, ako jest, dodamo je u brojac.
        if(JeLiMina(grid,x-1,y-1,sirina, visina)) brojac++;
        if(JeLiMina(grid,x,y-1,sirina, visina)) brojac++;
        if(JeLiMina(grid,x+1,y-1,sirina, visina)) brojac++;
        if(JeLiMina(grid,x-1,y,sirina, visina)) brojac++;
        if(JeLiMina(grid,x+1,y,sirina, visina)) brojac++;
        if(JeLiMina(grid,x-1,y+1,sirina, visina)) brojac++;
        if(JeLiMina(grid,x+1,y+1,sirina, visina)) brojac++;
        if(JeLiMina(grid,x,y+1,sirina, visina)) brojac++;

        return brojac;

    }
    //ovu dodatnu funkciju radimo kako bismo izbjegli silne provjere gore
    private static boolean JeLiMina(cell [][]grid, int x, int y, int sirina, int visina)
    {
        if(x>=0 && y>=0 && x<sirina && y<visina)
        {
            if(grid[x][y].bomba==true)
                return true;
        }
        return false;
    }


    public static void Otvori(cell [][]grid, int x, int y, int sirina, int visina)
    {
        if(x>=0 && y>=0 && x<sirina && y<visina)
        {
            if (grid[x][y].zastavica == true || grid[x][y].otkriveno == true) return;
            if (grid[x][y].bomba == true)
            {
                grid[x][y].otkriveno=true;
                grid[x][y].eksplodirala=true;
                OtvoriBombe(grid, sirina,visina);
                return;
            }
            else if (grid[x][y].brojBombi > 0)
            {
                grid[x][y].otkriveno = true;
                return;
            }
            else if (grid[x][y].brojBombi == 0)
            {
                grid[x][y].otkriveno = true;
                Otvori(grid, x - 1, y - 1, sirina, visina);
                Otvori(grid, x , y - 1, sirina, visina);
                Otvori(grid, x + 1, y - 1, sirina, visina);
                Otvori(grid, x - 1, y , sirina, visina);
                Otvori(grid, x + 1, y , sirina, visina);
                Otvori(grid, x - 1, y + 1, sirina, visina);
                Otvori(grid, x , y + 1, sirina, visina);
                Otvori(grid, x + 1, y + 1, sirina, visina);
                return;
            }
        }
        return;
    }

    public static void zastavica(cell [][]grid, int x, int y)
    {
        if(grid[x][y].zastavica==true)
        {
            grid[x][y].zastavica = false;
        }
        else
            grid[x][y].zastavica=true;
        return;
    }

    public static void dugiKLik(cell [][]grid, int x, int y, int sirina, int visina)
    {
        int flagovi=0;
        int bombe=0;
        if(grid[x][y].otkriveno)
        {
            flagovi=izbrojSusjedneFlagove(grid,x,y,sirina, visina);
            bombe=izbrojSusjedneBombe(grid,x,y,sirina,visina);
            if(flagovi==bombe)
            {
                Otvori(grid, x - 1, y - 1, sirina, visina);
                Otvori(grid, x , y - 1, sirina, visina);
                Otvori(grid, x + 1, y - 1, sirina, visina);
                Otvori(grid, x - 1, y , sirina, visina);
                Otvori(grid, x + 1, y , sirina, visina);
                Otvori(grid, x - 1, y + 1, sirina, visina);
                Otvori(grid, x , y + 1, sirina, visina);
                Otvori(grid, x + 1, y + 1, sirina, visina);
            }
        }
    }



    private static int izbrojSusjedneFlagove(cell[][]grid, int x, int y, int sirina, int visina)
    {

        int brojac=0;
        //za polja okolo ovog kojeg promatramo provjeravamo je li flag na njima, ako jest, dodamo je u brojac.
        if(JeLiFlag(grid,x-1,y-1,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x,y-1,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x+1,y-1,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x-1,y,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x+1,y,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x-1,y+1,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x+1,y+1,sirina, visina)) brojac++;
        if(JeLiFlag(grid,x,y+1,sirina, visina)) brojac++;

        return brojac;

    }
    //ovu dodatnu funkciju radimo kako bismo izbjegli silne provjere gore
    private static boolean JeLiFlag(cell [][]grid, int x, int y, int sirina, int visina)
    {
        if(x>=0 && y>=0 && x<sirina && y<visina)
        {
            if(grid[x][y].zastavica==true)
                return true;
        }
        return false;
    }

    public static void OtvoriBombe(cell [][]grid, int sirina, int visina)
    {
        for(int i = 0; i<visina; ++i)
        {
            for(int j = 0; j < sirina; ++j)
            {
                if(grid[i][j].eksplodirala==true) grid[i][j].otkriveno=true;
                if(grid[i][j].bomba==true && grid[i][j].zastavica==false) grid[i][j].otkriveno=true;
            }
        }
    }

    public static boolean ProvjeriKraj(cell [][]grid, int sirina, int visina, int brBombi)
    {
        int brojOtvorenih = 0;
        for (int i = 0; i < visina; ++i){
            for(int j = 0; j < sirina; ++j){
                if(grid[i][j].otkriveno==true && grid[i][j].bomba==false) ++brojOtvorenih;
            }
        }
        if (brojOtvorenih == sirina*visina-brBombi) return true;
        else return false;
    }


}
