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
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;
import br.ufpe.cin.if1001.projeto_p3.util.FeedAdapter;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.ADD_FEED;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.ARTICLE_LIST_ACTIVITY_ACTION;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.FEED_LINK;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.GET_FAVORITE_ARTICLES;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.GET_READ_LATER_ARTICLES;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedAdapter;
    private SQLDataBaseHelper db;

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

        ArrayList<Feed> feeds = db.getFeeds();
        mFeedAdapter = new FeedAdapter(feeds, R.layout.feed_list_item, MainActivity.this);
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

        Intent articleListActivity = new Intent(this, ArticleListActivity.class);
        articleListActivity.putExtra(ARTICLE_LIST_ACTIVITY_ACTION, ADD_FEED);
        articleListActivity.putExtra(FEED_LINK, feedLink);
        startActivity(articleListActivity);
    }
}

