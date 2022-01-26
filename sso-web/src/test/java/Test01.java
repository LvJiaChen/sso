import com.sso.web.application.MainApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  {MainApplication.class})
public class Test01 {

    public static void main(String[] args) {
        Account account=new Account();
        ExecutorService executorService= Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new AddMoneyThread(account,2));
        }
        executorService.shutdown();
        /** 若关闭后所有任务都已完成，则返回true */
        while(!executorService.isTerminated()) {}
        System.out.println("账户余额: " + account.getBalance());
    }

}
