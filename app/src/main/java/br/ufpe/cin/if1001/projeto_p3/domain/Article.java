package br.ufpe.cin.if1001.projeto_p3.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Article implements Parcelable {
    private String title;
    private String author;
    private String link;
    private Date pubDate;
    private String description;
    private String content;
    private String image;
    private String channel;
    private boolean favorite;
    private boolean readLater;

    public Article(){}

    public Article(
            String title,
            String author,
            String link,
            Date pubDate,
            String description,
            String content,
            String image,
            String channel,
            boolean favorite,
            boolean readLater
    ) {
        this.title = title;
        this.author = author;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.content = content;
        this.image = image;
        this.channel = channel;
        this.favorite = favorite;
        this.readLater = readLater;
    }

    private Article(Parcel in) {
        title = in.readString();
        author = in.readString();
        link = in.readString();
        description = in.readString();
        content = in.readString();
        image = in.readString();
        channel = in.readString();
        favorite = in.readByte() != 0;
        readLater = in.readByte() != 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getChannel() { return channel; }

    public boolean isReadLater() { return readLater; }

    public boolean isFavorite() { return favorite; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setChannel(String channel) { this.channel = channel; }

    public void setReadLater(boolean readLater) { this.readLater = readLater; }

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", channel='" + channel + '\'' +
                ", favorite='" + favorite + '\'' +
                ", readLater='" + readLater + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(link);
        dest.writeLong(pubDate.getTime());
        dest.writeString(description);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(channel);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeByte((byte) (readLater ? 1 : 0));
    }

    public Article createFromParcel(Parcel in) {
        Article article = new Article();

        article.title = in.readString();
        article.author = in.readString();
        article.link = in.readString();
        article.pubDate = new Date(in.readLong());
        article.description = in.readString();
        article.content = in.readString();
        article.image = in.readString();
        article.channel = in.readString();
        article.favorite = in.readByte() == 1;
        article.readLater = in.readByte() == 1;

        return article;
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        author = in.readString();
        link = in.readString();
        pubDate = new Date(in.readLong());
        description = in.readString();
        content = in.readString();
        image = in.readString();
        channel = in.readString();
        favorite = in.readByte() == 1;
        readLater = in.readByte() == 1;
    }
}
