/**
 * 存钱线程
 */
public class AddMoneyThread implements Runnable{
    private Account account;
    private int money;

    public  AddMoneyThread(Account account, int money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        account.deposit(money);
    }
}
