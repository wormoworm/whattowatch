package uk.tomhomewood.whattowatch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.tomhomewood.whattowatch.titles.Genre;
import uk.tomhomewood.whattowatch.titles.Title;
import uk.tomhomewood.whattowatch.titles.TitleType;
import uk.tomhomewood.whattowatch.watchitems.WatchItem;

/**
 * Created by Tom on 06/02/2016.
 */
public class Database extends SQLiteOpenHelper {
    private static final String TAG = "TitleDatabase";

    private static final String DATABASE_NAME = "titles";
    private static final int DATABASE_VERSION = 1;

    //Tables
    private final String TABLE_TITLES = "titles";
    private final String TABLE_WATCH_ITEMS = "watchItems";
    private final String TABLE_TITLE_GENRES = "titleGenres";

    //Shared columns
    private final String COLUMN_ID = "id";
    private final String COLUMN_TITLE_ID = "titleId";

    //Columns for the "titles" table
    private final String COLUMN_TYPE = "type";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_TAGLINE = "tagline";
    private final String COLUMN_DESCRIPTION = "description";
    private final String COLUMN_BANNER_PATH = "banner";
    private final String COLUMN_POSTER_PATH = "poster";
    private final String COLUMN_RATING = "rating";
    //private final String COLUMN_INFO_URL = "infoUrl";

    //Columns for the "watch items" table
    private final String COLUMN_WATCH_COUNT = "watchCount";
    private final String COLUMN_LAST_WATCHED = "lastWatched";
    private final String COLUMN_WATCH_METRICS = "metrics";

    //Columns for the "title genres" table
    private final String COLUMN_GENRE_ID = "genreId";

    //Table creation statements
    private final String CREATE_TABLE_TITLES = "CREATE TABLE IF NOT EXISTS "+TABLE_TITLES+" ("+COLUMN_ID+" INTEGER, "+COLUMN_TYPE+" INTEGER, "+COLUMN_NAME+" TEXT, "+COLUMN_TAGLINE+" TEXT, "+COLUMN_DESCRIPTION+" TEXT, "+ COLUMN_BANNER_PATH +" TEXT, "+ COLUMN_POSTER_PATH +" TEXT, "+COLUMN_RATING+" INTEGER)";
    private final String CREATE_TABLE_WATCH_ITEMS = "CREATE TABLE IF NOT EXISTS "+TABLE_WATCH_ITEMS+" ("+COLUMN_ID+" INTEGER PRIMARY KEY, "+COLUMN_TITLE_ID+" INTEGER, "+COLUMN_WATCH_COUNT+" TEXT ,"+COLUMN_LAST_WATCHED+" INTEGER, "+ COLUMN_WATCH_METRICS +" TEXT)";
    private final String CREATE_TABLE_TITLE_GENRES = "CREATE TABLE IF NOT EXISTS "+TABLE_TITLE_GENRES+" ("+COLUMN_TITLE_ID+" INTEGER, "+COLUMN_GENRE_ID+" INTEGER)";

    //Table deletion statements
    private final String DROP_TABLE_TITLES = "DROP TABLE IF EXISTS "+TABLE_TITLES;
    private final String DROP_TABLE_WATCH_ITEMS = "DROP TABLE IF EXISTS "+TABLE_WATCH_ITEMS;
    private final String DROP_TABLE_TITLE_GENRES = "DROP TABLE IF EXISTS "+TABLE_TITLE_GENRES;

    //Query column arrays
    private final String[] QUERY_COLUMNS_TITLES = {COLUMN_ID, COLUMN_TYPE, COLUMN_NAME, COLUMN_TAGLINE, COLUMN_DESCRIPTION, COLUMN_BANNER_PATH, COLUMN_POSTER_PATH, COLUMN_RATING};
    private final String[] QUERY_COLUMNS_TITLE_GENRES = {COLUMN_TITLE_ID, COLUMN_GENRE_ID};

    private final SQLiteDatabase database;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTables(database);
        Log.i(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    /**
     * Closes this {@link Database}. Call this when you are finished with your TitleDatabase object.
     */
    public void close(){
        database.close();
    }

    /**
     * Empties all tables in this {@link Database}.
     */
    public void empty() {
        dropTables();
        createTables(database);
        Log.i(TAG, "Emptied database");
    }

    /**
     * Begins an atomic transaction block. Actions performed whilst in the transaction will not be committed to the underlying {@link SQLiteDatabase}
     * until {@link #endTransaction()} is called. If {@link #cancelTransaction()} is called instead, then all operations performed since this
     * call will be rolled back.
     */
    public void beginTransaction(){
        database.beginTransaction();
    }

    /**
     * Completes the current transaction, if one is ongoing. See {@link #beginTransaction()}.
     */
    public void endTransaction() {
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    /**
     * Cancels the current transaction, if one is ongoing. See {@link #beginTransaction()}.
     */
    public void cancelTransaction(){
        database.endTransaction();
    }

    //Titles

    public boolean addTitle(Title title){
        try {
            //All operations are grouped into an atomic transaction
            beginTransaction();
            //First, insert the title
            database.insertOrThrow(TABLE_TITLES, null, createRowForTitle(title));
            //Now, insert Title-Genre entries for all Genres belonging to this Title
            for(Genre genre : title.getGenres()) insertTitleGenreEntry(title, genre);
            endTransaction();
            Log.i(TAG, "Inserted Title, ID: "+title.getId());
            return true;
        }
        catch(SQLException e){
            Log.e(TAG, "Could not insert Title", e);
            cancelTransaction();
            return false;
        }
    }

    public Title getTitle(int titleId){
        //Use a transaction to improve performance
        beginTransaction();
        //First, get the Title itself
        Cursor cursor = database.query(TABLE_TITLES, QUERY_COLUMNS_TITLES, COLUMN_ID + "=?", new String[]{Integer.toString(titleId)}, null, null, null, null);
        Title title = null;
        if(cursor.moveToFirst()) title = getTitleFromCursor(cursor);
        cursor.close();
        //Next, get all Genres associated with this Title
        if(title!=null) title.setGenres(getGenresForTitle(title));
        endTransaction();
        return title;
    }

    public List<Genre> getGenresForTitle(int titleId){
        Cursor cursor = database.query(TABLE_TITLE_GENRES, QUERY_COLUMNS_TITLE_GENRES, COLUMN_TITLE_ID + "=?", new String[]{Integer.toString(titleId)}, null, null, null, null);
        List<Genre> genres = new ArrayList<>(cursor.getCount());
        while(cursor.moveToNext()) genres.add(getTitleGenreFromCursor(cursor));
        cursor.close();
        return genres;
    }

    public List<Genre> getGenresForTitle(Title title){
        return getGenresForTitle(title.getId());
    }

    private Title getTitleFromCursor(Cursor cursor) {
        return new Title(cursor.getInt(0))
                .setType(TitleType.fromTypeCode(cursor.getInt(1)))
                .setName(cursor.getString(2))
                .setTagline(cursor.getString(3))
                .setDescription(cursor.getString(4))
                .setBannerPath(cursor.getString(5))
                .setPosterPath(cursor.getString(6))
                .setRating(cursor.getInt(7));
    }

    private Genre getTitleGenreFromCursor(Cursor cursor) {
        return Genre.fromId(cursor.getInt(1));
    }

    private void insertTitleGenreEntry(Title title, Genre genre) throws SQLException {
        database.insertOrThrow(TABLE_TITLE_GENRES, null, createRowForTitleGenreEntry(title, genre));
    }

    private ContentValues createRowForTitleGenreEntry(Title title, Genre genre) {
        ContentValues row = new ContentValues();
        row.put(COLUMN_TITLE_ID, title.getId());
        row.put(COLUMN_GENRE_ID, genre.getId());
        return row;
    }

    private ContentValues createRowForTitle(Title title) {
        ContentValues row = new ContentValues();
        row.put(COLUMN_ID, title.getId());
        row.put(COLUMN_TYPE, title.getType().getTypeCode());
        row.put(COLUMN_NAME, title.getName());
        row.put(COLUMN_TAGLINE, title.getTagline());
        row.put(COLUMN_DESCRIPTION, title.getDescription());
        row.put(COLUMN_BANNER_PATH, title.getBannerPath());
        row.put(COLUMN_POSTER_PATH, title.getPosterPath());
        row.put(COLUMN_RATING, title.getRating());
        return row;
    }

    public boolean removeTitle(int titleId){
        return true;        //TODO
    }

    public boolean removeTitle(Title title){
        return removeTitle(title.getId());
    }

    public boolean addWatchItem(WatchItem watchItem){
        try {
            long id = database.insertOrThrow(TABLE_WATCH_ITEMS, null, createRowForWatchItem(watchItem));
            Log.i(TAG, "Inserted WatchItem, ID: "+id);
            return true;
        }
        catch(SQLException e){
            Log.e(TAG, "Could not insert WatchItem", e);
            return false;
        }
    }

    private ContentValues createRowForWatchItem(WatchItem watchItem) {
        ContentValues row = new ContentValues();
        row.put(COLUMN_ID, watchItem.getId());
        row.put(COLUMN_TITLE_ID, watchItem.getTitle().getId());
        row.put(COLUMN_WATCH_COUNT, watchItem.getWatchCount());
        row.put(COLUMN_LAST_WATCHED, watchItem.getLastWatched());
        row.put(COLUMN_WATCH_METRICS, watchItem.getWatchMetrics().toString());
        return row;
    }

    public boolean removeWatchItem(int watchItemId){
        return true;        //TODO
    }

    public boolean removeWatchItem(WatchItem watchItem){
        if(watchItem.hasId()) return removeTitle(watchItem.getId());
        else return false;
    }

    private void createTables(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_TITLES);
        database.execSQL(CREATE_TABLE_WATCH_ITEMS);
        database.execSQL(CREATE_TABLE_TITLE_GENRES);
    }

    private void dropTables() {
        database.execSQL(DROP_TABLE_TITLES);
        database.execSQL(DROP_TABLE_WATCH_ITEMS);
        database.execSQL(DROP_TABLE_TITLE_GENRES);
    }
}