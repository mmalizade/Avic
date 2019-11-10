package ir.moovic.entertainment.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

public class DownloadProgressCounter extends AsyncTask<Void, String, Void> {

    private final long downloadId;
    private final DownloadManager.Query query;
    private Cursor cursor;
    private int totalBytes;
    private Listener listener;
    private DownloadManager manager;

    public DownloadProgressCounter(Context context, long downloadId, Listener listener) {
        this.downloadId = downloadId;
        this.listener = listener;
        this.query = new DownloadManager.Query();
        query.setFilterById(this.downloadId);
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }


    @Override
    protected Void doInBackground(Void... params) {
        while(downloadId > 0){
            try {
                Thread.sleep(100);
                cursor = manager.query(query);
                if (cursor.moveToFirst()) {
                    if (totalBytes <= 0) {
                        totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    }
                    final int bytesDownloadedSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    if (bytesDownloadedSoFar >= totalBytes && totalBytes > 0) {
                        break;
                    } else {
                        publishProgress(String.valueOf(bytesDownloadedSoFar * 100 / totalBytes));
                    }

                }
                cursor.close();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(listener != null){
            int p = Integer.valueOf(values[0]);
            listener.updateProgress(p);
        }
    }

    public interface Listener {
        void updateProgress(int percent);
    }
}