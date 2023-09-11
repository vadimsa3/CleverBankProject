import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Transaction {

    private static CopyOnWriteArrayList<BankAccount> bankAccounts = new CopyOnWriteArrayList<>();
    private final Random random = new Random();

//    public synchronized boolean isFraud(Account fromAccount, Account toAccount, long amount)
//            throws InterruptedException {
//        System.out.println(Thread.currentThread() + "Перевод " +  amount + " руб."+ " превышает 50 000 руб. " +
//                "Счета " + fromAccount.getAccNumber() + " и " + toAccount.getAccNumber() +
//                " направлены на проверку Службе Безопасности.");
//        Thread.sleep(1000);
//        return random.nextBoolean(); // статус блокировки
//    }

    public void transfer(BankAccount fromAccount, BankAccount toAccount, long amountToTransfer) throws Exception {

//        // проверка суммы транзакции
//        if (amountToTransfer > 50_000) {
//            boolean accesseAccount = false;
//            synchronized (this) {
//                try {
//                    if (fromAccount.isBlocked() && toAccount.isBlocked()) {
//                        wait();
//                    }
//                    accesseAccount = isFraud(fromAccount, toAccount, amountToTransfer);
//                    notify();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (accesseAccount) {
//                fromAccount.setBlocked(true); // меняем статус блокировка аккаунта при получении true от СБ
//                toAccount.setBlocked(true);   // меняем статус блокировка аккаунта при получении true от СБ
//                throw new Exception("Внимание!!! Перевод невозможен. " +
//                        "Счета " + fromAccount.getAccNumber() +
//                        " и " + toAccount.getAccNumber() + " заблокированы.");
//            }
//        } else {

        // проверка баланса и счетов отправки и получения, перевод средств
        if (!isTranslateYourself(fromAccount, toAccount) && !isSufficientBalance(fromAccount, amountToTransfer)) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    fromAccount.credit(amountToTransfer);
                    toAccount.debit(amountToTransfer);
                    System.out.println(Thread.currentThread() +
                            " Перевод со счета " + fromAccount.getAccountNumber() + " суммы в размере " +
                            amountToTransfer + " руб. на счет " + toAccount.getAccountNumber() + " совершен." +
                            " Текущий баланс счетов: " + fromAccount.getAccountNumber() + " - " +
                            fromAccount.getBalance() + " руб.," + toAccount.getAccountNumber() + " - " +
                            toAccount.getBalance() + " руб.");
                }
            }
        }
    }

    // остаток на счете клиента
    public long getNewBalance(BankAccount accountClient) {
        return accountClient.getBalance();
    }

    // общий объем депозитов в банке
    public long getTotalBalanceAllAccounts(CopyOnWriteArrayList<BankAccount> bankAccounts) {
        long total = 0;
        for (BankAccount bankAccount : bankAccounts) {
            total += bankAccount.getBalance();
        }
        return total;
    }

    // проверка счета на достаточность средств
    public boolean isSufficientBalance(BankAccount fromAccount, long amountToTransfer) throws Exception {
        if (fromAccount.getBalance() < amountToTransfer) {
            throw new Exception("Внимание!!! Для перевода суммы " + amountToTransfer +
                    " руб." + " Недостаточно средств на счете № "
                    + fromAccount.getAccountNumber() + ". Текущий баланс счета равен: "
                    + fromAccount.getBalance() + " руб.");
        }
        return false;
    }

    // проверка номера отправляющего и принимающего счетов
    public boolean isTranslateYourself(BankAccount fromAccount, BankAccount toAccount) throws Exception {
        if (Objects.equals(fromAccount.getAccountNumber(), toAccount.getAccountNumber())) {
            throw new Exception("Внимание!!! Перевод со своего счета № " +
                    fromAccount.getAccountNumber() + " на свой счет " +
                    toAccount.getAccountNumber() + " невозможен.");
        }
        return false;
    }
}


