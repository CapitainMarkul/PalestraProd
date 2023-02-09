package palestra.palestra;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dmitry on 13.06.2017.
 */

public class EventCategores {

    private HashMap<Integer,String> categoresList = new HashMap<>();


    private static EventCategores instance;

    private EventCategores(){
    }

    public static synchronized EventCategores getInstance(){

        if(instance==null){
            instance = new EventCategores();
        }
        return instance;
    }

    public void setCategores(int id, String value){
        categoresList.put(id,value);
    }

    public String[] getString(){
        String[] temp = new String[categoresList.size()];
        for (int i =0 ; i < temp.length;i++) {
            temp[i] = categoresList.get(i);
        }
        Arrays.sort(temp);
        return temp;
    }
    public HashMap<Integer,String> getMap(){
        return categoresList;
    }
    public <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
