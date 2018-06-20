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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;
import br.ufpe.cin.if1001.projeto_p3.util.ArticleAdapter;
import br.ufpe.cin.if1001.projeto_p3.util.FeedAdapter;
import br.ufpe.cin.if1001.projeto_p3.util.Parser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedAdapter;
    private ArticleAdapter mArticleAdapter;
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

        if (id == R.id.favoritos_menu){
            Intent favoritosIntent = new Intent(getApplicationContext(), ArticleListActivity.class);
            //TODO: Definir no intent um parâmetro para sinalizar que a Activity de destino será 'Favoritos'.
            startActivity(favoritosIntent);
        }
        if (id == R.id.lermaistarde_menu){
            Intent lermaistardeIntent = new Intent(getApplicationContext(), ArticleListActivity.class);
            //TODO: Definir no intent um parâmetro para sinalizar que a Activity de destino será 'Ler Depois'.
            startActivity(lermaistardeIntent);
        }

        return false;
    }

    public void addFeed (View view){
        EditText feedLinkTextView = findViewById(R.id.feedLinkText);
        String feedLink;
        feedLink = feedLinkTextView.getText().toString();

        db.insertFeed(feedLink, feedLink);

        ArrayList<Feed> feeds = db.getFeeds();
    }

    public void getArticles(View view) {
        EditText feedLinkTextView = findViewById(R.id.feedLinkText);
        String feedLink;
        feedLink = feedLinkTextView.getText().toString();

        Parser parser = new Parser();
        parser.execute(feedLink);

        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                mArticleAdapter = new ArticleAdapter(list, R.layout.article_list_item, MainActivity.this);
                mRecyclerView.setAdapter(mArticleAdapter);
            }

            //what to do in case of error
            @Override
            public void onError() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Unable to load data.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}

