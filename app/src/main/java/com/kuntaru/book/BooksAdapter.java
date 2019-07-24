package com.kuntaru.book;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>  {

    ArrayList<Book> books;

    public BooksAdapter(ArrayList<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item,parent, false);

        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {

        Book book = books.get(i);
        bookViewHolder.bind(book);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDate;
        TextView tvPublisher;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvPublisher = itemView.findViewById(R.id.publisher);
            tvDate = itemView.findViewById(R.id.publishedDate);
            itemView.setOnClickListener(this);
        }

        public void bind(Book book){
            tvTitle.setText(book.title);

            tvAuthor.setText(book.authors);
            tvDate.setText(book.publishedDate);
            tvPublisher.setText(book.publisher);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);
            Intent intent = new Intent(v.getContext(),BookDetail.class);
            intent.putExtra("Book",selectedBook);
            v.getContext().startActivity(intent);


        }
    }
}
