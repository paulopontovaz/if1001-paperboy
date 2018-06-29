package br.ufpe.cin.if1001.projeto_p3.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;
import br.ufpe.cin.if1001.projeto_p3.util.ArticleAdapter;
import br.ufpe.cin.if1001.projeto_p3.util.Parser;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.*;

public class ArticleListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private String action;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mArticleAdapter;
    private SQLDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        db = SQLDataBaseHelper.getInstance(getApplicationContext());
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

        action = getIntent().getStringExtra(ARTICLE_LIST_ACTIVITY_ACTION);

        switch (action) {
            case ADD_FEED:
            case GET_FEED_ARTICLES:
                getArticlesFromLink(getIntent().getStringExtra(FEED_LINK));
                break;
            case GET_READ_LATER_ARTICLES:
                getSupportActionBar().setTitle(R.string.lerDepois);
                break;
            case GET_FAVORITE_ARTICLES:
                getSupportActionBar().setTitle(R.string.favoritos);
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

    public void addFeed (Feed feed) {
        /*
            TODO: antes de inserir, verificar se o feed já está salvo no banco.
            Se já estiver, enviar um Toast avisando e não permitir a inserção.
        * */
        db.insertFeed(feed);
    }

    public void getArticlesFromLink(final String feedLink) {
        Parser parser = new Parser();
        parser.execute(feedLink);

        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Pair<String, ArrayList<Article>> xmlData) {
                if(!xmlData.first.isEmpty())
                    getSupportActionBar().setTitle(xmlData.first);

                if(action.equals(ADD_FEED))
                    addFeed(new Feed(xmlData.first, feedLink));

                mArticleAdapter = new ArticleAdapter(
                        xmlData.second,
                        R.layout.article_list_item,
                        ArticleListActivity.this);
                mRecyclerView.setAdapter(mArticleAdapter);
            }

            //what to do in case of error
            @Override
            public void onError() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ArticleListActivity.this, "Unable to load data.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
