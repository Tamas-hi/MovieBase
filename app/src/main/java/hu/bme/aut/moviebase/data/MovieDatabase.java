package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(
        entities = {Movie.class},
        version = 1,
        exportSchema = false
)
@TypeConverters(value = {Movie.Category.class})
public abstract class MovieDatabase extends RoomDatabase{
    public abstract MovieDao movieDao();
}
