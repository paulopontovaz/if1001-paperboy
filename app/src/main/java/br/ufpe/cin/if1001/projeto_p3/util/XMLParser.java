package br.ufpe.cin.if1001.projeto_p3.util;

import android.support.v4.util.Pair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufpe.cin.if1001.projeto_p3.domain.Article;

public class XMLParser extends Observable {

    private ArrayList<Article> articles;
    private Article currentArticle;
    private String feedTitle;
    private Pair<String, ArrayList<Article>> xmlData;

    public XMLParser() {
        articles = new ArrayList<>();
        feedTitle = "";
        currentArticle = new Article();
    }

    public void parseXML(String xml) throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(false);
        XmlPullParser xmlPullParser = factory.newPullParser();

        xmlPullParser.setInput(new StringReader(xml));
        boolean insideItem = false;
        boolean insideChannel = false;
        int eventType = xmlPullParser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xmlPullParser.getName().equalsIgnoreCase("channel"))
                    insideChannel = true;

                if (insideChannel && xmlPullParser.getName().equalsIgnoreCase("title")) {
                    feedTitle = xmlPullParser.nextText();
                    insideChannel = false;
                }

                if (xmlPullParser.getName().equalsIgnoreCase("item")) {
                    insideItem = true;

                } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                    if (insideItem) {
                        String title = xmlPullParser.nextText();
                        currentArticle.setTitle(title);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {
                    if (insideItem) {
                        String link = xmlPullParser.nextText();
                        currentArticle.setLink(link);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("dc:creator")) {
                    if (insideItem) {
                        String author = xmlPullParser.nextText();
                        currentArticle.setAuthor(author);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("media:thumbnail")) {
                    if (insideItem) {
                        String img = xmlPullParser.getAttributeValue(null, "url");
                        currentArticle.setImage(img);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {
                    if (insideItem) {
                        String description = xmlPullParser.nextText();
                        if (currentArticle.getImage() == null) {
                            currentArticle.setImage(getImageUrl(description));
                        }
                        currentArticle.setDescription(description);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("content:encoded")) {
                    if (insideItem) {
                        String content = xmlPullParser.nextText();
                        if (currentArticle.getImage() == null) {
                            currentArticle.setImage(getImageUrl(content));
                        }
                        currentArticle.setContent(content);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {
                    DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
                    Date pubDate = null;
                    try {
                        pubDate = format.parse(xmlPullParser.nextText());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    currentArticle.setPubDate(pubDate);
                }

            } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {
                insideItem = false;
                articles.add(currentArticle);
                currentArticle = new Article();
            }
            eventType = xmlPullParser.next();
        }

        xmlData = new Pair<>(feedTitle, articles);
        triggerObserver();
    }


    private void triggerObserver() {
        setChanged();
        notifyObservers(xmlData);
    }

    private String getImageUrl(String input) {

        String url = null;
        Pattern patternImg = Pattern.compile("(<img .*?>)");
        Matcher matcherImg = patternImg.matcher(input);
        if (matcherImg.find()) {
            String imgTag = matcherImg.group(1);
            Pattern patternLink = Pattern.compile("src\\s*=\\s*\"(.+?)\"");
            Matcher matcherLink = patternLink.matcher(imgTag);
            if (matcherLink.find()) {
                url = matcherLink.group(1);
            }
        }
        return url;
    }
}