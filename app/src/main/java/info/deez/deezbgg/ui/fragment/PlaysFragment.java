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
import info.deez.deezbgg.entity.Play;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.PlayRepository;

public class PlaysFragment extends ListFragment {
    class PlaysFragmentRowData {
        public Play play;
        public BoardGame boardGame;
    }

    class PlaysFragmentAdapter extends BaseAdapter {
        private static final String TAG = "PlaysFragmentAdapter";
        private Context mContext;

        public PlaysFragmentAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return PlayRepository.getAllPlays().size();
        }

        @Override
        public PlaysFragmentRowData getItem(int position) {
            PlaysFragmentRowData rowData = new PlaysFragmentRowData();
            rowData.play = PlayRepository.getAllPlays().get(position);
            rowData.boardGame = BoardGameRepository.getBoardGameById(rowData.play.boardGameId);
            return rowData;
        }

        @Override
        public long getItemId(int position) {
            return PlayRepository.getAllPlays().get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(mContext);
                v = vi.inflate(R.layout.fragment_plays, parent, false);
            }
            PlaysFragmentRowData rowData = getItem(position);
            if (rowData != null) {
                if (rowData.boardGame != null && rowData.boardGame.thumbnailUrl != null) {
                    ImageView iv = (ImageView) v.findViewById(R.id.flag);
                    if (iv != null) {
                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            Log.i(TAG, "Downloading " + rowData.boardGame.thumbnailUrl);
                            URL url = new URL(rowData.boardGame.thumbnailUrl);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            iv.setImageBitmap(bmp);
                        } catch (Exception e) {
                            Log.e(TAG, "Error loading board game thumbnail", e);
                        }
                    }
                }

                if (rowData.boardGame != null && rowData.boardGame.name != null) {
                    TextView tvTitle = (TextView) v.findViewById(R.id.txt);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(new PlaysFragmentAdapter(getActivity()));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
}
