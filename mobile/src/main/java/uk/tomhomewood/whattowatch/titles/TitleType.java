package uk.tomhomewood.whattowatch.titles;

/**
 * Created by Tom on 06/02/2016.
 */
public enum TitleType {

    UNDEFINED(-1),

    FILM(1),
    TV_EPISODE(2);

    private final int typeCode;

    TitleType(int intCode) {
        this.typeCode = intCode;
    }

    public int getTypeCode(){
        return typeCode;
    }

    public static TitleType fromTypeCode(int typeCode){
        for(TitleType type : values()) if(typeCode==type.typeCode) return type;
        return UNDEFINED;
    }
}