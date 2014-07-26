package info.deez.deezbgg.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sengelh on 7/18/2014.
 */
public class BitmapUtils {
    public static Bitmap getBitmap(String bitmapUrl) throws IOException {
        URL url = new URL(bitmapUrl);
        URLConnection conn = url.openConnection();
        InputStream stream = conn.getInputStream();
        try {
            return BitmapFactory.decodeStream(stream);
        } finally {
            stream.close();
        }
    }

    public static void loadBitmapIntoImageViewAsync(URL url, ImageView imageView) {
        // TODO: Change this to handle image view recycling per http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
        // TODO: Implement image load caching (perhaps in the worker task?)
        // TODO: Move to use Volley for this per http://developer.android.com/training/volley/index.html
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(url);
    }
}
