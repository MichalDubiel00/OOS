import bank.*;
import bank.exceptions.*;
import com.google.gson.*;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {


        PrivateBank pBank = new PrivateBank("Spaßkasse", 0.25, 0.3);

        try {
            pBank.createAccount("Kevin", List.of(
                    new Payment( "24.03.2001",300.1, "Payment", 0.8, 0.5),
                    new Payment( "24.03.2001",405, "Payment", 0.3, 0.6),
                    new Payment( "24.03.2001",231, "Payment", 0.4, 0.5),
                    new OutgoingTransfer( "20.11.2022",20, "Transfer", "Mark", "Kevin"),
                    new Payment( "24.06.2001",-300.1, "Payment", 0.1, 0.2)
            ));
            pBank.createAccount("Christian", List.of(
                    new OutgoingTransfer( "09.01.2015",734.12, "Transfer", "Mark", "Christian"),
                    new Payment( "24.06.2001",-300.1, "Payment", 0.1, 0.2),
                    new IncomingTransfer( "09.01.2015",734.12, "Transfer", "Christian", "Mark"),
                    new Payment( "24.03.2001",308, "Payment", 0.37, 0.23),
                    new IncomingTransfer( "09.01.2015",734.12, "Transfer", "Christian", "Betty")
            ));
        }catch(AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException e){
            System.out.println(e);
        }

        System.out.println(pBank.containsTransaction("Kevin", new Payment( "24.03.2001",300.1, "Payment", 0.8, 0.5)));

        PrivateBank test = new PrivateBank(pBank) ;
        System.out.println(pBank.equals(test));
        try {
            pBank.deleteAccount("Christian");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(pBank);

    }
}