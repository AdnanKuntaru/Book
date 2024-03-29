package com.kuntaru.book;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Book implements Parcelable {
    public String id;
    public String title;
    public String SubTitle;
    public String  authors;
    public String publisher;
    public String publishedDate;
    public String description;
    public String thumbnail;


    public Book(String id, String title, String subTitle, String[] authors,
                String publisher, String publishedDate, String description, String thumbnail) {
        this.id = id;
        this.title = title;
        SubTitle = subTitle;
        this.authors = TextUtils.join(", ", authors);
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        SubTitle = in.readString();
        authors = in.readString();
        publisher = in.readString();
        publishedDate = in.readString();
        description = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(SubTitle);
        dest.writeString(authors);
        dest.writeString(publisher);
        dest.writeString(publishedDate);
        dest.writeString(description);
        dest.writeString(thumbnail);
    }

  @BindingAdapter({"android:imageUrl"})
    public static void loadImage (ImageView view, String imageUrl) {
      if (!imageUrl.isEmpty()) {


          Picasso.with(view.getContext())
                  .load(imageUrl)
                  .placeholder(R.drawable.ic_library_books_black_24dp)
                  .into(view);
      } else {
          view.setBackgroundResource(R.drawable.ic_library_books_black_24dp);
      }
  }
}
