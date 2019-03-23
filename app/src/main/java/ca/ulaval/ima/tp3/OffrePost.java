package ca.ulaval.ima.tp3;

import android.os.Parcel;
import android.os.Parcelable;

public class OffrePost implements Parcelable {
    int km;
    int year;
    int price;
    String tr;
    int model;
    String kmSTR;
    String yearSTR;
    String priceSTR;
    String modelSTR;

    public int StrToId(String name) {
        String[] tab = {"Acura", "Aston", "Martin", "Audi", "BMW", "Buick", "Cadillac", "Chevrolet","Chrysler", "Dodge", "Ferarri",
                "Ford", "GMC", "Honda", "Hummer", "Hyundai","Isuzu", "Jeep", "Kia", "Land Rover", "Lotus", "Maserati", "Mazda", "Mercedes-Benz",
                "Nissan", "Porsche", "Rolls-Royce", "Saab", "Saturn", "Scion", "Smart", "Subaru","Toyota", "Volkswagon", "Volvo"};
        int i = 0;
        while (i < tab.length){
            if (name.equals(tab[i]))
                return i + 1;
            i++;
        }
        return 1;
    }
    protected OffrePost(Parcel in){
         kmSTR = in.readString();
         yearSTR = in.readString();
         priceSTR = in.readString();
         modelSTR = in.readString();

        km = Integer.valueOf(kmSTR);
        year = Integer.valueOf(yearSTR);
        price = Integer.valueOf(priceSTR);
        tr = in.readString();
        model = StrToId(modelSTR);
    }
    public OffrePost(String kmField, String yearField ,String priceField,String trField,String modelField){
        km = Integer.parseInt(kmField);
        year = Integer.valueOf(yearField);
        price = Integer.valueOf(priceField);
        tr = trField;
        model = Integer.valueOf(modelField);
    }

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
        parcel.writeString(kmSTR);
        parcel.writeString(yearSTR);
        parcel.writeString(priceSTR);
        parcel.writeString(tr);
        parcel.writeString(modelSTR);
    }

    public int getKm() {
        return km;
    }

    public int getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public String getTr() {
        return tr;
    }
}
