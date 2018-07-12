package br.ufpe.cin.if1001.projeto_p3.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.FEED_LINK;

public class ArticlesJobService extends JobService {
    private static final String TAG = "JOB_SERVICE";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob");
        android.os.Debug.waitForDebugger();

        Intent loadServiceIntent = new Intent(getApplicationContext(), ArticlesIntentService.class);
        loadServiceIntent.putExtra(FEED_LINK, params.getExtras().getLong(FEED_LINK));

        startService(loadServiceIntent);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "onStopJob");
        Intent intent = new Intent(getApplicationContext(), ArticlesJobService.class);
        getApplicationContext().stopService(intent);
        return false;
    }
}
