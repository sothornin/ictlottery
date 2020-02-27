import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class User {
    void showDescription() {
        System.out.println("Welcome to ICT Lottery\nPrize number announce every 1 and 16 of every month");
        FirstPrize f = new FirstPrize(1);
        SecondPrize s = new SecondPrize(1);
        ThirdPrize t = new ThirdPrize(1);
        FourthPrize ff = new FourthPrize(1);
        System.out.println("First prize: " + f.getDescription() + "\nPrize: " + f.getPrizeMoney());
        System.out.println("Second prize: " + s.getDescription() + "\nPrize: " + s.getPrizeMoney());
        System.out.println("Third prize: " + t.getDescription() + "\nPrize: " + t.getPrizeMoney());
        System.out.println("Fourth prize: " + ff.getDescription() + "\nPrize: " + ff.getPrizeMoney());
        System.out.println("Press Enter to Exit");
        Main.enterKey();
    }

    private void showPrize() {
        FirstPrize[] f = new FirstPrize[2];
        SecondPrize[] s = new SecondPrize[2];
        ThirdPrize[] t = new ThirdPrize[2];
        FourthPrize[] ff = new FourthPrize[2];
        for (int i = 0; i < 2; i++) {
            f[i] = new FirstPrize(i);
            s[i] = new SecondPrize(i);
            t[i] = new ThirdPrize(i);
            ff[i] = new FourthPrize(i);
        }
        for (int i = 0; i < 2; i++) {
            System.out.println("Cycle: " + Prize.cycleDate[i][0] + "/" + Prize.cycleDate[i][1] + "/" + Prize.cycleDate[i][2]);
            System.out.println("1st Prize: " + Arrays.toString(f[i].getNumber()));
            System.out.println("2nd Prize: " + Arrays.toString(s[i].getNumber()));
            System.out.println("3rd Prize: " + Arrays.toString(t[i].getNumber()));
            System.out.println("4th Prize: " + Arrays.toString(ff[i].getNumber()));
            System.out.println();
            if (i == 0) {
                System.out.println("Press Enter to continue");
                Main.enterKey();
            }
        }
    }

    void olderPrize() {
        JSONParser jsonParser = new JSONParser();
        try {
            mainMenu:
            while (true) {
                System.out.println("1. Last 2 cycles\n2. Older cycles\n3. Back");
                int button = Main.inputButton(3, "Wrong number");
                if (button == 1) {
                    showPrize();
                    System.out.println("Press Enter to Exit");
                    Main.enterKey();
                    break;
                } else if (button == 2) {

                    Object json = jsonParser.parse(new FileReader("old.json"));
                    JSONArray array = (JSONArray) json;
                    int size = array.size();

                    while (true) {
                        for (int i = 1; i <= size; i++) {
                            JSONObject object = (JSONObject) array.get(i - 1);
                            JSONObject date = (JSONObject) object.get("Date");
                            System.out.println(i + ". " + date.get("day") + "/" + date.get("month") + "/" + date.get("year"));

                        }
                        System.out.println(size + 1 + ". back");

                        button = Main.inputButton(array.size() + 1, "Wrong number");
                        if (button != size + 1) {
                            JSONObject object = (JSONObject) array.get(button - 1);
                            JSONObject prize = (JSONObject) object.get("Prize");
                            System.out.println("1st Prize: " + prize.get("1st Prize"));
                            System.out.println("2nd Prize: " + prize.get("2nd Prize"));
                            System.out.println("3rd Prize: " + prize.get("3rd Prize"));
                            System.out.println("4th Prize: " + prize.get("4th Prize"));
                            System.out.println("Press 1 go back; Press 2 to Main Menu");
                            int butt = Main.inputButton(2, "Wrong number");
                            if (butt == 2) break mainMenu;
                        } else break;
                    }
                } else if (button == 3) break;
            }
        } catch (ParseException | IOException e) {
            System.out.println("Exception: " + e);
        }
    }

}
