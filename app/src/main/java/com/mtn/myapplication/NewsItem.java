package com.mtn.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
    private String category;
    private String title;
    private String spot;

    NewsItem(){}
    protected NewsItem(Parcel in) {
        category = in.readString();
        title = in.readString();
        spot = in.readString();
        redirects = in.readByte() != 0;
        isAdvertorial = in.readByte() != 0;
        publishDate = in.readString();
        id = in.readInt();
        imageUrl = in.readString();
        videoUrl = in.readString();
        webUrl = in.readString();
        commentCount = in.readInt();
        imageSize = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public boolean isRedirects() {
        return redirects;
    }

    public void setRedirects(boolean redirects) {
        this.redirects = redirects;
    }

    public boolean isAdvertorial() {
        return isAdvertorial;
    }

    public void setAdvertorial(boolean advertorial) {
        isAdvertorial = advertorial;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    private boolean redirects;
    private boolean isAdvertorial;
    private String publishDate;
    private int id;
    private String imageUrl;
    private String videoUrl;
    private String webUrl;
    private int commentCount;
    private String imageSize;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(title);
        parcel.writeString(spot);
        parcel.writeByte((byte) (redirects ? 1 : 0));
        parcel.writeByte((byte) (isAdvertorial ? 1 : 0));
        parcel.writeString(publishDate);
        parcel.writeInt(id);
        parcel.writeString(imageUrl);
        parcel.writeString(videoUrl);
        parcel.writeString(webUrl);
        parcel.writeInt(commentCount);
        parcel.writeString(imageSize);
    }
}
