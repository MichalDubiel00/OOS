package bank;
import bank.exceptions.*;


/** Represents bank Transfer
 * @author Michal Dubiel 3532504
 *
 */
public class Transfer extends Transaction{
     private String sender;
    private String recipient;

    /**
     * sets Amount
     * @param newAmount new Amount
     */
    public void setAmount(double newAmount) throws TransactionAttributeException{
        if (newAmount>=0)this.amount=newAmount;
        else throw new TransactionAttributeException();
    }

    /**
     * gets Sender
     * @return A String with Sender Name
     */
    public String getSender(){return sender;}

    /**
     * sets Sender
     * @param newSender new Sender
     */
    public void setSender(String newSender){this.sender=newSender;}

    /**
     * gets Recipient
     * @return A String with Recipient Name
     */
    public String getRecipient(){return recipient;}

    /**
     * sets Recipient
     * @param newRecipient new Recipient
     */
    public void setRecipient(String newRecipient){this.recipient=newRecipient;}

    /**
     * Transfer Constructor for Date,Amount and Description Attributes
     * @param date Date
     * @param amount Amount
     * @param description Description
     */
    public Transfer(String date,double amount,String description) throws TransactionAttributeException {
        setDate(date);
        try {
            setAmount(amount);
        }
        catch (TransactionAttributeException e)
        {
            System.out.println("Amount under 0");
        }
        setDescription(description);
    }

    /**
     * Constructor for all Attributes.
     * @param date Date
     * @param amount Amount
     * @param description Description
     * @param sender Sender
     * @param recipient Recipient
     */

    public Transfer(String date,double amount,String description,String sender,String recipient) throws TransactionAttributeException {
        this(date,amount,description);
        setSender(sender);
        setRecipient(recipient);

    }

    /**
     * Copy constructor
     * @param transfer Object transfer
     */
    public Transfer(Transfer transfer) throws TransactionAttributeException {
        this(transfer.date, transfer.amount,transfer.description,transfer.sender,transfer.recipient);
    }

    /**
     * calculates Amount
     * @return Amount
     */
    @Override
    public double calculate() {
        return  amount;
    }

    /**
     * creates a String of objects
     * @return String of all objects in a given class
     */
    @Override
    public String toString() {
        return super.toString() + " Sender:" + getSender() + " Recipient:" + getRecipient();
    }

    /**
     * compares two objects
     * @param obj
     * @return boolean true if objects are equels
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (this == null)
            return false;
        if (obj instanceof Transfer)
        {
             Transfer other = (Transfer)obj;
             return super.equals(other) && other.getSender().equals(other.getSender())
                     && this.getRecipient().equals(other.getRecipient());
        }
            return false;
    }


}

