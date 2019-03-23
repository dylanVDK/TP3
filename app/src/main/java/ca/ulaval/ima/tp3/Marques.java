package ca.ulaval.ima.tp3;

import android.os.Parcel;
import android.os.Parcelable;

public class Marques implements Parcelable {
    public String name;
    public int id;

    protected Marques(Parcel in){
        this.name = in.readString();
        this.id = in.readInt();
    }

    public Marques(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }

    public int getId() { return id; }

    public static final Creator<Marques> CREATOR = new Creator<Marques>() {
        @Override
        public Marques createFromParcel(Parcel in) {
            return new Marques(in);
        }

        @Override
        public Marques[] newArray(int size) {
            return new Marques[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
      }

}
