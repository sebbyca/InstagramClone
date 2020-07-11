package com.example.instagramclone.fragments;

import android.util.Log;

import com.example.instagramclone.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    private static final String TAG = "ProfileFragment";
    private static final int MAX_POSTS_IN_TIMELINE = 20;


    @Override
    protected void queryPosts(final boolean isRefresh) {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(MAX_POSTS_IN_TIMELINE);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, isRefresh ? "Issue with getting posts on refresh" : "Issue with getting posts", e);
                    return;
                }

                allPosts.clear();
                for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();

                if (isRefresh) swipeContainer.setRefreshing(false);
            }
        });
    }
}
