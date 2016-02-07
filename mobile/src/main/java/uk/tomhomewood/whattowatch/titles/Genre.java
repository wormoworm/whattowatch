package uk.tomhomewood.whattowatch.titles;

import android.content.res.Resources;

import uk.tomhomewood.whattowatch.R;

/**
 * Created by Tom on 07/02/2016.
 */
public enum Genre {

    UNDEFINED(-1, -1),

    ACTION(28, R.string.genre_action_name);

    private final int id;
    private final int nameResId;

    Genre(int id, int nameResId){
        this.id = id;
        this.nameResId = nameResId;
    }

    public int getId(){
        return id;
    }

    public String getName(Resources resources){
        return resources.getString(nameResId);
    }

    public static Genre fromId(int id){
        for(Genre genre : values()) if(id==genre.id) return genre;
        return UNDEFINED;
    }
}
