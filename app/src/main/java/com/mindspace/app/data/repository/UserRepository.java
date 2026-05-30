package com.mindspace.app.data.repository;

import android.content.Context;

import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.local.UserDao;
import com.mindspace.app.data.model.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(User user, InsertCallback callback) {
        executorService.execute(() -> {
            long id = userDao.insert(user);
            user.setId((int) id);
            if (callback != null) {
                callback.onComplete(user);
            }
        });
    }

    public void login(String username, String password, LoginCallback callback) {
        executorService.execute(() -> {
            User user = userDao.login(username, password);
            if (callback != null) {
                callback.onComplete(user);
            }
        });
    }

    public void findByUsername(String username, FindCallback callback) {
        executorService.execute(() -> {
            User user = userDao.findByUsername(username);
            if (callback != null) {
                callback.onComplete(user);
            }
        });
    }

    public void getAllUsers(UsersCallback callback) {
        executorService.execute(() -> {
            List<User> users = userDao.getAllUsers();
            if (callback != null) {
                callback.onComplete(users);
            }
        });
    }

    public void delete(int userId) {
        executorService.execute(() -> userDao.delete(userId));
    }

    public void initDefaultUsers(InitCallback callback) {
        executorService.execute(() -> {
            User existingAdmin = userDao.findByUsername("admin");
            if (existingAdmin == null) {
                User admin = new User("admin", "admin123", "admin@mindspace.com", true);
                userDao.insert(admin);
            }
            
            User existingUser = userDao.findByUsername("user");
            if (existingUser == null) {
                User user = new User("user", "user123", "user@mindspace.com", false);
                userDao.insert(user);
            }
            
            if (callback != null) {
                callback.onComplete();
            }
        });
    }

    public interface InsertCallback {
        void onComplete(User user);
    }

    public interface LoginCallback {
        void onComplete(User user);
    }

    public interface FindCallback {
        void onComplete(User user);
    }

    public interface UsersCallback {
        void onComplete(List<User> users);
    }
    
    public interface InitCallback {
        void onComplete();
    }
}
