package com.mindspace.app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mindspace.app.data.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User findById(int id);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Update
    void update(User user);

    @Query("DELETE FROM users WHERE id = :id")
    void delete(int id);
}
