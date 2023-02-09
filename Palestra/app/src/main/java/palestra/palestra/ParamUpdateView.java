package palestra.palestra;

/**
 * Created by Dmitry on 22.04.2017.
 */

public class ParamUpdateView {
    private static ParamUpdateView instance;

    private ParamUpdateView(){
    }

    public static synchronized ParamUpdateView getInstance(){
        if(instance==null){
            instance = new ParamUpdateView();
        }
        return instance;
    }

    private String TYPE_EVENT = "ALL_EVENTS";

    private String currentView = "CARD";

    private final String ALL_EVENS = "ALL_EVENTS";
    private final String MY_FUTURE_EVENS = "MY_FUTURE_EVENTS";
    private final String MY_PAST_EVENS = "MY_PAST_EVENTS";

    public String getALL_EVENS(){
        return ALL_EVENS;
    }

    public String getMY_FUTURE_EVENS(){
        return MY_FUTURE_EVENS;
    }

    public String getMY_PAST_EVENS(){
        return MY_PAST_EVENS;
    }

    public String getTYPE_EVENT(){
        return TYPE_EVENT;
    }
    public void setTYPE_EVENT(String type_event){
        TYPE_EVENT = type_event;
    }
}
