package bank.exceptions;

public class AccountDoesNotExistException extends Exception{
    public AccountDoesNotExistException(){
        System.out.println("Account Does Not Exist");
    }
}
