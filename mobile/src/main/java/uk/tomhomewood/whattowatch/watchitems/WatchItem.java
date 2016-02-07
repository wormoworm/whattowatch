package uk.tomhomewood.whattowatch.watchitems;

import uk.tomhomewood.whattowatch.titles.Title;

/**
 * Created by Tom on 07/02/2016.
 */
public class WatchItem {

    private final Title title;
    private Integer id;
    private int watchCount;
    private long lastWatched;
    private WatchMetrics watchMetrics;

    public WatchItem(Title title) {
        this.title = title;
    }

    public Title getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasId() {
        return getId()!=null;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public long getLastWatched() {
        return lastWatched;
    }

    public void setLastWatched(long lastWatched) {
        this.lastWatched = lastWatched;
    }

    public WatchMetrics getWatchMetrics() {
        return watchMetrics;
    }

    public void setWatchMetrics(WatchMetrics watchMetrics) {
        this.watchMetrics = watchMetrics;
    }
}