package br.ufpe.cin.if1001.projeto_p3.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import br.ufpe.cin.if1001.projeto_p3.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void addFeed(View view) {
        EditText feedLinkTextView = findViewById(R.id.feedLinkText);
        String feedLink;
        feedLink = feedLinkTextView.getText().toString();
    }
}

