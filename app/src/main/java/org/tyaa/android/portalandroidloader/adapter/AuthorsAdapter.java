package org.tyaa.android.portalandroidloader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tyaa.android.portalandroidloader.R;
import org.tyaa.android.portalandroidloader.model.Author;

import java.util.List;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.AuthorsViewHolder> {

    private List<Author> mAuthors;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mItemTemplateId;

    public AuthorsAdapter(Context _context, List<Author> _authors, int _itemTemplateId) {
        mAuthors = _authors;
        mInflater = LayoutInflater.from(_context);
        mItemTemplateId = _itemTemplateId;
        mContext = _context;
    }

    @NonNull
    @Override
    public AuthorsAdapter.AuthorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View v = mInflater.inflate(mItemTemplateId, viewGroup, false);
        AuthorsViewHolder vh = new AuthorsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorsAdapter.AuthorsViewHolder authorsViewHolder, int i) {
        Log.d("My", mAuthors.get(i).name + " (" + mAuthors.get(i).startedAt + ")");
        authorsViewHolder.textView.setText(mAuthors.get(i).name + " (" + mAuthors.get(i).startedAt + ")");
    }

    @Override
    public int getItemCount() {
        return mAuthors.size();
    }

    public class AuthorsViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public AuthorsViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.authorItemTextView);
            // textView = new TextView(mContext);
        }

    }
}
