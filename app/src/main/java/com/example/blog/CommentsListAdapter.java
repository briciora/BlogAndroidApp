package com.example.blog;

//https://stackoverflow.com/questions/20755440/how-to-display-multiple-number-of-textviews-inside-each-row-in-listview

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.List;

public class CommentsListAdapter implements ListAdapter {
    private List<Comment> data;
    private Activity context;

    public CommentsListAdapter(Activity context, List<Comment> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View view, ViewGroup parent) {

        if(view == null) {
            //Only creates new view when recycling isn't possible
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_comment, null);
        }

        Comment comment = data.get(position);

        TextView emailView = view.findViewById(R.id.email);
        emailView.setText(comment.getEmail());

        TextView titleView = view.findViewById(R.id.name);
        titleView.setText(comment.getName());

        TextView bodyView = view.findViewById(R.id.body);
        bodyView.setText(comment.getBody());

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return data.size() == 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }
}
