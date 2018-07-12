package br.ufpe.cin.if1001.projeto_p3.util;

import br.ufpe.cin.if1001.projeto_p3.R;

public final class Constants {

    private Constants() {}

    public static final int FAVORITE_MARKED = R.drawable.ic_star_black_32dp;
    public static final int FAVORITE_UNMARKED = R.drawable.ic_star_border_black_32dp;
    public static final int READ_LATER_MARKED = R.drawable.ic_watch_later_black_32dp;
    public static final int READ_LATER_UNMARKED = R.drawable.ic_access_time_black_32dp;
    public static final int ARTICLE_LIST_UPDATE_FREQUENCY = 3000;//600000;

    public static final String FEED_LINK = "feed_link";
    public static final String FEED_TITLE = "feed_title";
    public static final String ARTICLE_LIST_ACTIVITY_ACTION = "article_list_activity_action";
    public static final String ARTICLE_LIST = "article_list";
    public static final String READ_LATER = "read_later";
    public static final String FAVORITES = "favorites";
    public static final String ARTICLE_ITEM = "article_item";
    public static final String UPDATE_ARTICLES_FINISHED = "update_articles_finished";

    //Ações na activity de artigos
    public static final String GET_FEED_ARTICLES = "get_articles";
    public static final String GET_READ_LATER_ARTICLES = "get_read_later_articles";
    public static final String GET_FAVORITE_ARTICLES= "get_favorite_articles";
}