package bank;

import bank.exceptions.SetAmountUnderNullException;
import bank.exceptions.TransactionAttributeException;

public class OutgoingTransfer extends Transfer{
    public OutgoingTransfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }

    public OutgoingTransfer(Transfer transfer) throws TransactionAttributeException {
        super(transfer);
    }

    @Override
    public double calculate() {
        return -super.calculate();
    }
}
