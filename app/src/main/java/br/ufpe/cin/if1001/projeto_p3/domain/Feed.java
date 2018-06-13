package br.ufpe.cin.if1001.projeto_p3.domain;

public class Feed {
    private String title;
    private String link;

    public Feed() {}

    public Feed(String link) {
        this.link = link;
    }

    public Feed(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}