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
    public String isbn;
    public String title;
    public String auth;
    public Long star;
    public String rec;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String isbn, String title, String auth, Long star, String rec) {
        this.isbn = isbn;
        this.title = title;
        this.auth = auth;
        this.star = star;
        this.rec = rec;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("isbn",isbn);
        result.put("title", title);
        result.put("auth", auth);
        result.put("star", star);
        result.put("rec", rec);
        return result;
    }
}