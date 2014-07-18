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

class BitmapWorkerTask extends AsyncTask<URL, Void, Bitmap> {
    private static final String TAG = "BitmapWorkerTask";
    private final WeakReference<ImageView> mImageViewReference;

    public BitmapWorkerTask(ImageView imageView) {
        mImageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(URL... params) {
        try {
            URL url = params[0];
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();
            try {
                return BitmapFactory.decodeStream(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading bitmap", e);
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
}
