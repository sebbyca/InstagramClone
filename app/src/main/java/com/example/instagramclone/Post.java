package com.example.instagramclone;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_USERS_LIKED = "usersThatLiked";

    private static final String TAG = "Post";

    private String description;
    private ParseFile image;
    private ParseUser user;
    private String createdAt;
    private List<String> likes = new ArrayList<>();

    public String getDescription() {
        description = getString(KEY_DESCRIPTION);
        return description;
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        image = getParseFile(KEY_IMAGE);
        return image;
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        user = getParseUser(KEY_USER);
        return user;
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public List<String> getUsersLiked() {
        likes = getList(KEY_USERS_LIKED);
        return likes;
    }

    public boolean currentUserLiked() {
        for (int i = 0; i < likes.size(); i++) {
            if (ParseUser.getCurrentUser().getUsername().equals(likes.get(i))) return true;
        }
        return false;
    }

    public void addUserLiked(final Context context, String username) {
        likes.add(username);
        put(KEY_USERS_LIKED, likes);
        saveData(context);
    }

    public void removeUserLiked(final Context context, String username) {
        likes.remove(username);
        put(KEY_USERS_LIKED, likes);
        saveData(context);
    }

    private void saveData(final Context context) {
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving data", e);
                    Toast.makeText(context, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getDate() {
        createdAt = getCreatedAt().toString();

        String parseFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(parseFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }
}
