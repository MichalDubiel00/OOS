package bank;

import bank.exceptions.SetAmountUnderNullException;
import bank.exceptions.TransactionAttributeException;

public class IncomingTransfer extends Transfer{
    public IncomingTransfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    public IncomingTransfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }

    public IncomingTransfer(Transfer transfer) throws TransactionAttributeException {
        super(transfer);
    }

    @Override
    public double calculate() {
        return super.calculate();
    }
}
