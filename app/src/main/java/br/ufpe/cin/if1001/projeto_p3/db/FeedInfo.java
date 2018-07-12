package br.ufpe.cin.if1001.projeto_p3.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class FeedInfo {
    public static final String _ID = BaseColumns._ID;
    public static final String TITLE = "title";
    public static final String LINK = "guid";
    public static final String FEED_TABLE = "feed";

    public final static String[] ALL_COLUMNS = {_ID, TITLE, LINK};
}
