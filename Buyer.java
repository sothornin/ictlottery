import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unchecked")
class Buyer extends User {
    private String id;

    Buyer() {
    }

    Buyer(String id, int i) {
        if (i == 1) {
            setId(id);
            writeNumber();
            writeId();
        } else if (i == 2) {
            setId(id);
        }
    }

    private String getId() {
        return this.id;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void writeNumber() {
        try {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(new FileReader("number.json"));
            JSONArray array = (JSONArray) json;
            JSONObject current = (JSONObject) array.get(2);
            JSONArray numberArray = new JSONArray();
            current.put(this.getId(), numberArray);
            FileWriter file = new FileWriter("number.json");
            file.write(array.toJSONString());
            file.flush();
            file.close();
        } catch (ParseException | IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    private void writeId() {
        try {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(new FileReader("id.json"));
            JSONArray idArray = (JSONArray) json;
            idArray.add(this.getId());
            FileWriter file = new FileWriter("id.json");
            file.write(idArray.toJSONString());
            file.flush();
            file.close();
        } catch (ParseException | IOException e) {
            System.out.println("Exception: " + e);

        }
    }

    void buyLotteryNumber(int number) {
        try {


            String s = String.format("%04d", number);
            System.out.println("Do you really want to buy the number " + s + " ?");
            System.out.println("1. Yes 2. No");
            int button = Main.inputButton(2, "Wrong number");
            if (button == 1) {
                JSONParser jsonParser = new JSONParser();
                Object json = jsonParser.parse(new FileReader("number.json"));
                JSONArray array = (JSONArray) json;
                JSONObject current = (JSONObject) array.get(2);
                JSONArray numberArray = (JSONArray) current.get(this.id);
                numberArray.add(s);
                FileWriter file = new FileWriter("number.json");
                file.write(array.toJSONString());
                file.flush();
                file.close();
                System.out.println("Your just buy number " + s + " costs 20 Bath");
                System.out.println("Press Enter to exit");
                Main.enterKey();
                Manager.writeWinnerFile();
                Manager.writeCurrentWinnerFile();
            }
        } catch (ParseException | IOException e) {
            System.out.println("Exception: " + e);

        }


    }

    void showLotteryNumber() {

        try {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(new FileReader("number.json"));
            JSONArray array = (JSONArray) json;
            System.out.println("Your Lottery number");
            int i = 2;
            JSONObject current = (JSONObject) array.get(i);
            JSONArray numberArray = (JSONArray) current.get(this.id);
            if (numberArray != null) {
                if (numberArray.size() == 0) {
                    System.out.println("Cycle: " + Prize.cycleDate[i][0] + "/" + Prize.cycleDate[i][1] + "/" + Prize.cycleDate[i][2]);
                    System.out.println("No Lottery Number\n");
                } else {
                    System.out.println("Cycle: " + Prize.cycleDate[i][0] + "/" + Prize.cycleDate[i][1] + "/" + Prize.cycleDate[i][2]);
                    for (Object o : numberArray) {
                        System.out.println((String) o);
                    }
                    System.out.println("Total Price: " + 20 * numberArray.size() + " Baht");
                    System.out.println();
                }
            } else {
                System.out.println("Cycle: " + Prize.cycleDate[i][0] + "/" + Prize.cycleDate[i][1] + "/" + Prize.cycleDate[i][2]);
                System.out.println("No Lottery Number\n");
            }
            System.out.println("Press Enter to go to Main Menu");
            Main.enterKey();
        } catch (ParseException | IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    void checkWinning() {
        FirstPrize f = new FirstPrize(1);
        SecondPrize s = new SecondPrize(1);
        ThirdPrize t = new ThirdPrize(1);
        FourthPrize ff = new FourthPrize(1);
        Prize[] allPrize = new Prize[]{f, s, t, ff};
        JSONParser jsonParser = new JSONParser();
        try {
            Object json = jsonParser.parse(new FileReader("number.json"));
            JSONArray array = (JSONArray) json;

            String[][] all = new String[4][5];
            System.out.println("Cycle: " + Prize.cycleDate[1][0] + "/" + Prize.cycleDate[1][1] + "/" + Prize.cycleDate[1][2]);
            int sum = 0;
            for (int k = 0; k < allPrize.length; k++) {
                all[k] = allPrize[k].getNumber();
            }

            JSONObject lastObject = (JSONObject) array.get(1);
            if (!lastObject.isEmpty()) {
                JSONArray test = (JSONArray) lastObject.get(this.getId());
                if (test != null) {
                    for (Object y : test) {
                        boolean notWin = true;
                        System.out.println("\nYour number " + y);
                        System.out.print("Press Enter to check");
                        Main.enterKey();
                        if (all[0][0].equals(y)) {
                            System.out.print("Win 1st Prize ");
                            sum = sum + f.getPrizeMoney();
                            Main.enterKey();
                            notWin = false;
                        }
                        for (int j = 0; j < all[1].length; j++) {
                            if (all[1][j].equals(y)) {
                                System.out.print("Win 2nd Prize");
                                sum = sum + s.getPrizeMoney();
                                Main.enterKey();
                                notWin = false;
                            }
                        }

                        for (int j = 0; j < all[2].length; j++) {
                            if (all[2][j].equals(y)) {
                                System.out.print("Win 3rd Prize");
                                sum = sum + t.getPrizeMoney();
                                Main.enterKey();
                                notWin = false;
                            }
                        }

                        for (int j = 0; j < all[3].length; j++) {
                            String zz = (String) y;
                            if (all[3][j].equals(zz.substring(zz.length() - 2))) {
                                System.out.print("Win 4th Prize");
                                sum = sum + ff.getPrizeMoney();
                                Main.enterKey();

                                notWin = false;
                            }
                        }
                        if (notWin) {
                            System.out.print("No Winning");
                            Main.enterKey();
                        }
                    }
                }
            }
            System.out.println("This is the end");
            Main.enterKey();
            System.out.println("You win " + sum + " Baht");
            System.out.println("Press Enter to go to Main Menu");
            Main.enterKey();
        } catch (ParseException | IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    void showHistory() {
        try {
            while (true) {
                FirstPrize f = new FirstPrize(1);
                SecondPrize s = new SecondPrize(1);
                ThirdPrize t = new ThirdPrize(1);
                FourthPrize ff = new FourthPrize(1);
                int button;
                JSONParser jsonParser = new JSONParser();
                Object json = jsonParser.parse(new FileReader("winner.json"));
                JSONArray array = (JSONArray) json;
                int size = array.size();
                JSONObject objectt;
                for (int i = 0; i < size; i++) {
                    objectt = (JSONObject) array.get(i);
                    JSONObject date = (JSONObject) objectt.get("Date");
                    System.out.println(i + 1 + ". " + date.get("month") + "/" + date.get("year"));
                }
                System.out.println(size + 1 + ". back");
                int monthlyWin = 0;
                int monthlyCounter = 0;
                button = Main.inputButton(array.size() + 1, "Wrong number");
                JSONObject object = (JSONObject) array.get(button - 1);
                JSONObject prize = (JSONObject) object.get("Prize");
                objectt = (JSONObject) array.get(button - 1);
                JSONObject date = (JSONObject) objectt.get("Date");
                int c = 0;
                for (Object j : prize.keySet()) {
                    JSONObject numberObject = (JSONObject) prize.get(j);


                    JSONObject number = (JSONObject) numberObject.get(this.getId());
                    if (number == null) {
                        c++;
                        continue;
                    }
                    System.out.println(j + "/" + date.get("month") + "/" + date.get("year"));
                    System.out.println("User" + ": " + this.getId());
                    int win = 0;
                    int counter = 0;
                    for (Object l : number.keySet()) {
                        System.out.print(l + ": ");
                        JSONArray n = (JSONArray) number.get(l);
                        counter++;
                        for (Object o : n) {
                            if (o.equals("1st")) {
                                System.out.print("1st Prize");
                                win = win + f.getPrizeMoney();
                            }
                            if (o.equals("2nd")) {
                                System.out.print("2nd Prize");
                                win = win + s.getPrizeMoney();
                            }
                            if (o.equals("3rd")) {
                                System.out.print("3rd Prize");
                                win = win + t.getPrizeMoney();
                            }
                            if (o.equals("4th")) {
                                System.out.print("4th Prize");
                                win = win + ff.getPrizeMoney();
                            }
                            if (o.equals("None")) System.out.print("No prize");
                            System.out.println();
                        }
                    }
                    System.out.println();
                    System.out.println("Win Money: " + win + " Baht");
                    System.out.println("Buy money: " + counter * 20 + " Baht");
                    System.out.println();
                    monthlyWin = monthlyWin + win;
                    monthlyCounter = monthlyCounter + counter;
                }
                if (c >= prize.keySet().size()) {
                    System.out.println("No data");
                }
                System.out.println("Monthly Win money: " + monthlyWin);
                System.out.println("Monthly Buy money: " + monthlyCounter * 20);
                System.out.println("Press 1 to go Back; Press 2 to go to Main Menu");
                int b = Main.inputButton(2, "Wrong Number");
                if (b == 2) {
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }
}
