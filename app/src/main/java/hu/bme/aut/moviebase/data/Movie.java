package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

@Entity(tableName = "movie")
public class Movie {
    // id, name, category, length, description, rating, price

    public enum Category{
        ACTION, DOCUMENTARY, HORROR, COMEDY, ROMANCE;
    }

    @TypeConverter
    public static Category getByOrdinal(int ordinal){
        Category ret = null;
        for (Category cat: Category.values()){
            if(cat.ordinal() == ordinal){
                ret = cat;
                break;
            }
        }
        return ret;
    }

    @TypeConverter
    public static int toInt(Category category){
        return category.ordinal();
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
