package bank;
import bank.exceptions.*;

/** Abstract class represents bank Transactions
 * @author Michal Dubiel
 */
public abstract class Transaction implements CalculateBill {
    String date;
    double amount;
    String description;


    /**
     * gets Date
     * @return Date
     */
    public String getDate(){return date;}

    /**
     * sets Date
     * @param newDate Date
     */
    public void setDate(String newDate){this.date=newDate;}

    /**
     * gets Description
     * @return description
     */
    public String getDescription(){return description;}

    /**
     * sets Description
     * @param newDescription description
     */
    public void setDescription(String newDescription){this.description=newDescription;}

    /**
     * gets Amount
     * @return Amount
     */
    public double getAmount(){return amount;}

    /**
     * sets Amount
     * @param newAmount Amount
     */
    abstract void setAmount(double newAmount) throws TransactionAttributeException;


    /**
     * creates a String of objects
     * @return String of all objects in a given class
     */
    @Override
    public String toString() {

        return "Date:"+getDate() +" Description:"+getDescription() +" Amount:" + calculate();
    }
    /**
     * compares two objects
     * @param obj
     * @return boolean true if objects are equels
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof Transaction)) return false;
        Transaction t = (Transaction) obj;
        return t.getAmount() == this.getAmount() && this.getDescription().equals(t.getDescription()) && this.getDate().equals(t.getDate());
    }
}
