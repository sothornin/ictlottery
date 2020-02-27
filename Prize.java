import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Prize {
    static String[][] cycleDate = new String[3][3];
    private String description;
    private String[] number;
    private int prizeMoney;
    private int cycle;

    Prize() {
    }

    private static void setCycleDate(String[][] cycleDate) {
        Prize.cycleDate = cycleDate;
    }

    static void setCycleDate() {
        Object json = null;
        String[][] date = new String[3][3];
        try {

            FileReader reader = new FileReader("prize.json");
            JSONParser jsonParser = new JSONParser();
            json = jsonParser.parse(reader);

        } catch (ParseException | IOException e) {
            System.err.println("Cannot Read file");
            e.printStackTrace();
        }
        JSONArray array = (JSONArray) json;


        JSONObject[] dateObject = new JSONObject[3];
        for (int i = 0; i < 3; i++) {
            assert array != null;
            JSONObject d = (JSONObject) array.get(i);
            dateObject[i] = (JSONObject) d.get("Date");

        }
        for (int i = 0; i < 3; i++) {
            date[i][0] = (String) dateObject[i].get("day");
            date[i][1] = (String) dateObject[i].get("month");
            date[i][2] = (String) dateObject[i].get("year");
        }

        setCycleDate(date);
    }

    static String[][] getCycleDate() {
        return cycleDate;
    }

    void setPrizeMoney(int prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    private int getCycle() {
        return cycle;
    }

    void setCycle(int cycle) {
        this.cycle = cycle;
    }

    int getPrizeMoney() {
        return prizeMoney;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    private void setNumber(String[] number) {
        this.number = number;
    }

    void setNumber(String prizeNumber, int n) {
        Object json = null;
        String[] number = new String[n];
        try {

            FileReader reader = new FileReader("prize.json");
            JSONParser jsonParser = new JSONParser();
            json = jsonParser.parse(reader);

        } catch (ParseException | IOException e) {
            System.err.println("Cannot Read file");
            e.printStackTrace();
        }

        JSONArray array = (JSONArray) json;
        assert array != null;
        JSONObject all = (JSONObject) array.get(getCycle());
        JSONObject prize = (JSONObject) all.get("Prize");
        JSONArray first = (JSONArray) prize.get(prizeNumber);
        for (int i = 0; i < first.size(); i++) {
            number[i] = (String) first.get(i);
        }


        setNumber(number);
    }

    String[] getNumber() {
        return number;
    }
}
