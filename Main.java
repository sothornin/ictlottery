import java.util.Random;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner1 = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
        ICTLOTTERYBANNER();
        while (true) {
            Prize.setCycleDate();
            Manager.writeWinnerFile();
            Manager.writeCurrentWinnerFile();
            System.out.println(" ++++++++++++");
            System.out.println("|  User Type |" +
                    "\n| 1. Manger  |" +
                    "\n| 2. Buyer   |" +
                    "\n ++++++++++++" +
                    "\n\nPress 3 to exit");
            int button1 = inputButton(3, "Wrong number");
            if (button1 == 2) {
                String id = inputID();
                while (true) {
                    Buyer b = createBuyer(id);
                    System.out.println("=====================================");
                    System.out.println("Current User: " + id);
                    System.out.println("Next cycle date: " + Prize.getCycleDate()[2][0] + "/" + Prize.getCycleDate()[2][1] + "/" + Prize.getCycleDate()[2][2]);
                    System.out.println("1. Buy lottery\n" +
                            "2. Show Last Cycle Prize Number\n" +
                            "3. Show Your Lottery Number\n" +
                            "4. Show Lottery Detail\n" +
                            "5. Check Winning Last Cycle\n" +
                            "6. Show History\n" +
                            "7. Exit");
                    int button = inputButton(7, "Wrong number");
                    if (button == 1) {

                        System.out.println("1. Buy Random Number\n2. Buy Your Own Number\n3. Back");
                        button = inputButton(3, "Wrong number");
                        if (button == 1) {
                            //1. Buy Random Number
                            while (true) {
                                int number = getRandomNumberInts();
                                System.out.printf("Your random lottery number is %03d\n", number);
                                System.out.println("Press 1 \"Try again\"; Press 2 \"Buy the number\"; Press 3 to \"exit\"");

                                button = inputButton(3, "Wrong number");
                                if (button == 2) {
                                    // Buying process
                                    b.buyLotteryNumber(number);
                                    break;
                                } else if (button == 3) break;
                            }
                        } else if (button == 2) {
                            //2. Buy Your Own Number
                            int number = Main.inputNumber();
                            b.buyLotteryNumber(number);
                            //b.lotteryNumberData(number);
                        }


                    } else if (button == 2) {
                        b.olderPrize();
                    } else if (button == 3) {
                        //3. Show Buyed Lottery number
                        b.showLotteryNumber();
                    } else if (button == 4) {
                        b.showDescription();
                    } else if (button == 5) {
                        b.checkWinning();
                    } else if (button == 6) {
                        b.showHistory();
                    } else break;
                }

            } else if (button1 == 1) {
                ////Manager
                Manager mm = new Manager();
                String pass;
                System.out.print("Enter Password: "); //default pass = "1234"
                pass = scanner1.nextLine();
                if (!mm.getPassword().equals(pass)) {
                    System.out.println("Wrong Passoword!!!!!");
                    continue;
                }
                while (true) {
                    Manager m = new Manager();
                    System.out.println("=====================================");
                    System.out.println("Current User: MANAGER");
                    System.out.println("Next cycle date: " + Prize.getCycleDate()[2][0] + "/" + Prize.getCycleDate()[2][1] + "/" + Prize.getCycleDate()[2][2]);
                    System.out.println("1. Set new cycle\n" +
                            "2. Show Prize\n" +
                            "3. Show Lottery Detail\n" +
                            "4. Check Buyer Number\n" +
                            "5. Check Transaction\n" +
                            "6. Check Winner\n" +
                            "7. Check Buyer History\n" +
                            "8. Change Password\n" +
                            "9. Exit");
                    int button = inputButton(9, "Wrong number");
                    if (button == 1) {
                        String answer;
                        do {
                            System.out.print("Are you sure you want to set a new cycle?(y/n)");
                            answer = input.nextLine();
                            if (answer.equalsIgnoreCase("y")) {
                                m.setNewCycle();
                                break;
                            } else if (answer.equalsIgnoreCase("n")) {
                                System.out.println("Exit Now!!");
                            }
                        } while (!answer.equalsIgnoreCase("n"));

                    } else if (button == 2) {
                        m.olderPrize();
                    } else if (button == 3) m.showDescription();
                    else if (button == 4) m.showBuyerNumber();
                    else if (button == 5) m.checkTransaction();
                    else if (button == 6) m.showWinner();
                    else if (button == 7) m.showHistory();
                    else if (button == 8) m.changePassword();
                    else break;
                }
            } else break;
        }
    }
    // END OF MAIN

    private static String inputID() {
        System.out.print("Enter Your Student ID (Last 3 digits): ");
        int ID = Main.inputButton(999, "From 000 to 999");
        return String.format("%03d", ID);
    }

    private static Buyer createBuyer(String id) {
        Buyer b = new Buyer();
        try {

            FileReader reader = new FileReader("id.json");
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(reader);
            JSONArray array = (JSONArray) json;
            int i;
            for (i = 0; i < array.size(); i++) {
                if (id.equals(array.get(i))) {
                    //Have
                    b = new Buyer(id, 2);
                    break;
                }
            }
            if (i == array.size()) {
                //Doesn't have yet
                b = new Buyer(id, 1);
            }
        } catch (ParseException | IOException e) {

            System.err.println("Cannot Read file");
        }
        return b;
    }

    private static int inputNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your lottery number(4 digits): ");
        while (true) {
            int number;
            try {
                number = scanner.nextInt();
                while (number >= 10000) {
                    System.out.print("Enter again: ");
                    number = scanner.nextInt();
                }
                return number;
            } catch (Exception e) {
                System.out.print("Enter again: ");
                scanner.next();
            }
        }

    }

    static int inputButton(int n, String message) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int number;
            try {
                number = scanner.nextInt();
                while (number > n || number <= 0) {
                    System.out.println(message);
                    System.out.print("Enter again: ");
                    number = scanner.nextInt();
                }
                return number;
            } catch (Exception e) {
                System.out.println("Only accept number");
                System.out.print("Enter again: ");
                scanner.next();
            }
        }

    }

    static int getRandomNumberInts() {
        int min = 1;
        int max = 9999;
        Random random = new Random();
        if (random.ints(min, (max + 1)).findFirst().isPresent())
            return random.ints(min, (max + 1)).findFirst().getAsInt();
        else return 0;
    }

    static int getRandomNumber2Ints() {
        int min = 1;
        int max = 99;
        Random random = new Random();
        if (random.ints(min, (max + 1)).findFirst().isPresent())
            return random.ints(min, (max + 1)).findFirst().getAsInt();
        else return 0;
    }

    static void enterKey() {
        Scanner scanner = new Scanner(System.in);
        String readString = scanner.nextLine();
        System.out.print(readString);
    }

    private static void ICTLOTTERYBANNER(){
        System.out.println("  =============================================================================");
        System.out.println("|                              OOP Project-based App                             |");
        System.out.println("|     _____ _____ _______   _      ____ _______ _______ ______ _______     __    |");
        System.out.println("|    |_   _/ ____|__   __| | |    / __ \\__   __|__   __|  ____|  __ \\ \\   / /    |");
        System.out.println("|      | || |       | |    | |   | |  | | | |     | |  | |__  | |__) \\ \\_/ /     |");
        System.out.println("|      | || |       | |    | |   | |  | | | |     | |  |  __| |  _  / \\   /      |");
        System.out.println("|     _| || |____   | |    | |___| |__| | | |     | |  | |____| | \\ \\  | |       |");
        System.out.println("|    |_____\\_____|  |_|    |______\\____/  |_|     |_|  |______|_|  \\_\\ |_|       |");
        System.out.println("|                                  By Dech x Liam                                |");
        System.out.println("  =============================================================================");
    }
}
