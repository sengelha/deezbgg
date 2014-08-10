package info.deez.deezbgg.ui.fragment.collection;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import info.deez.deezbgg.R;
import info.deez.deezbgg.bitmap.AsyncDrawable;
import info.deez.deezbgg.bitmap.BitmapMemoryCache;
import info.deez.deezbgg.bitmap.BitmapWorkerTask;
import info.deez.deezbgg.content.ContentContract;

public class CollectionFragmentAdapter extends CursorAdapter {
    private class ViewHolder {
        public ImageView thumbnail;
        public TextView boardGameName;
        public TextView boardGameYearPublished;
    }

    private static final String TAG = "CollectionFragmentAdapter";

    public static final String[] COLUMNS = {
            ContentContract.CollectionItemEntry._ID,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL,
    };

    private Context mContext;
    private BitmapMemoryCache mMemoryCache;

    public CollectionFragmentAdapter(Context context, Cursor cursor, int flags, BitmapMemoryCache memoryCache) {
        super(context, cursor, flags);
        mContext = context;
        mMemoryCache = memoryCache;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.i(TAG, "In CollectionFragmentAdapter.newView()");

        LayoutInflater inflater = LayoutInflater.from(context);
        View layoutView = inflater.inflate(R.layout.row_collection, null);

        ViewHolder holder = new ViewHolder();
        holder.thumbnail = (ImageView) layoutView.findViewById(R.id.boardGameThumbnail);
        holder.boardGameName = (TextView) layoutView.findViewById(R.id.boardGameName);
        holder.boardGameYearPublished = (TextView) layoutView.findViewById(R.id.boardGameYearPublished);
        layoutView.setTag(holder);

        return layoutView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.i(TAG, "In CollectionFragmentAdapter.bindView()");

        long boardGameId = cursor.getLong(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID));
        String boardGameName = cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME));
        String thumbnailUrl = cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL));
        String yearPublished = cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED));

        ViewHolder holder = (ViewHolder) view.getTag();

        if (thumbnailUrl != null) {
            if (cancelPotentialWork(thumbnailUrl, holder.thumbnail)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(holder.thumbnail, mMemoryCache);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), null, task);
                holder.thumbnail.setImageDrawable(asyncDrawable);
                task.execute(thumbnailUrl);
            }
        } else {
            holder.thumbnail.setImageResource(R.drawable.no_image_available);
        }
        holder.boardGameName.setText(boardGameName != null ? boardGameName : Long.toString(boardGameId));
        holder.boardGameYearPublished.setText(yearPublished != null ? yearPublished : context.getString(R.string.unknown));
    }

    private boolean cancelPotentialWork(String url, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String currentUrl = bitmapWorkerTask.getUrl();
            if (currentUrl == null || !currentUrl.equals(url)) {
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}