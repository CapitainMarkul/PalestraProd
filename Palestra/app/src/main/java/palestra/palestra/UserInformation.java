package palestra.palestra;

/**
 * Created by Dmitry on 13.04.2017.
 */

public class UserInformation {

    private int userID;
    private int age;
    private int userCategory;
    private String password;
    private String phoneNumber;
    private String registrationDate;
    private String userFirstName;
    private String userLastName;
    private String birthday;
    private String infoAboutUser;
    private String cityUser;
    private int avatarID;

    private static UserInformation instance;

    private UserInformation(){
    }

    public static synchronized UserInformation getInstance(){
        if(instance==null){
            instance = new UserInformation();
        }
        return instance;
    }


//Properties

    public String getCity(){return cityUser;}
    public void setCity(String cityUser){this.cityUser = cityUser;}

    public int getAvatarID(){return avatarID;}
    public void setAvatarID(int avatarID){this.avatarID = avatarID;}

    public String getUserFirstName(){return userFirstName;}
    public void setUserFirstName(String userFirstName){this.userFirstName = userFirstName;}

    public String getUserLastName(){return userLastName;}
    public void setUserLastName(String userLastName){this.userLastName = userLastName;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationDate(){
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate){
        this.registrationDate = registrationDate;
    }

    public int getAge(){
        return age;
    }
    public void setAge(int age){
        this.age = age;
    }

    public int getUserCategory(){
        return userCategory;
    }
    public void setUserCategory(int userCategory){
        this.userCategory = userCategory;
    }

    public String getBirthday(){return birthday;}
    public void setBirthday(String birthday){this.birthday = birthday;}

    public String getInfoAboutUser(){
        return infoAboutUser;
    }
    public void setInfoAboutUser(String infoAboutUser){
        this.infoAboutUser = infoAboutUser;
    }

}
