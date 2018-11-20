package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE uid = 0")
    List<Movie_> getAllNoUser();

    @Query("SELECT * FROM movie WHERE uid = :id")
    List<Movie_> getAllMoviesFromUser(long id);

    @Insert
    void insert(Movie_ movies);

    @Delete
    void deleteItem(Movie_ movie);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Query("DELETE FROM movie WHERE id = :id")
    void deleteRow(long id);

    @Query("SELECT * FROM movie WHERE id = :id")
    Movie_ findMovieById(long id);
}
