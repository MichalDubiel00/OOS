package bank;
import bank.exceptions.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class PrivateBank implements Bank {
    public String name;
    private double incomingInterest;
    private double outgoingInterest;
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<String, List<Transaction>>();

    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String directoryName) {
        this.path = directoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIncomingInterest() {
        return incomingInterest;
    }

    public void setIncomingInterest(double incomingInterest) {
        if (incomingInterest >= 0 && incomingInterest <= 1)
            this.incomingInterest = incomingInterest;
    }

    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    public void setOutgoingInterest(double outgoingInterest) {
        if (outgoingInterest >= 0 && outgoingInterest <= 1)
            this.outgoingInterest = outgoingInterest;
    }
    /**
     * Constructor
     * @param name Name of the bank.
     * @param incomingInterest Interest rate for incoming transactions (positive value in percent, range 0 to 1).
     * @param outgoingInterest Interest rate for outgoing transactions (positive value in percent, range 0 to 1).
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest){
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        setPath("data"+"/"+name);

        try{
            Path p = Paths.get(path);
            if(Files.notExists(p)){
                Files.createDirectories(p);
            }
            else{
                readAccounts();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Constructor if full path is to be set
     @param name Name of the bank.
      * @param incomingInterest Interest rate for incoming transactions (positive value in percent, range 0 to 1).
     * @param outgoingInterest Interest rate for outgoing transactions (positive value in percent, range 0 to 1).
     * @param path Path to store Bank data in
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String path){
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        setPath(path);

        try{
            Path p = Paths.get(path);
            if(Files.notExists(p)){
                Files.createDirectories(p);
            }
            else{
                readAccounts();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public PrivateBank(PrivateBank pb){
        this(pb.getName(), pb.getIncomingInterest(), pb.getOutgoingInterest(), pb.getPath());
        //this.accountsToTransactions = pb.accountsToTransactions;      //Nur die ersten drei Attribute gefordert
    }


    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException(account);
        else accountsToTransactions.put(account,new ArrayList<Transaction>());
        try{
            writeAccount(account);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        createAccount(account);
        for (Transaction transaction:transactions)
            try {
                addTransaction(account,transaction);
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        try{
            writeAccount(account);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException();
        if (containsTransaction(account,transaction))
            throw new TransactionAlreadyExistException();
        if (transaction instanceof Payment) {
            try {
                ((Payment) transaction).setIncomingInterest(this.incomingInterest);
            } catch (TransactionAttributeException e) {
                throw new TransactionAttributeException();
            }
            try {
                ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
            } catch (TransactionAttributeException e) {
                throw new TransactionAttributeException();
            }
        }
        accountsToTransactions.get(account).add(transaction);
        try{
            writeAccount(account);
        }catch(IOException e){
            e.printStackTrace();
        }
                
    }

    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException();
        if (!containsTransaction(account,transaction))
            throw new TransactionDoesNotExistException();
        List<Transaction> list = getTransactions(account);
        list.remove(transaction);
        try{
            writeAccount(account);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        if(transaction instanceof Payment){
            try {
                ((Payment) transaction).setIncomingInterest(this.incomingInterest);
                ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
            }catch(TransactionAttributeException e){
                System.out.println(e);
            }
        }
        return accountsToTransactions.get(account).contains(transaction);
    }

    @Override
    public double getAccountBalance(String account) {
        double amount = 0;
        if (accountsToTransactions.containsKey(account)) {
            List<Transaction> list = accountsToTransactions.get(account);
            for (Transaction transaction : list) {
                amount += transaction.calculate();
            }
        }
        return amount;
    }

    @Override
    public List<Transaction> getTransactions(String account){
        if (accountsToTransactions.containsKey(account))
            return accountsToTransactions.get(account);
        return null;
    }


    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> tmp = new ArrayList(accountsToTransactions.get(account));
        if(asc){
            tmp.sort(Comparator.comparingDouble(Transaction::calculate));
        }
        if(!asc){
            tmp.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
        }
        return tmp;
    }

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> list = getTransactions(account);
        List<Transaction> newList = new ArrayList<Transaction>();
        if (list != null) {
            if (positive) {
                for (Transaction transaction : list) {
                    if (transaction.calculate() > 0)
                        newList.add(transaction);
                }
                return newList;
            }
            if (!positive) {
                for (Transaction transaction : list) {
                    if (transaction.calculate() < 0)
                        newList.add(transaction);
                }
                return newList;

            }
        }
        return null;
    }

    @Override
    public String toString() {
        String str_1 = getName() + " " + getIncomingInterest() + " " + getOutgoingInterest() + "\n";
        String str_2 = "";
        for (String key : this.accountsToTransactions.keySet()) {
            List<Transaction> list = getTransactions(key);
            str_1 += key + ":\n";
            for (Transaction transaction : list) {
                str_2 += transaction.toString() + "\n";
            }
            str_1 += str_2;
            str_1 += "Balance:" +  getAccountBalance(key);

        }

        return str_1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        PrivateBank other = (PrivateBank) obj;
            return other.getName().equals(this.getName()) && other.getOutgoingInterest() == this.getOutgoingInterest() && other.getIncomingInterest() == this.getIncomingInterest()
                    && other.getPath().equals(this.getPath());
    }

    /**
     * reads JSON files and writes it to Object
     * @throws IOException
     */
    public void readAccounts() throws IOException{
        File directory = new File(this.path);
        File[] files = Objects.requireNonNull(directory.listFiles());
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Transaction.class, new Serializer()).create();
        for (File file: files) {

            String tmp = file.getName().replace(".json","");
            String tmp2 = tmp.replace("Konto ", "");
            String accountName = tmp2.replace("konto_ ", "");

            if(!accountsToTransactions.containsKey(accountName))
                accountsToTransactions.put(accountName, Collections.emptyList());

            System.out.println(accountName);


            try{
                Reader reader = new FileReader(this.path+"/"+file.getName());
                List<Transaction> TransactionList=gson.fromJson(reader,new TypeToken<List<Transaction>>(){}.getType());
                for(Transaction transaction : TransactionList ){
                    List<Transaction> tList = new ArrayList<>(accountsToTransactions.get(accountName));
                    tList.add(transaction);
                    accountsToTransactions.put(accountName, tList);
                }
                reader.close();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * writes Account to Json with Custom Serializer
     * @param account Bank account
     * @throws IOException
     */
    public void writeAccount(String account) throws IOException{
        FileWriter writer = new FileWriter(this.path+"/Konto "+account+".json");
        List<Transaction>accountliste = accountsToTransactions.get(account);
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Transaction.class, new Serializer()).setPrettyPrinting().create();
        writer.write(gson.toJson(accountliste));
        writer.close();
    }

    /**
     *deletes Account
     * @param account
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException{
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException();
        }

        accountsToTransactions.remove(account);
        File delAccount = new File(this.path+"/Konto "+account+".json");

        if(!delAccount.delete()){
            throw new IOException("Error deleting account's persistence sample.");
        }
    }

    public List<String> getAllAccounts(){
        List<String> accounts = new ArrayList<>();
        for (String key : accountsToTransactions.keySet()){
            accounts.add(key);
        }
        return accounts;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

