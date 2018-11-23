package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    User findUserById(long id);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();
}
