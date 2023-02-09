package palestra.palestra_v11;

import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

public class EventInfoRoot extends ExpandableGroup<EventInfoRoot.EventInfoChildren> {
    private final String
            titleEvent,
            briefInfo,
            categoryName,
            creatorName;
    private final int
            idCreator,
            eventCategoryID,
            creatorAvatarID,
            eventID,
            ageLimit;

    private ArrayList<EventInfoChildren> eventInfoChildrens;

    public EventInfoRoot(String titleEvent,
                         String briefInfo,
                         String categoryName,
                         String creatorName,
                         int idCreator,
                         int eventCategoryID,
                         int creatorAvatarID,
                         int eventID,
                         int ageLimit,
                         ArrayList<EventInfoChildren> eventInfoChildrens) {
        super(titleEvent, eventInfoChildrens);
        this.titleEvent = titleEvent;
        this.briefInfo = briefInfo;
        this.categoryName = categoryName;
        this.creatorName = creatorName;
        this.idCreator = idCreator;
        this.eventCategoryID = eventCategoryID;
        this.creatorAvatarID = creatorAvatarID;
        this.eventID = eventID;
        this.ageLimit = ageLimit;
        this.eventInfoChildrens = eventInfoChildrens;
    }

    public String getTitleEvent() {
        return titleEvent;
    }

    public String getBriefInfo() {
        return briefInfo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public int getEventCategoryID() {
        return eventCategoryID;
    }

    public int getCreatorAvatarID() {
        return creatorAvatarID;
    }

    public int getEventID() {
        return eventID;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public ArrayList<EventInfoChildren> getEventInfoChildrens() {
        return eventInfoChildrens;
    }

    public static class EventInfoChildren implements Parcelable {
        private final String
                latitude,
                longitude,
                dateOf;
        private final int
                countReview,
                countParticipant;

        public EventInfoChildren(String latitude,
                                 String longitude,
                                 String dateOf,
                                 int countReview,
                                 int countParticipant) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.dateOf = dateOf;
            this.countReview = countReview;
            this.countParticipant = countParticipant;
        }

        protected EventInfoChildren(Parcel in) {
            latitude = in.readString();
            longitude = in.readString();
            dateOf = in.readString();
            countReview = in.readInt();
            countParticipant = in.readInt();
        }

        public static final Creator<EventInfoChildren> CREATOR = new Creator<EventInfoChildren>() {
            @Override
            public EventInfoChildren createFromParcel(Parcel in) {
                return new EventInfoChildren(in);
            }

            @Override
            public EventInfoChildren[] newArray(int size) {
                return new EventInfoChildren[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeStringArray(
                    new String[]{latitude, longitude, dateOf, String.valueOf(countReview), String.valueOf(countParticipant)});
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getDateOf() {
            return dateOf;
        }

        public int getCountReview() {
            return countReview;
        }

        public int getCountParticipant() {
            return countParticipant;
        }
    }
}

