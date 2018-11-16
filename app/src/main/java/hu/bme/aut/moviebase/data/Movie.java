package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    public Movie(){

    }
    private Movie(Parcel in){
        //id = in.readLong();
        name = in.readString();
        category = (Category) in.readSerializable();
        length = in.readInt();
        description = in.readString();
        //rating = in.readFloat();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }*/
        dest.writeString(name);
        dest.writeSerializable(category);
        dest.writeInt(length);
        dest.writeString(description);
        //dest.writeFloat(rating);
    }
    // id, name, category, length, description, rating, price

    public enum Category{
        ACTION, DOCUMENTARY, HORROR, COMEDY, ROMANCE;


        @TypeConverter
        public static Category getByOrdinal(int ordinal) {
            Category ret = null;
            for (Category cat : Category.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break;
                }
            }
            return ret;
        }

        @TypeConverter
        public static int toInt(Category category) {
            return category.ordinal();
        }
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "category")
    public Category category;

    @ColumnInfo(name = "length")
    public int length;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "rating")
    public float rating;

    @ColumnInfo(name = "price")
    public int price;

}
