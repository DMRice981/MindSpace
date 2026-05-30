package com.mindspace.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mindspace.app.data.model.CommunityPost;
import com.mindspace.app.data.model.Comment;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.data.model.Note;
import com.mindspace.app.data.model.User;

@Database(
    entities = {MoodRecord.class, Note.class, User.class, CommunityPost.class, Comment.class}, 
    version = 6, 
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "mindspace_db";
    private static volatile AppDatabase INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE mood_records ADD COLUMN weather TEXT");
        }
    };
    
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("DROP TABLE IF EXISTS users");
            } catch (Exception e) {
            }
        }
    };
    
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "username TEXT," +
                    "password TEXT," +
                    "email TEXT," +
                    "avatar TEXT," +
                    "isAdmin INTEGER DEFAULT 0," +
                    "createdAt INTEGER)");
                
                database.execSQL("CREATE TABLE IF NOT EXISTS community_posts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "userId INTEGER NOT NULL," +
                    "username TEXT," +
                    "content TEXT," +
                    "mood TEXT," +
                    "createdAt INTEGER," +
                    "likeCount INTEGER DEFAULT 0," +
                    "commentCount INTEGER DEFAULT 0," +
                    "isReported INTEGER DEFAULT 0)");
                
                database.execSQL("CREATE TABLE IF NOT EXISTS comments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "postId INTEGER NOT NULL," +
                    "userId INTEGER NOT NULL," +
                    "username TEXT," +
                    "content TEXT," +
                    "createdAt INTEGER," +
                    "isReported INTEGER DEFAULT 0)");
            } catch (Exception e) {
            }
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("DROP TABLE IF EXISTS users");
                database.execSQL("CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "username TEXT," +
                    "password TEXT," +
                    "email TEXT," +
                    "avatar TEXT," +
                    "isAdmin INTEGER NOT NULL DEFAULT 0," +
                    "createdAt INTEGER NOT NULL)");
            } catch (Exception e) {
            }
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("DROP TABLE IF EXISTS users");
                database.execSQL("CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "username TEXT," +
                    "password TEXT," +
                    "email TEXT," +
                    "avatar TEXT," +
                    "isAdmin INTEGER NOT NULL DEFAULT 0," +
                    "createdAt INTEGER NOT NULL)");
                
                database.execSQL("DROP TABLE IF EXISTS community_posts");
                database.execSQL("CREATE TABLE community_posts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "userId INTEGER NOT NULL," +
                    "username TEXT," +
                    "content TEXT," +
                    "mood TEXT," +
                    "createdAt INTEGER NOT NULL," +
                    "likeCount INTEGER NOT NULL DEFAULT 0," +
                    "commentCount INTEGER NOT NULL DEFAULT 0," +
                    "isReported INTEGER NOT NULL DEFAULT 0)");
                
                database.execSQL("DROP TABLE IF EXISTS comments");
                database.execSQL("CREATE TABLE comments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "postId INTEGER NOT NULL," +
                    "userId INTEGER NOT NULL," +
                    "username TEXT," +
                    "content TEXT," +
                    "createdAt INTEGER NOT NULL," +
                    "isReported INTEGER NOT NULL DEFAULT 0)");
            } catch (Exception e) {
            }
        }
    };

    public abstract MoodDao moodDao();
    public abstract NoteDao noteDao();
    public abstract UserDao userDao();
    public abstract CommunityPostDao communityPostDao();
    public abstract CommentDao commentDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, 
                            DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
