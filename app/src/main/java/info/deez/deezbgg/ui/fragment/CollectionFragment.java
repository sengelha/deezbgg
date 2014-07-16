package info.deez.deezbgg.ui.fragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import info.deez.deezbgg.R;
import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;

public class CollectionFragment extends ListFragment {
    class CollectionFragmentAdapter extends BaseAdapter {
        private static final String TAG = "CollectionFragmentAdapter";
        private Context mContext;
        private List<CollectionItem> mCollectionItems;

        public CollectionFragmentAdapter(Context context, List<CollectionItem> collectionItems) {
            mContext = context;
            mCollectionItems = collectionItems;
        }

        @Override
        public int getCount() {
            return mCollectionItems.size();
        }

        @Override
        public CollectionItem getItem(int position) {
            return mCollectionItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCollectionItems.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(mContext);
                v = vi.inflate(R.layout.fragment_collection, parent, false);
            }
            CollectionItem collectionItem = getItem(position);
            if (collectionItem != null) {
                ImageView iv = (ImageView) v.findViewById(R.id.flag);
                if (iv != null) {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Log.i(TAG, "Downloading " + collectionItem.boardGame.thumbnailUrl);
                        URL url = new URL(collectionItem.boardGame.thumbnailUrl);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        iv.setImageBitmap(bmp);
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading board game thumbnail", e);
                    }
                }
                TextView tvTitle = (TextView) v.findViewById(R.id.txt);
                if (tvTitle != null) {
                    tvTitle.setText(collectionItem.boardGame.name);
                }
            }
            return v;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
        CollectionItem collectionItem = new CollectionItem();
        collectionItem.id = 1;
        collectionItem.boardGame = new BoardGame();
        collectionItem.boardGame.id = 1;
        collectionItem.boardGame.name = "Agricola";
        collectionItem.boardGame.imageUrl = "http://cf.geekdo-images.com/images/pic259085.jpg";
        collectionItem.boardGame.thumbnailUrl = "http://cf.geekdo-images.com/images/pic259085_t.jpg";
        collectionItems.add(collectionItem);
        CollectionItem collectionItem2 = new CollectionItem();
        collectionItem2.id = 2;
        collectionItem2.boardGame = new BoardGame();
        collectionItem2.boardGame.id = 2;
        collectionItem2.boardGame.name = "Blokus";
        collectionItem2.boardGame.imageUrl = "http://cf.geekdo-images.com/images/pic153979.jpg";
        collectionItem2.boardGame.thumbnailUrl = "http://cf.geekdo-images.com/images/pic153979_t.jpg";
        collectionItems.add(collectionItem2);
        CollectionItem collectionItem3 = new CollectionItem();
        collectionItem3.id = 3;
        collectionItem3.boardGame = new BoardGame();
        collectionItem3.boardGame.id = 3;
        collectionItem3.boardGame.name = "Carcassonne";
        collectionItem3.boardGame.imageUrl = "http://cf.geekdo-images.com/images/pic166867.jpg";
        collectionItem3.boardGame.thumbnailUrl = "http://cf.geekdo-images.com/images/pic166867_t.jpg";
        collectionItems.add(collectionItem3);

        setListAdapter(new CollectionFragmentAdapter(getActivity(), collectionItems));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
}
