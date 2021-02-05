package com.example.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasefriendPost {
    public String user_uid, email, uid, nickname;

    public FirebasefriendPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasefriendPost(String user_uid, String email, String uid, String nickname) {
        this.user_uid = user_uid;
        this.email = email;
        this.uid = uid;
        this.nickname = nickname;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_uid", user_uid);
        result.put("email", email);
        result.put("uid", uid);
        result.put("nickname", nickname);

        return result;
    }
}