package info.deez.deezbgg.ui.fragment.collection;

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

class CollectionFragmentAdapter extends BaseAdapter {
    private static final String TAG = "CollectionFragmentAdapter";
    private List<CollectionFragmentRowData> mCollection;
    private Context mContext;

    public CollectionFragmentAdapter(Context context) {
        mContext = context;
    }

    public void swapCollectionData(List<CollectionFragmentRowData> collection) {
        Log.i(TAG, "Swapping collection data");
        mCollection = collection;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = mCollection != null ? mCollection.size() : 0;
        Log.i(TAG, "Getting count (value = " + count + ")");
        return count;
    }

    @Override
    public CollectionFragmentRowData getItem(int position) {
        Log.i(TAG, "Getting item at position " + position);
        return mCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "Getting item id at position " + position);
        return mCollection.get(position).collectionItem.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "In getView for position " + position);
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.row_collection, parent, false);
        }
        CollectionFragmentRowData rowData = getItem(position);
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
        }
        return v;
    }
}
