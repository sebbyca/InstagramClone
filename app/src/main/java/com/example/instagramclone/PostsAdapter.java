package com.example.instagramclone;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvDate;

        private ImageButton btnLike;
        private ImageButton btnComment;
        private ImageButton btnForward;
        private ImageButton btnSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);

            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnForward = itemView.findViewById(R.id.btnForward);
            btnSave = itemView.findViewById(R.id.btnSave);

            // Creates an on-click listener for each IV
            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            // Bind the post data to the view elements
            final String username = post.getUser().getUsername();

            final SpannableStringBuilder sb = new SpannableStringBuilder(username + " " + post.getDescription());
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            sb.setSpan(bss, 0, username.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            tvDescription.setText(sb);

            tvUsername.setText(username);
            tvDate.setText(post.getDate());

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            post.getUsersLiked();
            btnLike.setSelected(post.currentUserLiked());
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnLike.setSelected(!btnLike.isSelected());
                    if (btnLike.isSelected()) {
                        post.addUserLiked(context, ParseUser.getCurrentUser().getUsername());
                    } else {
                        post.removeUserLiked(context, ParseUser.getCurrentUser().getUsername());
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

        // When the user clicks on a row, show MoreInfoActivity for the selected movie
        @Override
        public void onClick(View view) {
            // Get item position within the view
            int position = getAdapterPosition();

            // Make sure the position exists within the view
            if (position != RecyclerView.NO_POSITION) {
                // Retrieve movie at position
                Post post = posts.get(position);

                // Create intent for the "more info" activity
                Intent intent = new Intent(context, DetailedPostActivity.class);

                // Serialize the movie using parceler, use its short name as a key
                intent.putExtra("post", post.getObjectId());

                // Display the activity
                context.startActivity(intent);
            }
        }
    }
}
