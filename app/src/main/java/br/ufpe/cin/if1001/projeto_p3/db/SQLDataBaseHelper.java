package br.ufpe.cin.if1001.projeto_p3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;


public class SQLDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Paperboy";
    private static final String ARTICLE_TABLE = ArticleInfo.ARTICLE_TABLE;
    private static final String FEED_TABLE = FeedInfo.FEED_TABLE;
    private static final int DB_VERSION = 1;

    //alternativa
    Context c;

    private SQLDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        c = context;
    }

    private static SQLDataBaseHelper db;

    public static SQLDataBaseHelper getInstance(Context c) {
        if (db==null) {
            db = new SQLDataBaseHelper(c.getApplicationContext());
        }
        return db;
    }

    private static final String FEED_ROWID = FeedInfo._ID;
    private static final String FEED_TITLE = FeedInfo.TITLE;
    private static final String FEED_LINK = FeedInfo.LINK;

    private final static String[] feed_columns = FeedInfo.ALL_COLUMNS;

    private static final String ARTICLE_ROWID = ArticleInfo._ID;
    private static final String ARTICLE_TITLE = ArticleInfo.TITLE;
    private static final String ARTICLE_AUTHOR = ArticleInfo.AUTHOR;
    private static final String ARTICLE_LINK = ArticleInfo.LINK;
    private static final String ARTICLE_DATE = ArticleInfo.DATE;
    private static final String ARTICLE_DESCRIPTION = ArticleInfo.DESCRIPTION;
    private static final String ARTICLE_CONTENT = ArticleInfo.CONTENT;
    private static final String ARTICLE_IMAGE = ArticleInfo.IMAGE;
    private static final String ARTICLE_CHANNEL = ArticleInfo.CHANNEL;
    private static final String ARTICLE_FAVORITE = ArticleInfo.FAVORITE;
    private static final String ARTICLE_READ_LATER = ArticleInfo.READ_LATER;

    private final static String[] article_columns = ArticleInfo.ALL_COLUMNS;

    private static final String CREATE_ARTICLE_TABLE_COMMAND = "CREATE TABLE " + ARTICLE_TABLE + " (" +
        ARTICLE_ROWID +" integer primary key autoincrement, "+
        ARTICLE_TITLE + " text not null, " +
        ARTICLE_AUTHOR + " text not null, " +
        ARTICLE_LINK + " text not null, " +
        ARTICLE_DATE + " text not null, " +
        ARTICLE_DESCRIPTION + " text not null, " +
        ARTICLE_CONTENT + " text not null, " +
        ARTICLE_IMAGE + " text not null, " +
        ARTICLE_CHANNEL + " text not null, " +
        ARTICLE_FAVORITE + " boolean not null, " +
        ARTICLE_READ_LATER + " boolean not null " +
    ");";

    private static final String CREATE_FEED_TABLE_COMMAND = "CREATE TABLE " + FEED_TABLE + " (" +
        FEED_ROWID +" integer primary key autoincrement, "+
        FEED_TITLE + " text not null, " +
        FEED_LINK + " text not null " +
    ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE_TABLE_COMMAND);
        db.execSQL(CREATE_FEED_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //estamos ignorando esta possibilidade no momento
        throw new RuntimeException("nao se aplica");
    }

    public void insertFeed(Feed item) {
        insertFeed(item.getTitle(), item.getLink());
    }

    public void insertFeed(String title, String link) {
        SQLiteDatabase dataBase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEED_TITLE, title);
        values.put(FEED_LINK, link);

        dataBase.insert(FEED_TABLE, null, values);
    }

    public ArrayList<Feed> getFeeds() throws SQLException {
        SQLiteDatabase dataBase = db.getReadableDatabase();
        Cursor cursor = null;

        try {
            dataBase.beginTransaction();

            cursor = dataBase.query(
                FEED_TABLE,
                feed_columns,
                null,
                null,
                null,
                null,
                null,
                null
            );

            dataBase.endTransaction();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        ArrayList<Feed> feeds = new ArrayList<Feed>();
        for(Objects.requireNonNull(cursor).moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            feeds.add(new Feed(
                cursor.getString(cursor.getColumnIndexOrThrow(FEED_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(FEED_LINK))
            ));
        }

        cursor.close();

        return feeds;
    }

    public boolean deleteFeed (String link) {
        return db.getReadableDatabase()
            .delete(FEED_TABLE, FEED_LINK + "=?", new String[]{ link }) > 0;
    }

    public long insertArticle(Article item) {
        return insertArticle(
            item.getTitle(),
            item.getAuthor(),
            item.getLink(),
            item.getPubDate(),
            item.getDescription(),
            item.getContent(),
            item.getImage(),
            item.getChannel(),
            item.isFavorite(),
            item.isReadLater()
        );
    }

    //Inserindo itens novos no banco utilizando ContentValues.
    public long insertArticle(
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
        SQLiteDatabase dataBase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ARTICLE_TITLE, title);
        values.put(ARTICLE_AUTHOR, author);
        values.put(ARTICLE_LINK, link);
        values.put(ARTICLE_DATE, pubDate.toString());
        values.put(ARTICLE_DESCRIPTION, description);
        values.put(ARTICLE_CONTENT, content);
        values.put(ARTICLE_IMAGE, image);
        values.put(ARTICLE_CHANNEL, channel);
        values.put(ARTICLE_FAVORITE, favorite);
        values.put(ARTICLE_READ_LATER, readLater);

        return dataBase.insert(ARTICLE_TABLE,null, values);
    }

    public Article getArticleByLink(String link) throws SQLException {
        SQLiteDatabase dataBase = db.getReadableDatabase();
        Cursor cursor = dataBase.query(
                ARTICLE_TABLE,
                article_columns,
                ARTICLE_LINK + " LIKE ?", //WHERE -> filtrando a consulta pelo link
                new String[]{ link },
                null,
                null,
                null,
                null);

        Article item = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
            Date date = null;
            try {
                date = format.parse(cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            item = new Article(
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_AUTHOR)),
                link,
                date,
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_CHANNEL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(ARTICLE_FAVORITE)) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow(ARTICLE_READ_LATER)) == 1
            );
        }

        cursor.close();

        return item;
    }

    public ArrayList<Article> getArticles() throws SQLException {
        SQLiteDatabase dataBase = db.getReadableDatabase();
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
        Cursor cursor = null;

        try {
            dataBase.beginTransaction();

            cursor = dataBase.query(
                    ARTICLE_TABLE,
                    article_columns,
                    null,
                    null,
                    null,
                    null,
                    ARTICLE_DATE + " DESC",
                    null
            );

            dataBase.endTransaction();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        ArrayList<Article> articles = new ArrayList<>();
        for(Objects.requireNonNull(cursor).moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Date date = null;
            try {
                date = format.parse(cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            articles.add(new Article(
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_AUTHOR)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_LINK)),
                date,
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(ARTICLE_CHANNEL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(ARTICLE_FAVORITE)) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow(ARTICLE_READ_LATER)) == 1
            ));
        }

        cursor.close();

        return articles;
    }

    private void deleteArticle (String link) {
        db.getReadableDatabase().delete(ARTICLE_TABLE, ARTICLE_LINK + "=?", new String[]{link});
    }

    //Define os valores das propriedades "Favorite" ou "ReadLater" do artigo
    //Se nenhuma das duas for 'true', o artigo é removido do banco.
    public boolean setFavoriteReadLater(Article argArticle, boolean isFavorite, boolean isReadLater) {
        SQLiteDatabase dataBase = db.getWritableDatabase();
        int result = 0;

        Article article = getArticleByLink(argArticle.getLink());

        if (article == null)
            insertArticle(argArticle);
        else if (!isFavorite && !isReadLater)
            deleteArticle(article.getLink());
        else {
            ContentValues values = new ContentValues();
            values.put(ARTICLE_FAVORITE, isFavorite);
            values.put(ARTICLE_READ_LATER, isReadLater);

            result = dataBase.update(
                    ARTICLE_TABLE,
                    values,
                    ARTICLE_LINK + " LIKE ?",
                    new String[]{ article.getLink() });
        }

        return result > 0;
    }
}
