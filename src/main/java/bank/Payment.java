package bank;

import bank.exceptions.TransactionAttributeException;

/** Represents bank Payment
 * @author Michal Dubiel
 */
public class Payment extends Transaction{

    private double incomingInterest;

    private double outgoingInterest;

    /**
     * sets Amount
     * @param newAmount new Amount
     */

    public void setAmount(double newAmount){this.amount=newAmount;}

    /**
     * gets Incoming Interests
     * @return double of IncomingInterests
     */

    public double getIncomingInterest(){return incomingInterest;}

    /**
     * sets IncomingInterests between 0 and 1
     * @param newIncomingInterest
     */
    public void setIncomingInterest(double newIncomingInterest) throws TransactionAttributeException {
        if(newIncomingInterest >= 0 && newIncomingInterest <= 1)
            this.incomingInterest=newIncomingInterest;
        else throw new TransactionAttributeException();
    }

    /**
     * gets outgoingInterest
     * @return double of outgoingInterest
     */
    public double getOutgoingInterest(){return outgoingInterest;}

    /**
     * sets OutgoingInterests between 0 and 1
     * @param newOutGoingInterest
     */
    public void setOutgoingInterest(double newOutGoingInterest) throws TransactionAttributeException {
        if(newOutGoingInterest >= 0 && newOutGoingInterest <= 1)
            this.outgoingInterest=newOutGoingInterest;
        else throw new TransactionAttributeException();
    }

    /**
     * Constructor for Date,Amount,Description
     * @param date
     * @param amount
     * @param description
     */
    public Payment(String date,double amount,String description)
    {
        setDate(date);
        setAmount(amount);
        setDescription(description);

    }

    /**
     * Constructor for all attributes
     * @param date
     * @param amount
     * @param description
     * @param newIncomingInterest
     * @param newOutGoingInterest
     */
    public Payment(String date,double amount,String description,double newIncomingInterest,double newOutGoingInterest) {
        this(date, amount, description);
        try {
            setIncomingInterest(newIncomingInterest);
        } catch (TransactionAttributeException e) {
            throw new RuntimeException(e);
        }
        try {
            setOutgoingInterest(newOutGoingInterest);
        } catch (TransactionAttributeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * copy constructor
     * @param payment
     */
    public Payment(Payment payment) throws TransactionAttributeException {
        this( payment.getDate(),payment.getAmount(), payment.getDescription(),  payment.getIncomingInterest(),payment.getOutgoingInterest());
    }

    /**
     * calculates amount with Incoming/outgoingInterests
     * @return if double for calculated amount
     */
    @Override
    public double calculate() {
        if (amount >= 0){

            return amount-(amount*incomingInterest);
        }
        else
            return amount+(amount*outgoingInterest);
    }

    /**
     * creates a String of objects
     * @return String of all objects in a given class
     */
    @Override
    public String toString() {
        return super.toString() + " In coming Interest:" + getIncomingInterest() + " Out going Interest:" + getOutgoingInterest() + " Result:"+ calculate();
    }
    /**
     * compares two objects
     * @param obj
     * @return boolean true when objects are equal
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof Payment)) return false;
        Payment p = (Payment) obj;
        return super.equals(obj) && this.getIncomingInterest() == p.getIncomingInterest() && this.getOutgoingInterest() == p.getOutgoingInterest();
    }
}

