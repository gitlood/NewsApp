package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
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

        // Find the article at the given position in the list of articles
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

        //using picasso to load thumbnails into imageview
        Picasso.get().load(currentArticle.getThumbnail()).into(imageView);
        newsTitle.setText(currentArticle.getTitle());

        //putting api strings into UI elements
        newsAuthor.setText(currentArticle.getAuthor());
        newsTimeStamp.setText(formatDate(currentArticle.getDate()));
        newsSection.setText(currentArticle.getSectionName());

        //listener detects clicks on cardview and goes to webpage for article
        CardView card = itemView.findViewById(R.id.card);
        card.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(currentArticle.getWebUrl()));
            getContext().startActivity(browserIntent);
        });

        return itemView;
    }

    /**
     * This method format the date into a specific pattern.
     * @param dateObj is the web publication date.
     * @return a date formatted's string.
     */
    private String formatDate(String dateObj) {
        String dateFormatted = "";
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date newDate = inputDate.parse(dateObj);
            assert newDate != null;
            return outputDate.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }
}