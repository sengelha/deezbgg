package info.deez.deezbgg.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sengelh on 7/18/2014.
 */
public class BitmapUtils {
    public static void loadBitmapIntoImageViewAsync(URL url, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(url);
    }
}
