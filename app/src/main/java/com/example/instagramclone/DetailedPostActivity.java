package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DetailedPostActivity extends AppCompatActivity {

    private static final String TAG = "DetailedPostActivity";

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvDate;

    private String postId;

    private ImageButton btnLike;
    private ImageButton btnComment;
    private ImageButton btnForward;
    private ImageButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);

        postId = getIntent().getStringExtra("post");

        btnLike = findViewById(R.id.btnLike);
        btnComment = findViewById(R.id.btnComment);
        btnForward = findViewById(R.id.btnForward);
        btnSave = findViewById(R.id.btnSave);

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.getInBackground(postId, new GetCallback<Post>() {
            public void done(final Post post, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Showing more details on following post: " + post);
                    tvDescription.setText(post.getDescription());
                    tvDate.setText(post.getDate());

                    String username = "";
                    try {
                        username = post.getUser().fetchIfNeeded().getUsername();
                    } catch (ParseException ex) {
                        Log.e(TAG, "Error occurred while retrieving user", ex);
                    }

                    tvUsername.setText(username);

                    final SpannableStringBuilder sb = new SpannableStringBuilder(username + " " + post.getDescription());
                    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                    sb.setSpan(bss, 0, username.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tvDescription.setText(sb);

                    ParseFile image = post.getImage();
                    if (image != null) {
                        Glide.with(DetailedPostActivity.this).load(image.getUrl()).into(ivImage);
                    }

                    post.getUsersLiked();
                    btnLike.setSelected(post.currentUserLiked());
                    btnLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnLike.setSelected(!btnLike.isSelected());
                            if (btnLike.isSelected()) {
                                post.addUserLiked(DetailedPostActivity.this, ParseUser.getCurrentUser().getUsername());
                            } else {
                                post.removeUserLiked(DetailedPostActivity.this, ParseUser.getCurrentUser().getUsername());
                            }
                        }
                    });


                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnSave.setSelected(!btnSave.isSelected());
                        }
                    });
                }
            }
        });
    }
}