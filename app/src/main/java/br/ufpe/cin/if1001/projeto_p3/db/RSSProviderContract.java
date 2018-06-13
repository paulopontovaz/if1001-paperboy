package br.ufpe.cin.if1001.projeto_p3.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class RSSProviderContract {
    public static final Uri BASE_RSS_URI = Uri.parse("content://br.ufpe.cin.if1001.projeto_p3/");
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/RssProvider.data.text";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/RssProvider.data.text";
}

