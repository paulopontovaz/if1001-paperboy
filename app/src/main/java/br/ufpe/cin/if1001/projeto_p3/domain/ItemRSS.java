package br.ufpe.cin.if1001.projeto_p3.domain;

public class ItemRSS {
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String imageSource;

    public ItemRSS(
            String title,
            String link,
            String pubDate,
            String description,
            String imageSource) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getImageSource() { return imageSource; }

    @Override
    public String toString() {
        return title;
    }
}