package br.ufpe.cin.if1001.projeto_p3.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.Services.ArticlesIntentService;
import br.ufpe.cin.if1001.projeto_p3.Services.ArticlesJobService;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.util.ArticleAdapter;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.*;

public class ArticleListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    public String action;
    private RecyclerView mRecyclerView;
    private SQLDataBaseHelper db;
    private ArrayList<Article> articles;
    private MenuItem item;
    private DrawerLayout mDrawerLayout;
    private String feedLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mDrawerLayout = findViewById(R.id.articleListDrawer);
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

        feedLink = getIntent().getStringExtra(FEED_LINK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        action = getIntent().getStringExtra(ARTICLE_LIST_ACTIVITY_ACTION);
        db = SQLDataBaseHelper.getInstance(getApplicationContext());
        mDrawerLayout.closeDrawer(GravityCompat.START, false);

        if(feedLink == null)
            feedLink = getIntent().getStringExtra(FEED_LINK);

        IntentFilter intentFilter = new IntentFilter(UPDATE_ARTICLES_FINISHED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onUpdateArticleList, intentFilter);

        switch (action) {
            case GET_FEED_ARTICLES:
                getSupportActionBar().setTitle(getIntent().getStringExtra(FEED_TITLE));
                articles = getIntent().getParcelableArrayListExtra(ARTICLE_LIST);
                updateArticleList();

                if(!IsJobRunning(Integer.parseInt(getString(R.string.job_update_feed_id))))
                    ScheduleJob();

                if (!feedLink.isEmpty() && articles != null && !articles.isEmpty()) {
                    Intent loadServiceIntent = new Intent(getApplicationContext(), ArticlesIntentService.class);
                    loadServiceIntent.putExtra(FEED_LINK, getIntent().getStringExtra(FEED_LINK));
                    loadServiceIntent.putParcelableArrayListExtra(ARTICLE_LIST, getIntent().getParcelableArrayListExtra(ARTICLE_LIST));
                    startService(loadServiceIntent);
                }

                break;
            case GET_READ_LATER_ARTICLES:
                getSupportActionBar().setTitle(R.string.lerDepois);
                articles = db.getArticles(READ_LATER);
                updateArticleList();
                break;
            case GET_FAVORITE_ARTICLES:
                getSupportActionBar().setTitle(R.string.favoritos);
                articles = db.getArticles(FAVORITES);
                updateArticleList();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onUpdateArticleList);
    }

    private BroadcastReceiver onUpdateArticleList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive");
            ArrayList<Article> newArticles = intent.getParcelableArrayListExtra(ARTICLE_LIST);

            if(articles != null && !articles.isEmpty() && newArticles != null && !newArticles.isEmpty()){
                List<String> currentArticleLinks = articles.stream().map(Article::getLink).collect(Collectors.toList());

                for (Article article : newArticles)
                    if (!currentArticleLinks.contains(article.getLink())){
                        articles.add(article);
                        updateArticleList();
                        break;
                    }
            }
        }
    };

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        switch (action) {
            case GET_READ_LATER_ARTICLES:
                item = findViewById(R.id.lermaistarde_menu);
                item.setEnabled(false);
                break;
            case GET_FAVORITE_ARTICLES:
                item = findViewById(R.id.favoritos_menu);
                item.setEnabled(false);
                break;
        }

        return super.onMenuOpened(featureId, menu);
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

    private void updateArticleList() {
        ArticleAdapter mArticleAdapter = new ArticleAdapter(
                articles,
                R.layout.article_list_item,
                ArticleListActivity.this
        );
        mRecyclerView.setAdapter(mArticleAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Article article = data.getParcelableExtra(ARTICLE_ITEM);
                for (Article a : articles) {
                    if(article.getLink().equals(a.getLink())) {
                        a.setFavorite(article.isFavorite());
                        a.setReadLater(article.isReadLater());
                        updateArticleList();
                        break;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean IsJobRunning (int jobId) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (scheduler != null) {
            for ( JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                Log.i("JOB_COUNT", Integer.toString(jobInfo.getId()));
                if ( jobInfo.getId() == jobId )
                    return true;
            }
        }

        return false;
    }

    public void ScheduleJob(){
        if (!feedLink.isEmpty()) {
            ComponentName componentName = new ComponentName(this, ArticlesJobService.class);
            PersistableBundle bundle = new PersistableBundle();
            bundle.putString(FEED_LINK, feedLink);

            int jobId = Integer.parseInt(getString(R.string.job_update_feed_id));
            JobInfo ji = new JobInfo.Builder(jobId, componentName)
                    .setPeriodic(ARTICLE_LIST_UPDATE_FREQUENCY)
                    .setMinimumLatency(ARTICLE_LIST_UPDATE_FREQUENCY)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(bundle)
                    .build();

            JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

            if (js != null)
                js.schedule(ji);
            Log.i("JOB_SCHEDULER", "Job scheduled!");
        }
    }
}
