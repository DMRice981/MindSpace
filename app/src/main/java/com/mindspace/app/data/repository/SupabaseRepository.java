package com.mindspace.app.data.repository;

import com.mindspace.app.data.model.User;
import com.mindspace.app.network.supabase.SupabaseApiService;
import com.mindspace.app.network.supabase.SupabaseClient;
import com.mindspace.app.network.supabase.SupabaseDtos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class SupabaseRepository {
    private final SupabaseApiService apiService;
    private final ExecutorService executorService;

    public SupabaseRepository() {
        apiService = SupabaseClient.getInstance().getApiService();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void syncProfile(User user, ProfileCallback callback) {
        executorService.execute(() -> {
            try {
                SupabaseDtos.Profile profile = findProfileSync(user.getUsername());
                if (profile == null) {
                    SupabaseDtos.ProfileInsertRequest request = new SupabaseDtos.ProfileInsertRequest(
                            user.getUsername(),
                            user.getEmail(),
                            user.getAvatar() == null ? "avatar_0" : user.getAvatar(),
                            user.isAdmin(),
                            user.isBanned()
                    );
                    Response<List<SupabaseDtos.Profile>> response = apiService.createProfile(request).execute();
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        profile = response.body().get(0);
                    }
                }
                if (callback != null) {
                    callback.onComplete(profile, profile == null ? "联网用户同步失败" : null);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onComplete(null, e.getMessage());
                }
            }
        });
    }

    public void searchUser(String username, ProfileCallback callback) {
        executorService.execute(() -> {
            try {
                SupabaseDtos.Profile profile = findProfileSync(username);
                if (callback != null) {
                    callback.onComplete(profile, null);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onComplete(null, e.getMessage());
                }
            }
        });
    }

    public void sendFriendRequest(long fromUserId, long toUserId, SimpleCallback callback) {
        executorService.execute(() -> {
            try {
                if (fromUserId == toUserId) {
                    notifySimple(callback, false, "不能添加自己");
                    return;
                }
                if (isFriendSync(fromUserId, toUserId)) {
                    notifySimple(callback, false, "已经是好友");
                    return;
                }
                SupabaseDtos.FriendRequest existing = findRequestBetweenUsersSync(fromUserId, toUserId);
                if (existing != null) {
                    notifySimple(callback, false, "已经发送过好友申请");
                    return;
                }
                Response<List<SupabaseDtos.FriendRequest>> response = apiService.createFriendRequest(new SupabaseDtos.FriendRequestInsertRequest(fromUserId, toUserId)).execute();
                notifySimple(callback, response.isSuccessful(), response.isSuccessful() ? "好友申请已发送" : "发送失败");
            } catch (Exception e) {
                notifySimple(callback, false, e.getMessage());
            }
        });
    }

    public void getIncomingRequests(long userId, RequestsCallback callback) {
        executorService.execute(() -> {
            try {
                Response<List<SupabaseDtos.FriendRequest>> response = apiService.getIncomingRequests(
                        "eq." + userId,
                        "eq.pending",
                        "*,fromProfile:profiles!friend_requests_from_user_id_fkey(*)",
                        "created_at.desc"
                ).execute();
                if (callback != null) {
                    callback.onComplete(response.isSuccessful() && response.body() != null ? response.body() : new ArrayList<>(), response.isSuccessful() ? null : "获取申请失败");
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onComplete(new ArrayList<>(), e.getMessage());
                }
            }
        });
    }

    public void acceptFriendRequest(SupabaseDtos.FriendRequest request, String currentUsername, SimpleCallback callback) {
        executorService.execute(() -> {
            try {
                SupabaseDtos.Profile fromProfile = findProfileByIdSync(request.fromUserId);
                SupabaseDtos.Profile toProfile = findProfileByIdSync(request.toUserId);
                String fromUsername = fromProfile == null ? "好友" : fromProfile.username;
                String toUsername = toProfile == null ? currentUsername : toProfile.username;
                Response<Void> updateResponse = apiService.updateFriendRequest("eq." + request.id, new SupabaseDtos.FriendRequestUpdateRequest("accepted")).execute();
                if (!updateResponse.isSuccessful()) {
                    notifySimple(callback, false, "同意申请失败");
                    return;
                }
                apiService.createFriendship(new SupabaseDtos.FriendshipInsertRequest(request.toUserId, request.fromUserId, fromUsername)).execute();
                apiService.createFriendship(new SupabaseDtos.FriendshipInsertRequest(request.fromUserId, request.toUserId, toUsername)).execute();
                notifySimple(callback, true, "已添加好友");
            } catch (Exception e) {
                notifySimple(callback, false, e.getMessage());
            }
        });
    }

    public void rejectFriendRequest(long requestId, SimpleCallback callback) {
        executorService.execute(() -> {
            try {
                Response<Void> response = apiService.updateFriendRequest("eq." + requestId, new SupabaseDtos.FriendRequestUpdateRequest("rejected")).execute();
                notifySimple(callback, response.isSuccessful(), response.isSuccessful() ? "已拒绝" : "操作失败");
            } catch (Exception e) {
                notifySimple(callback, false, e.getMessage());
            }
        });
    }

    public void getFriends(long userId, FriendsCallback callback) {
        executorService.execute(() -> {
            try {
                Response<List<SupabaseDtos.Friendship>> response = apiService.getFriends("eq." + userId, "*", "created_at.desc").execute();
                if (callback != null) {
                    callback.onComplete(response.isSuccessful() && response.body() != null ? response.body() : new ArrayList<>(), response.isSuccessful() ? null : "获取好友失败");
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onComplete(new ArrayList<>(), e.getMessage());
                }
            }
        });
    }

    public void deleteFriend(long userId, long friendId, SimpleCallback callback) {
        executorService.execute(() -> {
            try {
                String firstFilter = "and(user_id.eq." + userId + ",friend_id.eq." + friendId + ")";
                String secondFilter = "and(user_id.eq." + friendId + ",friend_id.eq." + userId + ")";
                Response<Void> firstResponse = apiService.deleteFriendship(firstFilter).execute();
                Response<Void> secondResponse = apiService.deleteFriendship(secondFilter).execute();
                boolean success = firstResponse.isSuccessful() && secondResponse.isSuccessful();
                notifySimple(callback, success, success ? "已删除好友" : "删除失败");
            } catch (Exception e) {
                notifySimple(callback, false, e.getMessage());
            }
        });
    }

    public void getConversations(long userId, ConversationsCallback callback) {
        executorService.execute(() -> {
            try {
                List<SupabaseDtos.Friendship> friends = new ArrayList<>();
                Response<List<SupabaseDtos.Friendship>> friendsResponse = apiService.getFriends("eq." + userId, "*", "created_at.desc").execute();
                if (friendsResponse.isSuccessful() && friendsResponse.body() != null) {
                    friends.addAll(friendsResponse.body());
                }

                String filter = "sender_id.eq." + userId + ",receiver_id.eq." + userId;
                Response<List<SupabaseDtos.ChatMessage>> messagesResponse = apiService.getUserChatMessages(filter, "*", "created_at.desc").execute();
                List<SupabaseDtos.ChatMessage> messages = messagesResponse.isSuccessful() && messagesResponse.body() != null ? messagesResponse.body() : new ArrayList<>();
                Map<Long, SupabaseDtos.ChatConversation> conversationMap = new HashMap<>();

                for (SupabaseDtos.Friendship friend : friends) {
                    SupabaseDtos.ChatConversation conversation = new SupabaseDtos.ChatConversation();
                    conversation.friendId = friend.friendId;
                    conversation.friendUsername = friend.friendUsername;
                    conversation.lastMessage = null;
                    conversation.lastMessageTime = friend.createdAt;
                    conversation.unreadCount = 0;
                    conversationMap.put(friend.friendId, conversation);
                }

                for (SupabaseDtos.ChatMessage message : messages) {
                    long friendId = message.senderId == userId ? message.receiverId : message.senderId;
                    SupabaseDtos.ChatConversation conversation = conversationMap.get(friendId);
                    if (conversation == null) {
                        SupabaseDtos.Profile profile = findProfileByIdSync(friendId);
                        conversation = new SupabaseDtos.ChatConversation();
                        conversation.friendId = friendId;
                        conversation.friendUsername = profile == null ? "用户 " + friendId : profile.username;
                        conversation.unreadCount = 0;
                        conversationMap.put(friendId, conversation);
                    }
                    if (conversation.lastMessage == null) {
                        conversation.lastMessage = message.content;
                        conversation.lastMessageTime = message.createdAt;
                    }
                    if (message.receiverId == userId && !message.isRead) {
                        conversation.unreadCount++;
                    }
                }

                List<SupabaseDtos.ChatConversation> conversations = new ArrayList<>(conversationMap.values());
                if (callback != null) {
                    callback.onComplete(conversations, null);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onComplete(new ArrayList<>(), e.getMessage());
                }
            }
        });
    }

    public void markMessagesRead(long currentUserId, long friendId, SimpleCallback callback) {
        executorService.execute(() -> {
            try {
                Response<Void> response = apiService.markMessagesRead("eq." + friendId, "eq." + currentUserId, "eq.false", new SupabaseDtos.ChatMessageReadRequest(true)).execute();
                notifySimple(callback, response.isSuccessful(), response.isSuccessful() ? "已读" : "标记已读失败");
            } catch (Exception e) {
                notifySimple(callback, false, e.getMessage());
            }
        });
    }

    public void getChatMessages(long userId, long friendId, MessagesCallback callback) {
        executorService.execute(() -> {
            try {
                String filter = "and(sender_id.eq." + userId + ",receiver_id.eq." + friendId + "),and(sender_id.eq." + friendId + ",receiver_id.eq." + userId + ")";
                Response<List<SupabaseDtos.ChatMessage>> response = apiService.getChatMessages(filter, "*", "created_at.asc").execute();
                if (callback != null) {
                    callback.onComplete(response.isSuccessful() && response.body() != null ? response.body() : new ArrayList<>(), response.isSuccessful() ? null : "获取聊天记录失败");
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onComplete(new ArrayList<>(), e.getMessage());
                }
            }
        });
    }

    public void sendMessage(long senderId, long receiverId, String content, SimpleCallback callback) {
        executorService.execute(() -> {
            try {
                Response<List<SupabaseDtos.ChatMessage>> response = apiService.sendMessage(new SupabaseDtos.ChatMessageInsertRequest(senderId, receiverId, content)).execute();
                notifySimple(callback, response.isSuccessful(), response.isSuccessful() ? "发送成功" : "发送失败");
            } catch (Exception e) {
                notifySimple(callback, false, e.getMessage());
            }
        });
    }

    private SupabaseDtos.Profile findProfileSync(String username) throws IOException {
        Response<List<SupabaseDtos.Profile>> response = apiService.findProfilesByUsername("eq." + username, "*", 1).execute();
        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
            return response.body().get(0);
        }
        return null;
    }

    private SupabaseDtos.Profile findProfileByIdSync(long id) throws IOException {
        Response<List<SupabaseDtos.Profile>> response = apiService.findProfileById("eq." + id, "*", 1).execute();
        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
            return response.body().get(0);
        }
        return null;
    }

    private boolean isFriendSync(long userId, long friendId) throws IOException {
        Response<List<SupabaseDtos.Friendship>> response = apiService.findFriendship("eq." + userId, "eq." + friendId, "id", 1).execute();
        return response.isSuccessful() && response.body() != null && !response.body().isEmpty();
    }

    private SupabaseDtos.FriendRequest findRequestBetweenUsersSync(long userId, long friendId) throws IOException {
        String filter = "and(from_user_id.eq." + userId + ",to_user_id.eq." + friendId + "),and(from_user_id.eq." + friendId + ",to_user_id.eq." + userId + ")";
        Response<List<SupabaseDtos.FriendRequest>> response = apiService.findRequestBetweenUsers(filter, "*", 1).execute();
        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
            return response.body().get(0);
        }
        return null;
    }

    private void notifySimple(SimpleCallback callback, boolean success, String message) {
        if (callback != null) {
            callback.onComplete(success, message);
        }
    }

    public interface ProfileCallback {
        void onComplete(SupabaseDtos.Profile profile, String error);
    }

    public interface RequestsCallback {
        void onComplete(List<SupabaseDtos.FriendRequest> requests, String error);
    }

    public interface FriendsCallback {
        void onComplete(List<SupabaseDtos.Friendship> friends, String error);
    }

    public interface MessagesCallback {
        void onComplete(List<SupabaseDtos.ChatMessage> messages, String error);
    }

    public interface ConversationsCallback {
        void onComplete(List<SupabaseDtos.ChatConversation> conversations, String error);
    }

    public interface SimpleCallback {
        void onComplete(boolean success, String message);
    }
}
