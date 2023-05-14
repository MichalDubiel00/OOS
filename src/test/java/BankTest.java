import probeklausur.BankException;
import probeklausur.Bankaccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankTest {
    @Test
    void name() {

    }

    public static Bankaccount Account_1;
   public static Bankaccount Account_2;
@BeforeEach
        public void setup(){
     Account_2 = new Bankaccount("acc_2");
     Account_1 = new Bankaccount("acc_1");
}

@RepeatedTest(10)
@DisplayName("Transfer test")
    public void transfertest() throws BankException {
    Account_1.deposit(100);
    Account_1.transfer(Account_2,55.5f);
    assertTrue(Account_2.getBalance()==55.5f);
    assertTrue(Account_1.getBalance()==44.5f);

    }
}
