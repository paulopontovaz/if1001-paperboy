package br.ufpe.cin.if1001.projeto_p3.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufpe.cin.if1001.projeto_p3.domain.Article;
import br.ufpe.cin.if1001.projeto_p3.util.Parser;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.*;

public class ArticlesIntentService extends IntentService {
    public ArticlesIntentService() {
        super("ArticlesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String link = intent.getStringExtra(FEED_LINK);

        Parser parser = new Parser();
        parser.execute(link);

        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Pair<String, ArrayList<Article>> xmlData) {
                if (xmlData.second != null) {
                    Intent broadcastIntent = new Intent(UPDATE_ARTICLES_FINISHED);
                    broadcastIntent.putParcelableArrayListExtra(ARTICLE_LIST, xmlData.second);
                    sendBroadcast(broadcastIntent);
                } else
                    Toast.makeText(
                            getApplicationContext(), "Não foi possível atualizar a lista de artigos.",
                            Toast.LENGTH_LONG
                    ).show();
            }

            @Override
            public void onError() {
                Toast.makeText(
                        getApplicationContext(), "Erro ao ler XML",
                        Toast.LENGTH_LONG
                ).show();
            }

        });
    }
}
