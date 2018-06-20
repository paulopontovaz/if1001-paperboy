package br.ufpe.cin.if1001.projeto_p3.domain;

import java.util.Date;

public class Article {
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
}
