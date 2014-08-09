package info.deez.deezbgg.sync.bggapi;

/**
 * Created by sengelha on 8/9/2014.
 */
public class CollectionItem {
    public class BoardGame {
        public long id;
        public String name;
        private boolean mImageUrlSet = false;
        private String mImageUrl;
        private boolean mThumbnailUrlSet = false;
        private String mThumbnailUrl;
        private boolean mYearPublishedSet = false;
        private int mYearPublished;

        public String getImageUrl() {
            if (!mImageUrlSet)
                throw new IllegalStateException();
            return mImageUrl;
        }

        public String getThumbnailUrl() {
            if (!mThumbnailUrlSet)
                throw new IllegalStateException();
            return mThumbnailUrl;
        }

        public int getYearPublished() {
            if (!mYearPublishedSet)
                throw new IllegalStateException();
            return mYearPublished;
        }

        public boolean isImageUrlSet() { return mImageUrlSet; }
        public boolean isThumbnailUrlSet() { return mThumbnailUrlSet; }
        public boolean isYearPublishedSet() { return mYearPublishedSet; }

        public void setImageUrl(String imageUrl) { mImageUrl = imageUrl; mImageUrlSet = true; }
        public void setThumbnailUrl(String thumbnailUrl) { mThumbnailUrl = thumbnailUrl; mThumbnailUrlSet = true; }
        public void setYearPublished(int yearPublished) { mYearPublished = yearPublished; mYearPublishedSet = true; }
    }

    public long id;
    public BoardGame boardGame = new BoardGame();
    public boolean owned;
    public int numberOfPlays;
}
