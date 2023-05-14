package probeklausur;

public class BankAccountTest{
    private Bankaccount myAccount = new Bankaccount("XY");
    private Bankaccount targetAccount = new Bankaccount("YX");
    float transfer;

    public static void main(String [] args){
        BankAccountTest test_1 = new BankAccountTest();
        test_1.transfer = 55.5f;

        test_1.myAccount.deposit(100);
        try {
            test_1.myAccount.transfer(test_1.targetAccount, test_1.transfer);

        }catch (BankException e){}

        try {
            test_1.myAccount.transfer(test_1.targetAccount, test_1.transfer);

        }catch (BankException e){}

        System.out.println(
                "Expected myAccount balance 44.5 and target Account 55.5 got myAccount:"+test_1.myAccount.getBalance()
                + " target Account:"+
                test_1.targetAccount.getBalance()
        );
        System.out.println("Expected Bank Exception");

        try {
            test_1.myAccount.transfer(test_1.targetAccount, test_1.transfer);

        }catch (BankException e){System.out.println(e);}

        try {
            test_1.myAccount.transfer(test_1.targetAccount, test_1.transfer);

        }catch (BankException e){}


    }
}
