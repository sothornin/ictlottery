import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@SuppressWarnings("unchecked")
class Manager extends User {

    private String password;

    Manager() {
        setPassword();
    }

    String getPassword() {
        return password;
    }

    void changePassword() {
        String pass;
        while (true) {
            System.out.print("Enter New Password: ");
            Scanner s = new Scanner(System.in);
            pass = s.nextLine();
            if (this.getPassword().equals(pass)) {
                System.out.println("Same as old password");
            } else break;
        }
        try {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(new FileReader("password.json"));
            JSONObject passwordObject = (JSONObject) json;
            passwordObject.put("password", pass);
            FileWriter file = new FileWriter("password.json");
            file.write(passwordObject.toJSONString());
            file.flush();
            file.close();


        } catch (ParseException | IOException e) {
            System.out.println(e);
        }
    }

    private void setPassword() {

        try {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(new FileReader("password.json"));
            JSONObject passwordObject = (JSONObject) json;
            setPassword((String) passwordObject.get("password"));

        } catch (ParseException | IOException e) {
            System.out.println(e);
        }

    }

    private void setPassword(String password) {
        this.password = password;
    }

    void showBuyerNumber() {
        try {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(new FileReader("number.json"));
            JSONArray numberArray = (JSONArray) json;

            System.out.println("Cycle: " + Prize.cycleDate[2][0] + "/" + Prize.cycleDate[2][1] + "/" + Prize.cycleDate[2][2]);
            JSONObject object = (JSONObject) numberArray.get(2);
            for (Object o : object.keySet()) {
                JSONArray test = (JSONArray) object.get(o);
                if (test.size() != 0) {
                    String number = object.get(o).toString();
                    System.out.println(o + " : " + number.substring(1, number.length() - 1));
                }
            }
            System.out.println("Press Enter to Exit");
            Main.enterKey();
        } catch (ParseException | IOException e) {
            System.out.println(e);
        }
    }

    void checkTransaction() {
        try {
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
            while (true) {
                for (int i = 0; i < size; i++) {
                    objectt = (JSONObject) array.get(i);
                    JSONObject date = (JSONObject) objectt.get("Date");

                    System.out.println(i + 1 + ". " + date.get("month") + "/" + date.get("year"));
                }
                System.out.println(size + 1 + ". back");

                button = Main.inputButton(array.size() + 1, "Wrong number");
                JSONObject object = (JSONObject) array.get(button - 1);
                JSONObject prize = (JSONObject) object.get("Prize");
                objectt = (JSONObject) array.get(button - 1);
                JSONObject date = (JSONObject) objectt.get("Date");
                System.out.println("=====================================");
                System.out.println("Transaction For The Month: " + date.get("month") + "/" + date.get("year"));
                int totalWin = 0;
                int totalCounter = 0;
                for (Object j : prize.keySet()) {
                    JSONObject numberObject = (JSONObject) prize.get(j);
                    for (Object k : numberObject.keySet()) {
                        JSONObject number = (JSONObject) numberObject.get(k);
                        int win = 0;
                        int counter = 0;
                        for (Object l : number.keySet()) {
                            //System.out.print(l + ": ");
                            JSONArray n = (JSONArray) number.get(l);
                            counter++;
                            for (Object o : n) {
                                if (o.equals("1st")) {
                                    win = win + f.getPrizeMoney();
                                }
                                if (o.equals("2nd")) {
                                    win = win + s.getPrizeMoney();
                                }
                                if (o.equals("3rd")) {
                                    win = win + t.getPrizeMoney();
                                }
                                if (o.equals("4th")) {
                                    win = win + ff.getPrizeMoney();
                                }
                            }
                        }
                        totalWin = totalWin + win;
                        totalCounter = totalCounter + counter;

                    }

                }
                System.out.println("Total buyer winning Money: " + totalWin + " Baht");
                System.out.println("Total sale money: " + totalCounter * 20 + " Baht");
                System.out.println("=====================================");
                System.out.println();
                System.out.println("Press 1 go back; Press 2 to Main Menu");
                int butt = Main.inputButton(2, "Wrong number");
                if (butt == 2) break;
            }
        } catch (Exception ignored) {

        }
    }

    void setNewCycle() {
        try {
            writeWinnerFile();
            writeCurrentWinnerFile();
            Prize.setCycleDate();
            FileReader reader = new FileReader("prize.json");
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(reader);
            JSONArray array = (JSONArray) json;
            Object json2 = jsonParser.parse(new FileReader("old.json"));
            JSONArray oldArray = (JSONArray) json2;
            JSONObject old = (JSONObject) array.get(0);
            oldArray.add(old);
            FileWriter oldFile = new FileWriter("old.json");
            oldFile.write(oldArray.toJSONString());
            oldFile.flush();
            oldFile.close();


            array.remove(0);
            FileWriter file = new FileWriter("prize.json");


            JSONObject newObject = (JSONObject) array.get(1);

            JSONObject prizeObject = new JSONObject();
            //Random 1st prize
            JSONArray first = new JSONArray();
            first.add(String.format("%04d", Main.getRandomNumberInts()));
            prizeObject.put("1st Prize", first);

            //Random 2nd prize
            JSONArray second = new JSONArray();
            JSONArray third = new JSONArray();
            for (int i = 0; i < 5; i++) {
                second.add(String.format("%04d", Main.getRandomNumberInts()));
                third.add(String.format("%04d", Main.getRandomNumberInts()));
            }
            prizeObject.put("2nd Prize", second);
            prizeObject.put("3rd Prize", third);

            //Random 4th
            JSONArray fourth = new JSONArray();
            fourth.add(String.format("%02d", Main.getRandomNumber2Ints()));
            prizeObject.put("4th Prize", fourth);

            newObject.put("Prize", prizeObject);
            //Add New DATE

            JSONObject newCycle = new JSONObject();
            String day;
            String month;
            String year;
            if (Prize.cycleDate[0][0].equals("01")) day = "16";
            else day = "01";
            if (Prize.cycleDate[1][1].equals("12")) {
                month = "01";
                year = String.format("%02d", Integer.parseInt(Prize.cycleDate[1][2]) + 1);
            } else {
                month = String.format("%02d", Integer.parseInt(Prize.cycleDate[1][1]) + 1);
                year = Prize.cycleDate[1][2];
            }
            JSONObject dateData = new JSONObject();
            dateData.put("day", day);
            dateData.put("month", month);
            dateData.put("year", year);
            newCycle.put("Date", dateData);
            array.add(newCycle);

            file.write(array.toJSONString());
            Object json1 = jsonParser.parse(new FileReader("number.json"));
            JSONArray numberArray = (JSONArray) json1;
            numberArray.remove(0);
            numberArray.add(new JSONObject());
            FileWriter numberFile = new FileWriter("number.json");
            numberFile.write(numberArray.toJSONString());
            numberFile.flush();
            numberFile.close();
            JSONArray id = new JSONArray();
            FileWriter idFile = new FileWriter("id.json");
            idFile.write(id.toJSONString());
            idFile.flush();
            idFile.close();
            file.flush();
            file.close();
            Prize.setCycleDate();
            newWinnerFileEntry();

        } catch (ParseException | IOException e) {
            System.err.println("Cannot Read file");
            e.printStackTrace();
        }

    }

    void showWinner() {
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
            for (int k = 0; k < allPrize.length; k++) {
                all[k] = allPrize[k].getNumber();
            }
            JSONObject lastObject = (JSONObject) array.get(1);
            String[] last = new String[lastObject.keySet().size()];
            int k = 0;
            for (Object j : lastObject.keySet()
            ) {
                last[k] = (String) j;
                k++;
            }
            for (Object x : last) {
                System.out.println("User: " + x);
                JSONArray test = (JSONArray) lastObject.get(x);
                for (Object y : test) {
                    boolean notWin = true;
                    //System.out.println(y);
                    if (all[0][0].equals(y)) {
                        System.out.println(y + ": Win 1st Prize ");
                        notWin = false;
                    }
                    for (int j = 0; j < all[1].length; j++) {
                        if (all[1][j].equals(y)) {
                            System.out.println(y + ": Win 2nd Prize");
                            notWin = false;
                        }
                    }

                    for (int j = 0; j < all[2].length; j++) {
                        if (all[2][j].equals(y)) {
                            System.out.println(y + ": Win 3rd Prize");
                            notWin = false;
                        }
                    }

                    for (int j = 0; j < all[3].length; j++) {
                        String zz = (String) y;
                        if (all[3][j].equals(zz.substring(zz.length() - 2))) {
                            System.out.println(y + ": Win 4th Prize");
                            notWin = false;
                        }
                    }
                    if (notWin) System.out.println(y + ": not Win");
                }
                System.out.println("Press Enter to continue");
                Main.enterKey();
            }
            System.out.println("Press Enter to exit");
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
                button = Main.inputButton(array.size() + 1, "Wrong number");
                JSONObject object = (JSONObject) array.get(button - 1);
                JSONObject prize = (JSONObject) object.get("Prize");
                objectt = (JSONObject) array.get(button - 1);
                JSONObject date = (JSONObject) objectt.get("Date");
                for (Object j : prize.keySet()) {
                    JSONObject numberObject = (JSONObject) prize.get(j);
                    System.out.println("=====================================");
                    System.out.println(j + "/" + date.get("month") + "/" + date.get("year"));
                    for (Object k : numberObject.keySet()) {
                        System.out.println("User" + ": " + k);
                        JSONObject number = (JSONObject) numberObject.get(k);
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
                    }
                    System.out.println("Press Enter to continue");
                    Main.enterKey();
                }
                System.out.println("Press 1 to go Back; Press 2 to go to Main Menu");
                int b = Main.inputButton(2, "Wrong Number");
                if (b == 2) {
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    static void writeWinnerFile() {
        try {
            FirstPrize f = new FirstPrize(1);
            SecondPrize s = new SecondPrize(1);
            ThirdPrize t = new ThirdPrize(1);
            FourthPrize ff = new FourthPrize(1);
            JSONParser jsonParser = new JSONParser();
            Prize[] allPrize = new Prize[]{f, s, t, ff};
            JSONObject entry = new JSONObject();
            JSONObject date = new JSONObject();
            JSONObject user = new JSONObject();
            Object json = jsonParser.parse(new FileReader("number.json"));
            JSONArray array = (JSONArray) json;
            String[][] all = new String[4][5];
            date.put("month", Prize.cycleDate[1][1]);
            date.put("year", Prize.cycleDate[1][2]);
            for (int k = 0; k < allPrize.length; k++) {
                all[k] = allPrize[k].getNumber();
            }

            JSONObject lastObject = (JSONObject) array.get(1);
            String[] last = new String[lastObject.keySet().size()];
            int k = 0;
            for (Object j : lastObject.keySet()
            ) {
                last[k] = (String) j;
                k++;
            }
            for (Object x : last) {
                JSONObject prize = new JSONObject();
                JSONArray test = (JSONArray) lastObject.get(x);

                for (Object y : test) {
                    JSONArray win = new JSONArray(); //["1st", "2nd"]
                    boolean notWin = true;


                    if (all[0][0].equals(y)) {
                        win.add("1st");
                        notWin = false;
                    }
                    for (int j = 0; j < all[1].length; j++) {
                        if (all[1][j].equals(y)) {
                            win.add("2nd");
                            notWin = false;
                        }
                    }

                    for (int j = 0; j < all[2].length; j++) {
                        if (all[2][j].equals(y)) {
                            win.add("3rd");
                            notWin = false;
                        }
                    }

                    for (int j = 0; j < all[3].length; j++) {
                        String zz = (String) y;
                        if (all[3][j].equals(zz.substring(zz.length() - 2))) {
                            win.add("4th");
                            notWin = false;
                        }
                    }

                    prize.put(y, win);
                    if (notWin) win.add("None");
                }

                user.put(x, prize);

            }
            if (Prize.cycleDate[1][0].equals("01")) {
                Object winner = jsonParser.parse(new FileReader("winner.json"));
                JSONArray winnerArray = (JSONArray) winner;
                JSONObject latest = (JSONObject) winnerArray.get(winnerArray.size() - 1);
                JSONObject number = (JSONObject) latest.get("Prize");
                number.put("01", user);

                FileWriter oldFile = new FileWriter("winner.json");
                oldFile.write(winnerArray.toJSONString());

                oldFile.flush();
                oldFile.close();

            } else {
                Object winner = jsonParser.parse(new FileReader("winner.json"));
                JSONArray winnerArray = (JSONArray) winner;
                if (winnerArray.isEmpty()) {
                    JSONObject cycle = new JSONObject();
                    cycle.put("16", user);
                    entry.put("Date", date);
                    entry.put("Prize", cycle);
                    winnerArray.add(entry);
                    FileWriter oldFile = new FileWriter("winner.json");
                    oldFile.write(winnerArray.toJSONString());
                    oldFile.flush();
                    oldFile.close();
                } else {
                    JSONObject latest = (JSONObject) winnerArray.get(winnerArray.size() - 2);
                    JSONObject number = (JSONObject) latest.get("Prize");
                    number.put("16", user);
                    FileWriter oldFile = new FileWriter("winner.json");
                    oldFile.write(winnerArray.toJSONString());
                    oldFile.flush();
                    oldFile.close();
                }
            }
        } catch (Exception ignored) {

        }

    }

    static void writeCurrentWinnerFile() {

        try {

            JSONParser jsonParser = new JSONParser();
            JSONObject user = new JSONObject();
            Object json = jsonParser.parse(new FileReader("number.json"));
            JSONArray array = (JSONArray) json;
            JSONObject lastObject = (JSONObject) array.get(2);
            String[] last = new String[lastObject.keySet().size()];
            int k = 0;
            for (Object j : lastObject.keySet()
            ) {
                last[k] = (String) j;
                k++;
            }
            for (Object x : last) {
                JSONObject prize = new JSONObject();
                JSONArray test = (JSONArray) lastObject.get(x);

                for (Object y : test) {
                    JSONArray win = new JSONArray();
                    win.add("None");
                    prize.put(y, win);
                }
                user.put(x, prize);
            }
            Object winner = jsonParser.parse(new FileReader("winner.json"));
            JSONArray winnerArray = (JSONArray) winner;
            JSONObject before = (JSONObject) winnerArray.get(winnerArray.size() - 1);
            JSONObject prize = (JSONObject) before.get("Prize");
            if (prize.keySet().size() == 2) {
                prize.put("16", user);

            } else {
                prize.put("01", user);
            }
            FileWriter oldFile = new FileWriter("winner.json");
            oldFile.write(winnerArray.toJSONString());
            oldFile.flush();
            oldFile.close();


        } catch (Exception ignored) {

        }

    }

    private void newWinnerFileEntry() {
        try {
            JSONParser jsonParser = new JSONParser();
            if (Prize.cycleDate[1][0].equals("01")) {

                Object winner = jsonParser.parse(new FileReader("winner.json"));
                JSONArray winnerArray = (JSONArray) winner;
                JSONObject latest = (JSONObject) winnerArray.get(winnerArray.size() - 1);
                JSONObject number = (JSONObject) latest.get("Prize");
                JSONObject next = new JSONObject();
                number.put("16", next);

                FileWriter oldFile = new FileWriter("winner.json");
                oldFile.write(winnerArray.toJSONString());

                oldFile.flush();
                oldFile.close();


            } else {
                JSONObject entry = new JSONObject();
                JSONObject cycle = new JSONObject();
                JSONObject next = new JSONObject();
                cycle.put("01", next);
                JSONObject nextDate = new JSONObject();
                nextDate.put("month", Prize.cycleDate[2][1]);
                nextDate.put("year", Prize.cycleDate[2][2]);
                entry.put("Date", nextDate);
                entry.put("Prize", cycle);
                Object winner1 = jsonParser.parse(new FileReader("winner.json"));
                JSONArray winnerArray1 = (JSONArray) winner1;
                winnerArray1.add(entry);
                FileWriter oldFile = new FileWriter("winner.json");
                oldFile.write(winnerArray1.toJSONString());
                oldFile.flush();
                oldFile.close();


            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}

