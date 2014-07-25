package info.deez.deezbgg.ui.fragment.collection;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import info.deez.deezbgg.R;
import info.deez.deezbgg.content.ContentContract;

public class CollectionFragmentAdapter extends CursorAdapter {
    private class ViewHolder {
        public TextView boardGameName;
        public TextView boardGameYearPublished;
    }

    private static final String TAG = "CollectionFragmentAdapter";

    public static final String[] COLUMNS = {
            ContentContract.CollectionItemEntry._ID,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED,
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
        holder.boardGameName = (TextView) layoutView.findViewById(R.id.boardGameName);
        holder.boardGameYearPublished = (TextView) layoutView.findViewById(R.id.boardGameYearPublished);
        layoutView.setTag(holder);

        return layoutView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.i(TAG, "In CollectionFragmentAdapter.bindView()");

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.boardGameName.setText(cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME)));
        holder.boardGameYearPublished.setText(cursor.getString(cursor.getColumnIndex(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED)));
    }
}
