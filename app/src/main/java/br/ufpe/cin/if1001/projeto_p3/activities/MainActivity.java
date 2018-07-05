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
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;
import br.ufpe.cin.if1001.projeto_p3.util.FeedAdapter;
import br.ufpe.cin.if1001.projeto_p3.util.Parser;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.ARTICLE_LIST_ACTIVITY_ACTION;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.GET_FAVORITE_ARTICLES;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.GET_READ_LATER_ARTICLES;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private SQLDataBaseHelper db;
    private ArrayList<Feed> feeds;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = SQLDataBaseHelper.getInstance(getApplicationContext());
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer);
        mToggle =  new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.feedList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        feeds = db.getFeeds();
        FeedAdapter mFeedAdapter = new FeedAdapter(feeds, R.layout.feed_list_item, MainActivity.this);
        mRecyclerView.setAdapter(mFeedAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent articleListActivity = new Intent(getApplicationContext(), ArticleListActivity.class);

        if (id == R.id.favoritos_menu)
            articleListActivity.putExtra(ARTICLE_LIST_ACTIVITY_ACTION, GET_FAVORITE_ARTICLES);

        if (id == R.id.lermaistarde_menu)
            articleListActivity.putExtra(ARTICLE_LIST_ACTIVITY_ACTION, GET_READ_LATER_ARTICLES);

        startActivity(articleListActivity);
        return false;
    }

    public void addFeed (View view){
        EditText feedLinkTextView = findViewById(R.id.feedLinkText);
        String feedLink;
        feedLink = feedLinkTextView.getText().toString();

        if(!Patterns.WEB_URL.matcher(feedLink).matches())
            Toast.makeText(getApplicationContext(), "O link tem formato inválido!", Toast.LENGTH_SHORT).show();
        else {
            boolean feedAlreadyExists = false;

            for(Feed feed : feeds)
                if (feed.getLink().equals(feedLink)) {
                    feedAlreadyExists = true;
                    break;
                }

            if(feedAlreadyExists)
                Toast.makeText(getApplicationContext(), "Este feed já está cadastrado!", Toast.LENGTH_SHORT).show();
            else
                getTitleAndInsert(feedLink);
        }
    }

    private void getTitleAndInsert(final String feedLink) {
        Parser parser = new Parser();
        parser.execute(feedLink);

        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Pair<String, ArrayList<Article>> xmlData) {
                if (xmlData.first != null && !xmlData.first.isEmpty()) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(xmlData.first);

                    Feed newFeed = new Feed(xmlData.first, feedLink);
                    db.insertFeed(newFeed);

                    feeds.add(newFeed);
                    FeedAdapter mFeedAdapter = new FeedAdapter(feeds, R.layout.feed_list_item, MainActivity.this);
                    mRecyclerView.setAdapter(mFeedAdapter);
                }
                else
                    Toast.makeText(
                            MainActivity.this, "Não foi possível inserir o novo feed.",
                            Toast.LENGTH_LONG
                    ).show();
            }

            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                            MainActivity.this, "Erro ao ler XML",
                            Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        });
    }
}

