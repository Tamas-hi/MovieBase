package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE email = :email")
    User findUserByEmail(String email);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    long insert(User users);

    @Update
    void update(User... user);

    @Delete
    void delete(User... user);
}
