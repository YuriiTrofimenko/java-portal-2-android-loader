package org.tyaa.android.portalandroidloader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.sendgrid.Client;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;

import org.tyaa.android.portalandroidloader.adapter.AuthorsAdapter;
import org.tyaa.android.portalandroidloader.model.Author;
import org.tyaa.android.portalandroidloader.parser.AuthorsParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<Author>> {

    private static final String QUERY_URL_EXTRA = "query";
    private static final int MOVIE_LOADER_ID = 0;
    private AuthorsAdapter adapter;

    @BindView(R.id.loadingTextView)
    public TextView mLoadingTextView;

    @BindView(R.id.authorsRecyclerView)
    public RecyclerView mAuthorsRecyclerView;

    @BindView(R.id.progressBar)
    public ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuthorsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        String url = "https://spring-gae-datastore.appspot.com";
        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_URL_EXTRA, url);
        //  new DownloadTask().execute(url);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(MOVIE_LOADER_ID);
        if (movieLoader == null) {
            Log.e("InitLoader","true");
            loaderManager.initLoader(MOVIE_LOADER_ID, queryBundle, this).forceLoad();
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<List<Author>> onCreateLoader(int i, @Nullable final Bundle bundle) {

        return new AsyncTaskLoader<List<Author>>(this) {

            @Override
            protected void onStartLoading() {
                if (bundle == null) {
                    Log.e("args","null");
                    return;
                }
                Log.e("onStartLoading","true");
                mProgressBar.setVisibility(View.VISIBLE);
                mLoadingTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public List<Author> loadInBackground() {

                List<Author> authors = null;
                String urlString = bundle.getString(QUERY_URL_EXTRA);
                Log.e("URL", urlString);
                if (urlString == null || TextUtils.isEmpty(urlString)) {
                    return null;
                }

                try {
                    URL url = new URL(urlString + "/api/author");
                    HttpURLConnection urlConnection;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    int statusCode = urlConnection.getResponseCode();

                    if (statusCode == 200) {

                        BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }

                        String respString = response.toString();
                        Log.d("My", respString);
                        authors =
                            new AuthorsParser().parseAuthors(respString);
                    } else {
                        //
                    }
                } catch (Exception e) {
                    Log.d("My", e.getLocalizedMessage());
                    return null;
                }
                return authors;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Author>> loader, List<Author> authors) {

        mProgressBar.setVisibility(View.GONE);
        mLoadingTextView.setVisibility(View.GONE);
        Log.e("OnLoadFinished", "true");
        if (authors != null) {
            Log.e("My", authors.get(0).toString());
            adapter = new AuthorsAdapter(this, authors, R.layout.authors_item);
            mAuthorsRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Author>> loader) {

    }
}
