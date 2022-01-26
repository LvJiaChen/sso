import java.util.concurrent.atomic.AtomicInteger;

/**
 * 银行账户
 */
public class Account {
    private AtomicInteger balance=new AtomicInteger(0);     // 账户余额

    /**
     * 存款
     * @param money 存入金额
     */
    public void deposit(int money) {
        balance.getAndAdd(money);
        try {
            Thread.sleep(10);   // 执行某个存钱的方法
        }
        catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获得账户余额
     */
    public AtomicInteger getBalance() {
        return balance;
    }
}
