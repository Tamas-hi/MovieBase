package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(
        entities = {Movie_.class, User.class},
        version = 1,
        exportSchema = false
)
@TypeConverters(value = {Movie_.Category.class})
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;
    private static final String DB_name = "movie-list";

    public static MovieDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, MovieDatabase.class, DB_name).build();
                }
            }
        }
        return instance;
    }

    public abstract MovieDao movieDao();

    public abstract UserDao userDao();
}
