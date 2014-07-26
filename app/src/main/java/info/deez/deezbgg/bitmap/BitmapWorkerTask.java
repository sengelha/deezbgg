package info.deez.deezbgg.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "BitmapWorkerTask";
    private final WeakReference<ImageView> mImageViewReference;
    private BitmapMemoryCache mMemoryCache;
    private String url = null;

    public BitmapWorkerTask(ImageView imageView, BitmapMemoryCache memoryCache) {
        mImageViewReference = new WeakReference<ImageView>(imageView);
        mMemoryCache = memoryCache;
    }

    public String getUrl() {
        return url;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        url = params[0];
        try {
            Bitmap bitmap = getBitmapFromCache(url);
            if (bitmap == null) {
                bitmap = decodeSampledBitmapFromUrl(url);
            }
            addBitmapToCache(url, bitmap);
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + e);
            return null;
        }
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mImageViewReference != null && bitmap != null) {
            final ImageView imageView = mImageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void addBitmapToCache(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    private Bitmap getBitmapFromCache(String key) {
        return mMemoryCache.get(key);
    }

    private Bitmap decodeSampledBitmapFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        InputStream stream = conn.getInputStream();
        try {
            return BitmapFactory.decodeStream(stream);
        } finally {
            stream.close();
        }
    }
}
