package info.deez.deezbgg.bitmap;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapMemoryCache extends LruCache<String, Bitmap> {
    public BitmapMemoryCache(int cacheSize) {
        super(cacheSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        return bitmap.getByteCount() / 1024;
    }
}
