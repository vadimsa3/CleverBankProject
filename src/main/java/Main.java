import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static CopyOnWriteArrayList<BankAccount> bankAccounts = new CopyOnWriteArrayList<>();
    private static Transaction transaction = new Transaction();
    private static final String fileNameDB = "resources/clientsBase.xml";

    public static void main(String[] args) throws SQLException {

        DatabaseInteractionService.parseFileXMLbySAX(fileNameDB); // запуск парсинга базы клиентов
        DatabaseInteractionService.getConnectionCreateDB(); // создание БД клиентов
        DatabaseInteractionService.fillingBanksDB();
        DatabaseInteractionService.fillingAccountsDB();

        while (true) {

            System.out.println("\n" + "Перечень команд :" + "\n" +
                    "1 - Получение данных о зарегистрированных пользователях." + "\n" +
                    "2 - Получение данных о балансе счетов клиентов." + "\n" +
                    "3 - Запуск процесса эмуляции перевода средств." + "\n" +
                    "4 - Выход.");
            System.out.println("Введите номер команды: ");
            Scanner scanner = new Scanner(System.in);

            int number = scanner.nextInt();
            switch (number) {
                case 1 -> DatabaseInteractionService.getAllClients();

//                case 2 -> getBankClients(bankAccounts);
//                case 3 -> startTransaction();
                case 4 -> {
                    System.out.println("Завершение работы.");
                    System.exit(0);
                }
                default -> throw new IllegalArgumentException("Необходимо выбрать команду из списка.");
            }
        }
    }
}

//
//    // получение списка клиентов банка
//    private static void getBankClients(CopyOnWriteArrayList<BankAccount> bankAccounts) {
//        bankAccounts.forEach(bankAccount -> {
//            System.out.printf("Счет № %s.%n" +
//                            "Баланс - %d руб.%n" +
//                            "Клиент - %s.%n" +
//                            "Номер паспорта - %s.%n" +
//                            "Дата открытия счета - %s.%n" +
//                            "Банк - %s.%n",
//                    bankAccount.getAccountNumber(),
//                    bankAccount.getBalance(),
//                    bankAccount.getClient().getUserSurname() + " " + bankAccount.getClient().getUserName(),
//                    bankAccount.getClient().getPassport(),
//                    bankAccount.getOpenDate(),
//                    bankAccount.getClient().getBank() + "\n");
//        });
//    }
//
//    // запуск переводов средств между клиентами
//    private static void startTransaction() {
//        for (int j = 0; j < 20; j++) {
//            new Thread(() -> {
//                BankAccount from = bankAccounts.get((int) (Math.random() * 10));
//                BankAccount to = bankAccounts.get((int) (Math.random() * 10));
//                long amount = (long) (Math.random() * 60_000 + 1_000);
//                try {
//                    transaction.transfer(from, to, amount);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }).start();
//        }
//        System.out.println("After: " + transaction.getTotalBalanceAllAccounts(bankAccounts));
//    }
//}

