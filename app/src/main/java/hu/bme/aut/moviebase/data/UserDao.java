package hu.bme.aut.moviebase.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE email = :email")
    User findUserByEmail(String email);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User users);

    @Query("UPDATE user SET money = :money WHERE id =:id")
    void update(long id, int money);

   @Delete
   void delete(User user);

   @Query("DELETE FROM user WHERE email = :email")
   void deleteRow(String email);

    @Query("DELETE FROM user")
    void deleteAll();

    @Insert(onConflict = REPLACE)
    void insertAll(List<User> users);

    @Query("DELETE FROM user WHERE id= :id AND money = 30000")
    void deleteOld(long id);
}
