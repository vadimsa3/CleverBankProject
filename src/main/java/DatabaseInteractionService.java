import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DatabaseInteractionService {

    private static final String url = "jdbc:mysql://localhost:3306/clever_bank";
    private static final String user = "root";
    private static final String password = "3507988Asd";

    private static Connection connection;
    private static StringBuilder insertQuery = new StringBuilder();

    // SAX - парсер (Simple API for XML) порционное чтение/запись XML-файлов.
    // реализация SAX-механизма чтения XML-файлов (порционное чтение);
    // конкатенация строк при помощи StringBuilder;
    // загрузка данных в базу при помощи множественных INSERT’ов.
    public static void parseFileXMLbySAX(String fileName) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            parser.parse(new File(fileName), handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // создание БД
    public static Connection getConnectionCreateDB() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);

                // очистка БД //
                connection.createStatement().execute("DROP TABLE IF EXISTS users, banks, accounts, transactions");

                connection.createStatement().execute("CREATE TABLE users(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "name_user TINYTEXT NOT NULL, " +
                        "passport VARCHAR(100) NOT NULL, " +
                        "PRIMARY KEY(id), " +
                        "UNIQUE(passport))"
                );

                connection.createStatement().execute("CREATE TABLE banks(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "bank_name TEXT NOT NULL, " +
                        "PRIMARY KEY(id))"
                );

                connection.createStatement().execute("CREATE TABLE accounts(" +
                                "id INT NOT NULL AUTO_INCREMENT, " +
                                "user_id INT NOT NULL, " +
                                "bank_id INT NOT NULL, " +
                                "open_date DATE NOT NULL, " +
                                "account_number TINYTEXT NOT NULL, " +
                                "amount INT NOT NULL, " +
                                "PRIMARY KEY(id))"
//                        "PRIMARY KEY(user_id, bank_id), " +
//                        "FOREIGN KEY(user_id) REFERENCES users(id)," +
//                        "FOREIGN KEY(bank_id) REFERENCES banks(id))"
//                        "UNIQUE(account_number))"
                );

                connection.createStatement().execute("CREATE TABLE transactions(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "account_id INT NOT NULL, " +
                        "on_account INT NOT NULL, " +
                        "off_account INT NOT NULL, " +
                        "transaction INT NOT NULL, " +
                        "transaction_date DATE NOT NULL, " +
                        "PRIMARY KEY(id))"
                );

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // наполнение БД Users - пользователей
    public static void fillingUsersDB() throws SQLException {
        getConnectionCreateDB().createStatement().execute(
                "INSERT INTO users(name_user, passport)" +
                        "VALUES" + insertQuery.toString());
    }

    // наполнение БД Banks - банками
    public static void fillingBanksDB() throws SQLException {
        getConnectionCreateDB().createStatement().execute(
                "INSERT INTO banks(bank_name) VALUES " +
                        "('CLEVERBANK'), ('TRUSTBANK'), ('ALFABANK'), ('VTBBANK'), ('GAZPROMBANK')");
    }

    // наполнение БД Accounts - данными
    public static void fillingAccountsDB() throws SQLException {
        for (int i = 1; i <= 45; i++) {
            int idBank = (int) (Math.random() * (6 - 1) + 1);
            getConnectionCreateDB().createStatement().execute(
                    "INSERT INTO accounts(user_id, bank_id, open_date, account_number, amount)" +
                            " VALUES " +
                            "( " + i + ", " + idBank + ", "
                            + "'" + (randomDate()) + "', " + randomAccountNumber() + ", " + randomAmount() + ")"
            );
        }
    }

    // генератор случайного банка из массива банков
    public static Bank randomBank() {
        Bank[] directions = Bank.values();
        return directions[new Random().nextInt(directions.length)];
    }

    // генератор случайного счета в банке
    public static StringBuffer randomAccountNumber() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i <= 9; i++) {
            stringBuffer.append((int) (Math.random() * 10));
        }
        return stringBuffer;
    }

    // генератор первоначальной суммы на счету в банке
    public static Integer randomAmount() {
        return (int) (Math.random() * 100_000 + 10_000);
    }

    // генератор случайной даты открытия счета в банке
    public static LocalDate randomDate() {
        LocalDate start = LocalDate.of(1980, Month.JANUARY, 1);
        LocalDate end = LocalDate.now();
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    // накопление строк через StringBuilder insertQuery, передаем данные из парсера
    public static void passportInfoUsers(String userName, String passport) throws SQLException {
        insertQuery.append((insertQuery.length() == 0 ? "" : ",") +
                "('" + userName + "', '" + passport + "')");
        fillingUsersDB();
        insertQuery = new StringBuilder();
    }

    // получение данных из БД о клиентах
    public static void getAllClients() throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(
                "SELECT name_user, passport FROM users");
        while (resultSet.next()) {
            System.out.printf("Пользователь - %s. Паспорт № - %s.%n",
                    resultSet.getString("name_user"), resultSet.getString("passport"));
        }
        resultSet.close();
    }
}
