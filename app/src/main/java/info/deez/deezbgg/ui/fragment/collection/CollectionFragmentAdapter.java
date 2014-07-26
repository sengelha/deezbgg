package info.deez.deezbgg.ui.fragment.collection;

import android.content.Context;
import android.database.Cursor;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import info.deez.deezbgg.R;
import info.deez.deezbgg.bitmap.BitmapUtils;
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
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL,
    };

    public CollectionFragmentAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
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

        String thumbnailUrl = cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL));

        ViewHolder holder = (ViewHolder) view.getTag();

        if (thumbnailUrl != null) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i(TAG, "Loading thumbnail: " + thumbnailUrl);
            try {
                holder.thumbnail.setImageBitmap(BitmapUtils.getBitmap(thumbnailUrl));
            } catch (IOException e) {
                Log.e(TAG, "Error getting bitmap: " + e);
            }
        }
        holder.boardGameName.setText(cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME)));
        holder.boardGameYearPublished.setText(cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED)));
    }
}
