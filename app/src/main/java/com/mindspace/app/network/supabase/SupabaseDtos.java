package com.mindspace.app.network.supabase;

import com.google.gson.annotations.SerializedName;

public class SupabaseDtos {
    public static class Profile {
        public long id;
        public String username;
        public String email;
        public String avatar;
        @SerializedName("is_admin")
        public boolean isAdmin;
        @SerializedName("is_banned")
        public boolean isBanned;
        @SerializedName("created_at")
        public String createdAt;
    }

    public static class ProfileInsertRequest {
        public String username;
        public String email;
        public String avatar;
        @SerializedName("is_admin")
        public boolean isAdmin;
        @SerializedName("is_banned")
        public boolean isBanned;

        public ProfileInsertRequest(String username, String email, String avatar, boolean isAdmin, boolean isBanned) {
            this.username = username;
            this.email = email;
            this.avatar = avatar;
            this.isAdmin = isAdmin;
            this.isBanned = isBanned;
        }
    }

    public static class FriendRequest {
        public long id;
        @SerializedName("from_user_id")
        public long fromUserId;
        @SerializedName("to_user_id")
        public long toUserId;
        public String status;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("updated_at")
        public String updatedAt;
        public Profile fromProfile;
        public Profile toProfile;
    }

    public static class FriendRequestInsertRequest {
        @SerializedName("from_user_id")
        public long fromUserId;
        @SerializedName("to_user_id")
        public long toUserId;
        public String status;

        public FriendRequestInsertRequest(long fromUserId, long toUserId) {
            this.fromUserId = fromUserId;
            this.toUserId = toUserId;
            this.status = "pending";
        }
    }

    public static class FriendRequestUpdateRequest {
        public String status;

        public FriendRequestUpdateRequest(String status) {
            this.status = status;
        }
    }

    public static class Friendship {
        public long id;
        @SerializedName("user_id")
        public long userId;
        @SerializedName("friend_id")
        public long friendId;
        @SerializedName("friend_username")
        public String friendUsername;
        @SerializedName("created_at")
        public String createdAt;
    }

    public static class FriendshipInsertRequest {
        @SerializedName("user_id")
        public long userId;
        @SerializedName("friend_id")
        public long friendId;
        @SerializedName("friend_username")
        public String friendUsername;

        public FriendshipInsertRequest(long userId, long friendId, String friendUsername) {
            this.userId = userId;
            this.friendId = friendId;
            this.friendUsername = friendUsername;
        }
    }

    public static class ChatMessage {
        public long id;
        @SerializedName("sender_id")
        public long senderId;
        @SerializedName("receiver_id")
        public long receiverId;
        public String content;
        @SerializedName("is_read")
        public boolean isRead;
        @SerializedName("created_at")
        public String createdAt;
    }

    public static class ChatMessageInsertRequest {
        @SerializedName("sender_id")
        public long senderId;
        @SerializedName("receiver_id")
        public long receiverId;
        public String content;

        public ChatMessageInsertRequest(long senderId, long receiverId, String content) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.content = content;
        }
    }

    public static class ChatMessageReadRequest {
        @SerializedName("is_read")
        public boolean isRead;

        public ChatMessageReadRequest(boolean isRead) {
            this.isRead = isRead;
        }
    }

    public static class ChatConversation {
        public long friendId;
        public String friendUsername;
        public String lastMessage;
        public String lastMessageTime;
        public int unreadCount;
    }
}
