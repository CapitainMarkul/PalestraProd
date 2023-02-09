package palestra.palestra;

/**
 * Created by Dmitry on 14.04.2017.
 */

public class ConnectionToWCF {

    //public static final String NAMESPACE = "http://nexus525525-001-site1.dtempurl.com/";
    //public static final String URL = "http://nexus525525-001-site1.dtempurl.com/server/Service1.svc/basic";
    //public static final String SOAP_ACTION = "http://nexus525525-001-site1.dtempurl.com/IService1/";

    public static final String NAMESPACE = "http://lowelly525-001-site1.itempurl.com/";
    public static final String URL = "http://lowelly525-001-site1.itempurl.com/server/Service1.svc/basic";
    public static final String SOAP_ACTION = "http://lowelly525-001-site1.itempurl.com/IService1/";
    //Methods name (Service1)

    public static final String METHOD_ADD_NEW_EVENT = "AddNewEvent";
    public static final String METHOD_SELECT_ALL_EVENTS_NOT_CREATOR = "SelectAllEventsNotCreator";
    public static final String METHOD_SELECT_ALL_EVENTS_CREATOR = "SelectAllEventsCreator";
    public static final String METHOD_CHECK_PARTICIPANT = "CheckParticipant";
    public static final String METHOD_LOGIN = "LoginUser";
    public static final String METHOD_COUNT_EVENTS_IN_USER = "CountEventInUser";
    public static final String METHOD_REGISTRATION_USER = "AddNewUser";
    public static final String METHOD_SELECT_ALL_EVENTS = "SelectAllEvents";
    public static final String METHOD_UPDATE_COUNT_REVIEW = "UpCountReview";
    public static final String METHOD_WILL_GO_YES = "WillGoYes";
    public static final String METHOD_WILL_GO_NO = "DeleteUserInEvent";
    public static final String METHOD_COUNT_INFO_USER = "CountInfoUser";
    public static final String METHOD_CHECK_REGISTER = "UserRegister";
    public static final String METHOD_SELECT_ALL_CATEGORES = "SelectAllEventsCategory";
    public static final String METHOD_DELETE_EVENT = "DeleteEvent";

    private static ConnectionToWCF instance;

    private ConnectionToWCF(){
    }

    public static synchronized ConnectionToWCF getInstance(){
        if(instance==null){
            instance = new ConnectionToWCF();
        }
        return instance;
    }

    //SOAP Action
    public String SOAP_ACTION(String methodName){
        return SOAP_ACTION + methodName;
    }
}
