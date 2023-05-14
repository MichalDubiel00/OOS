package probeklausur;

public class Bankaccount{
    private float balance;
    private String name;
    public Bankaccount(String pName){
        name = pName;
        balance = 0;
    }
    public void withdraw(float amount){
        balance -= amount;
    }
    public void deposit(float amount){
        balance += amount;
    }

    public float getBalance(){return balance;}

    public void transfer(Bankaccount other,float amount) throws BankException{
        if (amount > this.balance)
            throw new BankException("Nicht genügend deckung");
        this.withdraw(amount);
        other.deposit(amount);
    }
}
