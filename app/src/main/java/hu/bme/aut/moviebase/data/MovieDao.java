package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    List<Movie_> getAll();

    @Insert
    long insert(Movie_ movies);

    @Update
    void update(Movie_ movie);

    @Delete
    void deleteItem(Movie_ movie);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Query("DELETE FROM movie WHERE name = :name")
    void deleteRow(String name);

    @Query("SELECT * FROM movie WHERE name = :name")
    Movie_ findMovieByName(String name);
}
