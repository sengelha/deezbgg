package info.deez.deezbgg.ui.fragment.plays;

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

public class PlaysFragmentAdapter extends CursorAdapter {
    private class ViewHolder {
        public ImageView thumbnail;
        public TextView boardGameName;
        public TextView playDate;
    }

    private static final String TAG = "PlaysFragmentAdapter";

    public static final String[] COLUMNS = {
            ContentContract.PlayEntry._ID,
            ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_NAME,
            ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL,
            ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE,
    };

    private Context mContext;
    private BitmapMemoryCache mMemoryCache;

    public PlaysFragmentAdapter(Context context, Cursor cursor, int flags, BitmapMemoryCache memoryCache) {
        super(context, cursor, flags);
        mContext = context;
        mMemoryCache = memoryCache;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.i(TAG, "In PlaysFragmentAdapter.newView()");

        LayoutInflater inflater = LayoutInflater.from(context);
        View layoutView = inflater.inflate(R.layout.row_plays, null);

        ViewHolder holder = new ViewHolder();
        holder.thumbnail = (ImageView) layoutView.findViewById(R.id.boardGameThumbnail);
        holder.boardGameName = (TextView) layoutView.findViewById(R.id.boardGameName);
        holder.playDate = (TextView) layoutView.findViewById(R.id.playDate);
        layoutView.setTag(holder);

        return layoutView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.i(TAG, "In PlaysFragmentAdapter.bindView()");

        String thumbnailUrl = cursor.getString(cursor.getColumnIndex(ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL));

        ViewHolder holder = (ViewHolder) view.getTag();

        if (thumbnailUrl != null) {
            if (cancelPotentialWork(thumbnailUrl, holder.thumbnail)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(holder.thumbnail, mMemoryCache);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), null, task);
                holder.thumbnail.setImageDrawable(asyncDrawable);
                task.execute(thumbnailUrl);
            }
        }
        holder.boardGameName.setText(cursor.getString(cursor.getColumnIndex(ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_NAME)));
        holder.playDate.setText(cursor.getString(cursor.getColumnIndex(ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE)));
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