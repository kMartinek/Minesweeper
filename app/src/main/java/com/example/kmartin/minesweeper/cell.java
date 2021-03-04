package com.example.kmartin.minesweeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kmartin on 2/6/19.
 */

public class cell implements Parcelable {

    int x;
    int y;
    boolean bomba = false;
    boolean otkriveno;
    boolean zastavica;
    int brojBombi;
    boolean eksplodirala;

    public cell(int i, int j) {
        this.x = i;
        this.y = j;
        this.bomba = false;
        this.otkriveno = false;
        this.zastavica = false;
        this.brojBombi = 0;
        this.eksplodirala = false;

    }

    protected cell(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        bomba = in.readByte() != 0x00;
        otkriveno = in.readByte() != 0x00;
        zastavica = in.readByte() != 0x00;
        brojBombi = in.readInt();
        eksplodirala = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeByte((byte) (bomba ? 0x01 : 0x00));
        dest.writeByte((byte) (otkriveno ? 0x01 : 0x00));
        dest.writeByte((byte) (zastavica ? 0x01 : 0x00));
        dest.writeInt(brojBombi);
        dest.writeByte((byte) (eksplodirala ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<cell> CREATOR = new Parcelable.Creator<cell>() {
        @Override
        public cell createFromParcel(Parcel in) {
            return new cell(in);
        }

        @Override
        public cell[] newArray(int size) {
            return new cell[size];
        }
    };
}
