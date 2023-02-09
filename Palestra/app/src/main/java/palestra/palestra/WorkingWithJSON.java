package palestra.palestra;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 12.04.2017.
 */

public class WorkingWithJSON {

    private String jsonString;
    private UserInformation userInformation;
    private EventCategores eventCategores;
    private int indexNextPage;


    public WorkingWithJSON(String jsonString){
        this.jsonString = jsonString;
        userInformation = UserInformation.getInstance();
        eventCategores = EventCategores.getInstance();
    }

    public String getUserName()  {
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null){
            JSONArray jsonArray = (JSONArray)jsonObject.get("User");
            JSONObject userName = (JSONObject)jsonArray.get(0);
            return userName.get("fullName").toString();
        }
        return "В поисках Вашего имени...";
    }

    public String getCountEventInUser(){
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("Count");
            JSONObject countEvents = (JSONObject) jsonArray.get(0);
            return countEvents.get("countEvents").toString();
        }
        return "0";
    }

    public ArrayList<Object> getCountInfoUser(){
        ArrayList<Object> allCountUser = new ArrayList<Object>();
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("UserInformation");
            JSONObject userInformation = (JSONObject)jsonArray.get(0);

            allCountUser.add(Integer.parseInt(userInformation.get("countAll").toString()));
            allCountUser.add(Integer.parseInt(userInformation.get("countCreatedEvent").toString()));
            allCountUser.add(Integer.parseInt(userInformation.get("countCreateActiveEvent").toString()));
            allCountUser.add(Integer.parseInt(userInformation.get("countParticipantEvent").toString()));
            allCountUser.add(Integer.parseInt(userInformation.get("countParticipantActiveEvent").toString()));
            allCountUser.add(userInformation.get("city").toString());
            allCountUser.add(Integer.parseInt(userInformation.get("age").toString()));

            return allCountUser;
        }
        return null;
    }

    public String getAddress(){
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");
            JSONObject address = (JSONObject) jsonArray.get(0);
            int index = address.get("formatted_address").toString().indexOf(',');
            index += address.get("formatted_address").toString().substring(index+1).indexOf(',');
            index += address.get("formatted_address").toString().substring(index+2).indexOf(',');
            return address.get("formatted_address").toString().substring(0, index+2);
        }
        return "null";
    }

    public boolean getCheckResister(){
        JSONObject jsonObject = JSONParsing();
        if(jsonObject==null) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("Participant");
            JSONObject countEvents = (JSONObject) jsonArray.get(0);
            return false;
        }
        return true;
    }

    public String getParticipant(){
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("Participant");
            JSONObject countEvents = (JSONObject) jsonArray.get(0);
            return countEvents.get("Value").toString();
        }
        return "false";
    }

    public void setUserInformation(){
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null){
            JSONArray jsonArray = (JSONArray)jsonObject.get("User");
            JSONObject userFullInfo = (JSONObject)jsonArray.get(0);

            userInformation.setUserID(Integer.parseInt(userFullInfo.get("userID").toString()));
            userInformation.setPhoneNumber(userFullInfo.get("phoneNumber").toString());
            userInformation.setRegistrationDate(userFullInfo.get("registrationDate").toString());
            userInformation.setUserFirstName(userFullInfo.get("firstName").toString());
            userInformation.setUserLastName(userFullInfo.get("lastName").toString());
            userInformation.setUserCategory(Integer.parseInt(userFullInfo.get("userCategory").toString()));
            if(userInformation.getUserCategory() == 1)
                userInformation.setAge(99);
            else
                userInformation.setAge(Integer.parseInt(userFullInfo.get("age").toString()));

            userInformation.setAvatarID(Integer.parseInt(userFullInfo.get("avatarID").toString()));
            userInformation.setCity(userFullInfo.get("city").toString());
            //userInformation.setInfoAboutUser(userFullInfo.get("infoAboutUser").toString());
        }
    }

    public void setEventsCategory(){
        JSONObject jsonObject = JSONParsing();
        if(jsonObject!=null){
            JSONArray jsonArray = (JSONArray)jsonObject.get("EventsCategory");

            for (Object eventsInfo : jsonArray) {
                JSONObject eventCategoresJson = (JSONObject) eventsInfo;
                eventCategores.setCategores(Integer.parseInt(eventCategoresJson.get("categoryID").toString()),
                        eventCategoresJson.get("title").toString());
            }
        }
    }

    private JSONObject JSONParsing(){
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();
        try {
             jsonObject = (JSONObject) jsonParser.parse(jsonString);
             return jsonObject;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public List<Event> getAllEvent() {

        List<Event> events = new ArrayList<Event>();

        JSONObject jsonObject = JSONParsing();
        if (jsonObject != null) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("Events");
            for (Object eventsInfo : jsonArray) {
                JSONObject eventFullInfo = (JSONObject) eventsInfo;

                int tempMaxIndex = Integer.parseInt(eventFullInfo.get("eventID").toString());
                if (indexNextPage == 0 || tempMaxIndex> indexNextPage){
                    indexNextPage = tempMaxIndex;
                }

                if(userInformation.getUserID() != Integer.parseInt(eventFullInfo.get("creatorId").toString())) {
                    if (userInformation.getAge() >= Integer.parseInt(eventFullInfo.get("ageLimit").toString())) {
                        events.add(AddEvent(
                                Integer.parseInt(eventFullInfo.get("eventID").toString()),
                                eventFullInfo.get("title").toString(),
                                eventFullInfo.get("briefInfo").toString(),
                                eventFullInfo.get("dateOf").toString(),
                                Integer.parseInt(eventFullInfo.get("ageLimit").toString()),
                                Integer.parseInt(eventFullInfo.get("eventCategoryID").toString()),
                                eventFullInfo.get("eventCategoryName").toString(),
                                eventFullInfo.get("latitude").toString(),
                                eventFullInfo.get("longitude").toString(),
                                Integer.parseInt(eventFullInfo.get("countReview").toString()),
                                Integer.parseInt(eventFullInfo.get("countParticipant").toString()),
                                eventFullInfo.get("creatorName").toString(),
                                Integer.parseInt(eventFullInfo.get("creatorId").toString()),
                                Integer.parseInt(eventFullInfo.get("avatarID").toString())));
                    }
                } else {
                    events.add(AddEvent(
                            Integer.parseInt(eventFullInfo.get("eventID").toString()),
                            eventFullInfo.get("title").toString(),
                            eventFullInfo.get("briefInfo").toString(),
                            eventFullInfo.get("dateOf").toString(),
                            Integer.parseInt(eventFullInfo.get("ageLimit").toString()),
                            Integer.parseInt(eventFullInfo.get("eventCategoryID").toString()),
                            eventFullInfo.get("eventCategoryName").toString(),
                            eventFullInfo.get("latitude").toString(),
                            eventFullInfo.get("longitude").toString(),
                            Integer.parseInt(eventFullInfo.get("countReview").toString()),
                            Integer.parseInt(eventFullInfo.get("countParticipant").toString()),
                            eventFullInfo.get("creatorName").toString(),
                            Integer.parseInt(eventFullInfo.get("creatorId").toString()),
                            Integer.parseInt(eventFullInfo.get("avatarID").toString())));
                }
            }
        }
        return events;
    }

    private Event AddEvent(int eventID, String title, String briefInfo,
                           String dateOf, int ageLimit,
                           int eventCategoryID, String eventCategoryName,
                           String latitude, String longitude,
                           int countReview, int countParticipant, String nameCreator,
                           int idCreator, int avatarID){
        return new Event(eventID,
                title,
                briefInfo,
                dateOf,
                ageLimit,
                eventCategoryID,
                eventCategoryName,
                latitude,
                longitude,
                countReview,
                countParticipant,
                nameCreator,
                idCreator,
                avatarID);
    }

    public int getIndexNextPage(){
        return indexNextPage;
    }
}
