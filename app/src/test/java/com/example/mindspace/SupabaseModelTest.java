package com.example.mindspace;

import com.mindspace.app.network.supabase.SupabaseDtos;
import com.mindspace.app.utils.ChatUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SupabaseModelTest {
    @Test
    public void profileInsertRequestKeepsUserFields() {
        SupabaseDtos.ProfileInsertRequest request = new SupabaseDtos.ProfileInsertRequest("alice", "alice@test.com", "avatar_0", false, false);

        assertEquals("alice", request.username);
        assertEquals("alice@test.com", request.email);
        assertEquals("avatar_0", request.avatar);
        assertFalse(request.isAdmin);
        assertFalse(request.isBanned);
    }

    @Test
    public void friendshipInsertRequestKeepsFriendFields() {
        SupabaseDtos.FriendshipInsertRequest request = new SupabaseDtos.FriendshipInsertRequest(1, 2, "bob");

        assertEquals(1, request.userId);
        assertEquals(2, request.friendId);
        assertEquals("bob", request.friendUsername);
    }

    @Test
    public void chatPairKeyIsStableForBothDirections() {
        assertEquals("3_9", ChatUtils.getChatPairKey(3, 9));
        assertEquals("3_9", ChatUtils.getChatPairKey(9, 3));
    }

    @Test
    public void friendRequestStatusHelpersRecognizeStates() {
        assertTrue(ChatUtils.isPending("pending"));
        assertTrue(ChatUtils.isAccepted("accepted"));
        assertTrue(ChatUtils.isRejected("rejected"));
        assertFalse(ChatUtils.isPending("accepted"));
    }
}
