package br.ufpe.cin.if1001.projeto_p3.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.util.ArticleAdapter;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.*;

public class ArticleListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    public String action;
    private RecyclerView mRecyclerView;
    private SQLDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer);
        mToggle =  new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.articleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        action = getIntent().getStringExtra(ARTICLE_LIST_ACTIVITY_ACTION);
        db = SQLDataBaseHelper.getInstance(getApplicationContext());

        switch (action) {
            case GET_FEED_ARTICLES:
                getSupportActionBar().setTitle(getIntent().getStringExtra(FEED_TITLE));
                ArrayList<Article> articlesFromExtra = getIntent().getParcelableArrayListExtra(ARTICLE_LIST);
                updateArticleList(articlesFromExtra);
                break;
            case GET_READ_LATER_ARTICLES:
                getSupportActionBar().setTitle(R.string.lerDepois);
                ArrayList<Article> readLaterArticles = db.getArticles(READ_LATER);
                updateArticleList(readLaterArticles);
                break;
            case GET_FAVORITE_ARTICLES:
                getSupportActionBar().setTitle(R.string.favoritos);
                ArrayList<Article> favoriteArticles = db.getArticles(FAVORITES);
                updateArticleList(favoriteArticles);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home_menu){
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
        }

        if (id == R.id.favoritos_menu && !action.equals(GET_FAVORITE_ARTICLES)){
            Intent articleListActivity = new Intent(getApplicationContext(), ArticleListActivity.class);
            articleListActivity.putExtra(ARTICLE_LIST_ACTIVITY_ACTION, GET_FAVORITE_ARTICLES);
            startActivity(articleListActivity);
        }

        if (id == R.id.lermaistarde_menu && !action.equals(GET_READ_LATER_ARTICLES)){
            Intent articleListActivity = new Intent(getApplicationContext(), ArticleListActivity.class);
            articleListActivity.putExtra(ARTICLE_LIST_ACTIVITY_ACTION, GET_READ_LATER_ARTICLES);
            startActivity(articleListActivity);
        }

        return false;
    }

    private void updateArticleList(ArrayList<Article> articles) {
        ArticleAdapter mArticleAdapter = new ArticleAdapter(
                articles,
                R.layout.article_list_item,
                ArticleListActivity.this
        );
        mRecyclerView.setAdapter(mArticleAdapter);
    }
}
