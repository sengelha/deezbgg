package info.deez.deezbgg.ui.fragment.plays;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.deez.deezbgg.R;
import info.deez.deezbgg.bitmap.BitmapUtils;
import info.deez.deezbgg.content.ContentContract;

public class PlaysFragmentAdapter extends CursorAdapter {
    private class ViewHolder {
        public TextView boardGameName;
        public TextView playDate;
    }

    private static final String TAG = "PlaysFragmentAdapter";

    public static final String[] COLUMNS = {
            ContentContract.PlayEntry._ID,
            ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_NAME,
            ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE,
    };

    public PlaysFragmentAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.i(TAG, "In PlaysFragmentAdapter.newView()");

        LayoutInflater inflater = LayoutInflater.from(context);
        View layoutView = inflater.inflate(R.layout.row_plays, null);

        ViewHolder holder = new ViewHolder();
        holder.boardGameName = (TextView) layoutView.findViewById(R.id.boardGameName);
        holder.playDate = (TextView) layoutView.findViewById(R.id.playDate);
        layoutView.setTag(holder);

        return layoutView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.i(TAG, "In PlaysFragmentAdapter.bindView()");

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.boardGameName.setText(cursor.getString(cursor.getColumnIndex(ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_NAME)));
        holder.playDate.setText(cursor.getString(cursor.getColumnIndex(ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE)));
    }
}
