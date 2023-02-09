package palestra.palestra;

/**
 * Created by Dmitry on 09.04.2017.
 */

public class Event {
    private final String
            title,
            briefInfo,
            latitude,
            longitude,
            dateOf,
            eventCategoryName,
            nameCreator;
    private final int countReview, countParticipant, eventID, ageLimit, eventCategoryID, idCreator, avatarID;

    Event(int eventID, String title, String briefInfo, String dateOf, int ageLimit,
            int eventCategoryID, String eventCategoryName,
            String latitude, String longitude,
            int countReview, int countParticipant, String nameCreator, int idCreator, int avatarID) {
        this.eventID = eventID;
        this.title = title;
        this.briefInfo = briefInfo;
        this.dateOf = dateOf;
        this.ageLimit = ageLimit;
        this.eventCategoryID = eventCategoryID;
        this.eventCategoryName = eventCategoryName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countReview = countReview;
        this.countParticipant = countParticipant;
        this.nameCreator = nameCreator;
        this.idCreator = idCreator;
        this.avatarID = avatarID;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public int getEventID() {
        return eventID;
    }

    public String getNameCreator() {
        return nameCreator;
    }

    public String getTitle() {
        return title;
    }

    public String getBriefInfo() {
        return briefInfo;
    }

    public String getDate() {
        return dateOf;
    }

    public int getAgeLimit(){ return ageLimit; }

    public int getEventCategoryID() {return eventCategoryID;}

    public String getEventCategoryName() {return eventCategoryName;}

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getCountReview() {
        return countReview;
    }

    public int getCountParticipant() {
        return countParticipant;
    }
}
