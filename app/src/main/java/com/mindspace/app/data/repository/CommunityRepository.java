package com.mindspace.app.data.repository;

import android.content.Context;

import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.local.CommunityPostDao;
import com.mindspace.app.data.local.CommentDao;
import com.mindspace.app.data.model.CommunityPost;
import com.mindspace.app.data.model.Comment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommunityRepository {
    private CommunityPostDao postDao;
    private CommentDao commentDao;
    private ExecutorService executorService;

    public CommunityRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        postDao = db.communityPostDao();
        commentDao = db.commentDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertPost(CommunityPost post, InsertCallback callback) {
        executorService.execute(() -> {
            long id = postDao.insert(post);
            post.setId((int) id);
            if (callback != null) {
                callback.onComplete(post);
            }
        });
    }

    public void getActivePosts(PostsCallback callback) {
        executorService.execute(() -> {
            List<CommunityPost> posts = postDao.getActivePosts();
            if (callback != null) {
                callback.onComplete(posts);
            }
        });
    }

    public void getReportedPosts(PostsCallback callback) {
        executorService.execute(() -> {
            List<CommunityPost> posts = postDao.getReportedPosts();
            if (callback != null) {
                callback.onComplete(posts);
            }
        });
    }

    public void incrementLike(int postId) {
        executorService.execute(() -> postDao.incrementLike(postId));
    }

    public void incrementCommentCount(int postId) {
        executorService.execute(() -> postDao.incrementCommentCount(postId));
    }

    public void reportPost(int postId) {
        executorService.execute(() -> {
            CommunityPost post = postDao.getActivePosts().stream()
                .filter(p -> p.getId() == postId)
                .findFirst()
                .orElse(null);
            if (post != null) {
                post.setReported(true);
                postDao.update(post);
            }
        });
    }

    public void deletePost(int postId) {
        executorService.execute(() -> postDao.delete(postId));
    }

    public void insertComment(Comment comment, InsertCallback callback) {
        executorService.execute(() -> {
            long id = commentDao.insert(comment);
            comment.setId((int) id);
            if (callback != null) {
                callback.onComplete(comment);
            }
        });
    }

    public void getCommentsByPost(int postId, CommentsCallback callback) {
        executorService.execute(() -> {
            List<Comment> comments = commentDao.getCommentsByPost(postId);
            if (callback != null) {
                callback.onComplete(comments);
            }
        });
    }

    public void getReportedComments(CommentsCallback callback) {
        executorService.execute(() -> {
            List<Comment> comments = commentDao.getReportedComments();
            if (callback != null) {
                callback.onComplete(comments);
            }
        });
    }

    public void deleteComment(int commentId) {
        executorService.execute(() -> commentDao.delete(commentId));
    }

    public interface InsertCallback {
        void onComplete(Object obj);
    }

    public interface PostsCallback {
        void onComplete(List<CommunityPost> posts);
    }

    public interface CommentsCallback {
        void onComplete(List<Comment> comments);
    }
}
