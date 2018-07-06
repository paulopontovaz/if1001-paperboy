package br.ufpe.cin.if1001.projeto_p3.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.ARTICLE_ITEM;

public class ReaderActivity extends AppCompatActivity {
    private static final int FAVORITE_MARKED = R.drawable.ic_star_black_32dp;
    private static final int FAVORITE_UNMARKED = R.drawable.ic_star_border_black_32dp;
    private static final int READ_LATER_MARKED = R.drawable.ic_watch_later_black_32dp;
    private static final int READ_LATER_UNMARKED = R.drawable.ic_access_time_black_32dp;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        article = getIntent().getParcelableExtra(ARTICLE_ITEM);

        TextView title = findViewById(R.id.readerTitle);
        title.setText(article.getTitle());

        WebView myWebView = findViewById(R.id.readerContent);
        myWebView.setWebViewClient(new WebViewClient());
        Intent intentContent = getIntent();
        myWebView.loadUrl(article.getLink());

        if(article.isReadLater()) {
            ImageButton readLaterButton = findViewById(R.id.readerReadLaterButton);
            readLaterButton.setImageResource(READ_LATER_MARKED);
        }

        if(article.isFavorite()) {
            ImageButton favoriteButton = findViewById(R.id.readerFavoriteButton);
            favoriteButton.setImageResource(FAVORITE_MARKED);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ARTICLE_ITEM, article);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    public void setFavorite(View view){
        ImageButton favoriteButton = (ImageButton)findViewById(R.id.readerFavoriteButton);
        article.setFavorite(!article.isFavorite());

        int resourceId = article.isFavorite() ? FAVORITE_MARKED : FAVORITE_UNMARKED;
        favoriteButton.setImageResource(resourceId);

        updateArticle(article);

        SQLDataBaseHelper db = SQLDataBaseHelper.getInstance(getApplicationContext());
        ArrayList<Article> list = db.getArticles();
    }

    public void setReadLater(View view){
        ImageButton readLaterButton = (ImageButton)findViewById(R.id.readerReadLaterButton);
        article.setReadLater(!article.isReadLater());

        int resourceId = article.isReadLater() ? READ_LATER_MARKED : READ_LATER_UNMARKED;
        readLaterButton.setImageResource(resourceId);

        updateArticle(article);

        SQLDataBaseHelper db = SQLDataBaseHelper.getInstance(getApplicationContext());
        ArrayList<Article> list = db.getArticles();
    }

    private void updateArticle(Article article){
        SQLDataBaseHelper db = SQLDataBaseHelper.getInstance(getApplicationContext());
        db.updateArticleFavoriteReadLater(article);
    }
}
