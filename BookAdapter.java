package com.example.android.booklisting;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<googleBook> {

    public BookAdapter(MainActivity context, ArrayList<googleBook> books_list) {
        super(context, 0, books_list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_book, parent, false);
        }
        googleBook currentBook = getItem(position);

        TextView book_author = (TextView) listItemView.findViewById(R.id.author);
        book_author.setText(currentBook.getAuthor());

        TextView book_title = (TextView) listItemView.findViewById(R.id.title_info);
        book_title.setText(currentBook.getTitle_info());

        return listItemView;
    }
}
