package palestra.palestra;

import java.util.Calendar;

/**
 * Created by Dmitry on 03.06.2017.
 */

public class WorkWithDate {
//
//    private String inputDate = null;
//    public WorkWithDate(String inputDate){
//        this.inputDate = inputDate;
//    }

    Calendar dateAndTime= Calendar.getInstance();
    private int age;

    private String DateRelRegister(String date){
        String day;
        String month;
        String year;
        //String hour;

        int index = date.indexOf(' ');
        day = date.substring(0, index);

        date = date.substring(index+1);
        month = CheckMonth(date.substring(0, date.indexOf(' ')));

        index = date.indexOf(' ');
        date = date.substring(index+1);
        year = date.substring(0, date.indexOf(' '));

        setAge(Integer.parseInt(year));

//        index = date.indexOf(' ');
//        date = date.substring(index+1);
//        index = date.indexOf(' ');
//        date = date.substring(index+1);
//        hour = date;
        //"2011.01.12 14:34:00"
        date = year+"."+month+"."+day+" "+"00:00:00";

        return  date;
    }
    private String DateRel(String date){
        String day;
        String month;
        String year;
        String hour;

        int index = date.indexOf(' ');
        day = date.substring(0, index);

        date = date.substring(index+1);
        month = CheckMonth(date.substring(0, date.indexOf(' ')));

        index = date.indexOf(' ');
        date = date.substring(index+1);
        year = date.substring(0, date.indexOf(' '));

        index = date.indexOf(' ');
        date = date.substring(index+1);
        index = date.indexOf(' ');
        date = date.substring(index+1);
        hour = date;
        //"2011.01.12 14:34:00"
        date = year+"."+month+"."+day+" "+hour+":00";

        return  date;
    }

    private String CheckMonth(String month){
        switch (month){
            case "января":{
                return "01";
            }
            case "февраля":{
                return "02";
            }
            case "марта":{
                return "03";
            }
            case "апреля":{
                return "04";
            }
            case "мая":{
                return "05";
            }
            case "июня":{
                return "06";
            }
            case "июля":{
                return "07";
            }
            case "августа":{
                return "08";
            }
            case "сентября":{
                return "09";
            }
            case "октября":{
                return "10";
            }
            case "ноября":{
                return "11";
            }
            case "декабря":{
                return "12";
            }
            default:{
                return "01";
            }
        }
    }

    private void setAge(int yearBirthday){
        age = dateAndTime.getInstance().get(Calendar.YEAR) - yearBirthday;
    }
    public int getUserAge(String inputDate){
        DateRelRegister(inputDate);
        return age;
    }
    public String getBirthdaySQLFormat(String inputDate){
        return DateRelRegister(inputDate);
    }
    public String getDateSQLFormat(String inputDate){
        return DateRel(inputDate);
    }
}
