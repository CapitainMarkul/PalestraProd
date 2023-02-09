package palestra.palestra;

import java.util.ArrayList;

/**
 * Created by Dmitry on 21.06.2017.
 */

public class DeletedEvents {

    private ArrayList<Integer> arrayDeletedEvent = new ArrayList<>();

    private static DeletedEvents instance;

    private DeletedEvents(){
    }

    public static synchronized DeletedEvents getInstance(){
        if(instance==null){
            instance = new DeletedEvents();
        }
        return instance;
    }

    public void addEventId(int eventID){
        arrayDeletedEvent.add(eventID);
    }

    public boolean isEventDelete(int eventID){
        if (arrayDeletedEvent.indexOf(eventID) != -1)
            return true;
        return false;
    }
}
