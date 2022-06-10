package com.example.news;

import static android.content.ContentValues.TAG;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    private static final String GUARDIAN_REQ_URL = "http://content.guardianapis.com/search";
    private final String API_KEY = "357729b5-a772-4033-b233-d312ce47992e";
    private static final String QUERY_SEARCH = "q";
    private static final String QUERY_API_KEY = "api-key";
    private static final String QUERY_THUMBNAIL = "show-fields";
    private static final String QUERY_AUTHOR = "show-tags";
    private static final String THUMBNAIL_VALUE = "thumbnail";
    private static final String AUTHOR_VALUE = "contributor";
    private static final String QUERY_PAGE = "page-size";

    /**
     * Constants string variables storing API call back responses
     */

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int ARTICLE_LOADER_ID = 1;

    public String Url;

    private ArticleAdapter articleAdapters;
    private TextView emptyView;
    private ProgressBar progressbar;
    private ListView articleListView;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleListView = findViewById(R.id.list);
        emptyView = findViewById(R.id.empty_view);
        progressbar = findViewById(R.id.progressbar);

        articleListView.setEmptyView(emptyView);

        articleAdapters = new ArticleAdapter(this, new ArrayList<>());

        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = getLoaderManager();
        //Parsing the base URL for Guardian API
        Uri.Builder builder = Uri.parse(GUARDIAN_REQ_URL).buildUpon();

        //Setting up the query parameters - api key, topic for the news, author name and news thumbnail
        int initialPage = 50;
        builder
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .appendQueryParameter(QUERY_PAGE, String.valueOf(initialPage))
                .appendQueryParameter(QUERY_AUTHOR, AUTHOR_VALUE)
                .appendQueryParameter(QUERY_THUMBNAIL, THUMBNAIL_VALUE);

        // If there is a network connection, fetch data
        if (checkOnlineStatus()) {
            Log.d(TAG, "onQueryTextSubmit: internet is available");
            emptyView.setVisibility(View.INVISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            Url = builder.toString();
            Log.d(TAG, "onQueryTextSubmit: " + Url);
            articleAdapters.clear();
            loaderManager.initLoader(1, null, MainActivity.this);
        } else {
            emptyView.setText(R.string.no_internet_connection);
            articleAdapters.clear();
            progressbar.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(this, Url);
    }

    public String searchQueryURL(String searchQuery) {
        //Parsing the base URL for Guardian API
        Uri.Builder builder = Uri.parse(GUARDIAN_REQ_URL).buildUpon();

        //Setting up the query parameters - api key, topic for the news, author name and news thumbnail
        int initialPage = 50;
        builder.appendQueryParameter(QUERY_SEARCH, searchQuery)
                .appendQueryParameter(QUERY_API_KEY, API_KEY)
                .appendQueryParameter(QUERY_PAGE, String.valueOf(initialPage))
                .appendQueryParameter(QUERY_AUTHOR, AUTHOR_VALUE)
                .appendQueryParameter(QUERY_THUMBNAIL, THUMBNAIL_VALUE);
        return builder.toString();
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        progressbar.setVisibility(View.GONE);
        // Set empty state text to display "No articles found."
        emptyView.setText(R.string.no_articles);

        articleListView.setAdapter(articleAdapters);

        // Clear the adapter of previous article data
        articleAdapters.clear();

        if (articles != null && !articles.isEmpty()) {
            // If there is a valid list of {@link article}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            articleAdapters.addAll(articles);
            articleAdapters.notifyDataSetChanged();
        }
        loaderManager.destroyLoader(1);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        articleAdapters.clear();
    }

    public static class ArticleLoader extends AsyncTaskLoader<List<Article>> {

        /**
         * Tag for log messages
         */
        private final String LOG_TAG = ArticleLoader.class.getName();

        /**
         * Query URL
         */
        private final String mUrl;

        /**
         * Constructs a new {@link ArticleLoader}.
         *
         * @param context of the activity
         * @param url     to load data from
         */
        public ArticleLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Article> loadInBackground() {
            if (mUrl == null) {
                return null;
            }
            // Perform the network request, parse the response, and extract a list of earthquakes.
            return Util.fetchArticleData(mUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(TAG, "onQueryTextSubmit: called");

                // If there is a network connection, fetch data
                if (checkOnlineStatus()) {
                    Log.d(TAG, "onQueryTextSubmit: internet is available");
                    emptyView.setVisibility(View.INVISIBLE);
                    progressbar.setVisibility(View.VISIBLE);
                    Url = searchQueryURL(query);
                    Log.d(TAG, "onQueryTextSubmit: " + Url);

                    articleAdapters.clear();

                    loaderManager.initLoader(1, null, MainActivity.this);

                    //reset the search view
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    searchItem.collapseActionView();

                    Url = "";

                    //set The title of the activity to the search query
                    MainActivity.this.setTitle(query);
                } else {
                    emptyView.setText(R.string.no_internet_connection);
                    articleAdapters.clear();
                    progressbar.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public boolean checkOnlineStatus() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}