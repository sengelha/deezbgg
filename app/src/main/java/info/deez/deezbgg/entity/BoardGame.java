package info.deez.deezbgg.entity;

import java.net.URL;

import info.deez.deezbgg.utils.StringUtils;

/**
 * Created by sengelha on 7/15/2014.
 */
public class BoardGame {
    public long id;
    public String name;
    private boolean mThumbnailUrlSet = false;
    private String mThumbnailUrl;
    private boolean mYearPublishedSet = false;
    private int mYearPublished;

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

    public boolean isThumbnailUrlSet() { return mThumbnailUrlSet; }
    public boolean isYearPublishedSet() { return mYearPublishedSet; }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
        mThumbnailUrlSet = true;
    }

    public void setYearPublished(int yearPublished) {
        mYearPublished = yearPublished;
        mYearPublishedSet = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof BoardGame))
            return false;
        BoardGame rhs = (BoardGame)obj;
        return
                id == rhs.id &&
                StringUtils.areEqualOrBothNull(name, rhs.name) &&
                mThumbnailUrlSet == rhs.mThumbnailUrlSet &&
                StringUtils.areEqualOrBothNull(mThumbnailUrl, rhs.mThumbnailUrl) &&
                mYearPublishedSet == rhs.mYearPublishedSet &&
                mYearPublished == rhs.mYearPublished;
    }
}
