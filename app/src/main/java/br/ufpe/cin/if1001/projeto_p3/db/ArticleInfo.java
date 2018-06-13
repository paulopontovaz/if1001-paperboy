package br.ufpe.cin.if1001.projeto_p3.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class ArticleInfo extends RSSProviderContract {
    public static final String _ID = BaseColumns._ID;
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String LINK = "guid";
    public static final String DATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String CHANNEL = "channel";
    public static final String FAVORITE = "favorite";
    public static final String READ_LATER = "readLater";
    public static final String ARTICLE_TABLE = "article";

    public final static String[] ALL_COLUMNS = {
            _ID,
            TITLE,
            AUTHOR,
            LINK,
            DATE,
            DESCRIPTION,
            CONTENT,
            IMAGE,
            CHANNEL,
            FAVORITE,
            READ_LATER
    };

    public static final Uri ARTICLES_LIST_URI = Uri.withAppendedPath(BASE_RSS_URI, ARTICLE_TABLE);

}
