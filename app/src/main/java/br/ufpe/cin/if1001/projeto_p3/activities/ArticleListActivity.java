package br.ufpe.cin.if1001.projeto_p3.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;

public class ArticleListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private boolean isFavoriteView = false;//TODO: Atribuir 'true' quando vier esta informação via intent
    private boolean isReadLaterView = false;//TODO: Atribuir 'true' quando vier esta informação via intent

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home_menu){
            Intent favoritosIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(favoritosIntent);
        }

        if (id == R.id.favoritos_menu && !isFavoriteView){
            Intent favoritosIntent = new Intent(getApplicationContext(), ArticleListActivity.class);
            startActivity(favoritosIntent);
        }

        if (id == R.id.lermaistarde_menu && !isReadLaterView){
            Intent lermaistardeIntent = new Intent(getApplicationContext(), ArticleListActivity.class);
            startActivity(lermaistardeIntent);
        }

        return false;
    }
}
