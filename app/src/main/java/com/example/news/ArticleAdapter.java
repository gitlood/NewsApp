package com.example.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_view, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        Article currentArticle = getItem(position);

        // View variable declaration for UI elements
        final ImageView imageView;
        final TextView newsTitle;
        final TextView newsAuthor;
        final TextView newsTimeStamp;
        final TextView newsSection;

        // Fetching View IDs from ID resource
        imageView = itemView.findViewById(R.id.newsImage);
        imageView.setClipToOutline(true);
        newsTitle = itemView.findViewById(R.id.news_text);
        newsTitle.setMaxLines(2);
        newsAuthor = itemView.findViewById(R.id.author_text);
        newsTimeStamp = itemView.findViewById(R.id.timestamp_text);
        newsSection = itemView.findViewById(R.id.section_text);

        Picasso.get().load(currentArticle.getThumbnail()).into(imageView);
        newsTitle.setText(currentArticle.getTitle());

        newsAuthor.setText(currentArticle.getAuthor());
        newsTimeStamp.setText(currentArticle.getDate());
        newsSection.setText(currentArticle.getSectionName());

        return itemView;
    }
}
