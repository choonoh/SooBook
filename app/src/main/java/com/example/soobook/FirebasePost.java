package com.example.soobook;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DowonYoon on 2017-07-11.
 */

@IgnoreExtraProperties
public class FirebasePost {
    public String title;
    public String auth;
    public Long star;
    public String gender;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String title, String auth, Long star, String gender) {

        this.title = title;
        this.auth = auth;
        this.star = star;
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("auth", auth);
        result.put("star", star);
        result.put("gender", gender);
        return result;
    }
}