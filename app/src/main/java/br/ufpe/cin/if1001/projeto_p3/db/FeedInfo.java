package br.ufpe.cin.if1001.projeto_p3.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class FeedInfo extends RSSProviderContract {
    public static final String _ID = BaseColumns._ID;
    public static final String TITLE = "title";
    public static final String LINK = "guid";
    public static final String FEED_TABLE = "feed";

    public final static String[] ALL_COLUMNS = {_ID, TITLE, LINK};

    public static final Uri FEEDS_LIST_URI = Uri.withAppendedPath(BASE_RSS_URI, FEED_TABLE);
}
