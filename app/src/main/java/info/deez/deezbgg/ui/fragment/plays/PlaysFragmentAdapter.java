package info.deez.deezbgg.ui.fragment.plays;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.deez.deezbgg.R;

class PlaysFragmentAdapter extends BaseAdapter {
    private static final String TAG = "PlaysFragmentAdapter";
    private List<PlaysFragmentRowData> mPlays;
    private Context mContext;

    public PlaysFragmentAdapter(Context context) {
        mContext = context;
    }

    public void swapPlayData(List<PlaysFragmentRowData> plays) {
        Log.i(TAG, "Swapping play data");
        mPlays = plays;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = mPlays != null ? mPlays.size() : 0;
        Log.i(TAG, "Getting count (value = " + count + ")");
        return count;
    }

    @Override
    public PlaysFragmentRowData getItem(int position) {
        Log.i(TAG, "Getting item at position " + position);
        return mPlays.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "Getting item id at position " + position);
        return mPlays.get(position).play.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "In getView for position " + position);

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.row_plays, parent, false);
        }
        PlaysFragmentRowData rowData = getItem(position);
        if (rowData != null) {
            if (rowData.boardGameBitmap != null) {
                ImageView iv = (ImageView) v.findViewById(R.id.boardGameThumbnail);
                if (iv != null) {
                    iv.setImageBitmap(rowData.boardGameBitmap);
                }
            }

            if (rowData.boardGame != null && rowData.boardGame.name != null) {
                TextView tvTitle = (TextView) v.findViewById(R.id.boardGameName);
                if (tvTitle != null) {
                    tvTitle.setText(rowData.boardGame.name);
                }
            }

            if (rowData.play != null && rowData.play.date != null) {
                TextView tvTitle = (TextView) v.findViewById(R.id.playDate);
                if (tvTitle != null) {
                    tvTitle.setText(rowData.play.date);
                }
            }
        }
        return v;
    }
}
