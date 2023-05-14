package ui;

import bank.*;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.*;
import javafx.stage.*;

public class AccountviewController implements Initializable{

    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    private PrivateBank bank;
    @FXML
    public Parent root;
    //Hinzufügen Knopf
    @FXML
    public MenuButton addButton;
    @FXML
    public MenuItem payment;
    @FXML
    public MenuItem transaction;
    //Optionen Knopf
    @FXML
    public MenuButton optionsButton;
    @FXML
    public MenuItem ascending;
    @FXML
    public MenuItem descending;
    @FXML
    public MenuItem positive;
    @FXML
    public MenuItem negative;
    @FXML
    public ListView<Transaction> transactionsListView;
    @FXML
    public Text textAccount;
    @FXML
    public Button backButton;
    private String name;

    /**
     * Updates the ListView of transactions
     * @param listTransaction List of all transactions of given account
     */
    private void updateTransactionList(List<Transaction> listTransaction) {
        transactionsList.clear();
        transactionsList.addAll(listTransaction);
        transactionsListView.setItems(transactionsList);
    }

    /**
     * Used for data transfer between Scenes
     * @param pbank Bank to use
     * @param name  Name of account to display
     */
    public void setUpAccount(PrivateBank pbank, String name){
        bank = pbank;
        this.name = name;
        textAccount.setText(String.format("Konto: %s | Kontostand: %.2f€",name,bank.getAccountBalance(name)));
        updateTransactionList(bank.getTransactions(name));
    }

    /**
     * Creates a dialog for adding of transactions
     * @param type Type of transaction as String
     */
    public void addTransaction(String type){
        Dialog<Transaction> dialog = new Dialog<>();

        GridPane gridPane = new GridPane();

        Label date = new Label("Datum: ");
        Label description = new Label("Beschreibung: ");
        Label amount = new Label("Betrag: ");

        Label incomingInterest_sender = new Label();
        Label outgoingInterest_recipient = new Label();


        TextField dateText = new TextField();
        TextField descriptionText = new TextField();
        TextField amountText = new TextField();
        TextField incomingInterest_senderText = new TextField();
        TextField outgoingInterest_recipientText = new TextField();

        gridPane.add(date,1,1);
        gridPane.add(dateText,2,1);
        gridPane.add(description,1,2);
        gridPane.add(descriptionText,2,2);
        gridPane.add(amount,1,3);
        gridPane.add(amountText,2,3);
        gridPane.add(incomingInterest_sender,1,4);
        gridPane.add(incomingInterest_senderText,2,4);
        gridPane.add(outgoingInterest_recipient,1,5);
        gridPane.add(outgoingInterest_recipientText ,2,5);

        dialog.getDialogPane().setContent(gridPane);
        ButtonType okButton = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(okButton);
        dialog.getDialogPane().setMinWidth(300);
        dialog.getDialogPane().setMinHeight(250);

        Alert invalid = new Alert(Alert.AlertType.ERROR);
        invalid.setContentText("ERROR!");
        dialog.show();

        if(type.equals("payment")){
            dialog.setTitle("Neue Zahlung hinzufügen.");
            dialog.setHeaderText(String.format("Neue Zahlung zu Konto %s hinzufügen", name));
            incomingInterest_sender.setText("Ausgehender Zinssatz: ");
            outgoingInterest_recipient.setText("Eingehender Zinssatz: ");

            dialog.setResultConverter( buttonType -> {
                if(buttonType == cancelButton){
                    dialog.close();
                }
                if(buttonType == okButton){

                    if(dateText.getText().equals("") || descriptionText.getText().equals("") || amountText.getText().equals("") || incomingInterest_senderText.getText().equals("") || outgoingInterest_recipientText.getText().equals("")){
                        invalid.setContentText("Bitte geben Sie gültige Werte ein!");
                        invalid.showAndWait();
                        return null;
                    }

                    Payment p = new Payment( dateText.getText(),Double.parseDouble(amountText.getText()), descriptionText.getText(), Double.parseDouble(incomingInterest_senderText.getText()), Double.parseDouble(outgoingInterest_recipientText.getText()) );

                    try{
                        bank.addTransaction(name, p);
                    } catch (TransactionAlreadyExistException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Error");
                        alert.show();

                        return null;
                    } catch (AccountDoesNotExistException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Error");
                        alert.show();
                        e.printStackTrace();
                    } catch (TransactionAttributeException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Error");
                        alert.show();

                        return null;
                    }

                    updateTransactionList(bank.getTransactions(name));
                    textAccount.setText(String.format("Konto: %s | Kontostand: %.2f€",name,bank.getAccountBalance(name)));
                }
                return null;
            });
        }
        else if(type.equals("transfer")){
            dialog.setTitle("Neuen Transfer hinzufügen.");
            dialog.setHeaderText(String.format("Neuen Transfer zu Konto %s hinzufügen", name));
            incomingInterest_sender.setText("Sender: ");
            outgoingInterest_recipient.setText("Empfänger: ");

            dialog.setResultConverter( buttonType -> {
                if(buttonType == cancelButton){
                    dialog.close();
                }
                if(buttonType == okButton){

                    if(dateText.getText().equals("") || descriptionText.getText().equals("") || amountText.getText().equals("") || incomingInterest_senderText.getText().equals("") || outgoingInterest_recipientText.getText().equals("")){

                        invalid.showAndWait();
                        return null;
                    }
                    if(incomingInterest_senderText.getText().equals(outgoingInterest_recipientText.getText())){

                        invalid.showAndWait();
                        return null;
                    }
                    if(incomingInterest_senderText.getText().equals(name)){
                        OutgoingTransfer t = null;
                        try {
                            t = new OutgoingTransfer(dateText.getText(),Double.parseDouble(amountText.getText()), descriptionText.getText(), incomingInterest_senderText.getText(), outgoingInterest_recipientText.getText());
                        } catch (TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        try{
                            bank.addTransaction(name, t);
                        } catch (TransactionAlreadyExistException e) {

                            invalid.showAndWait();
                            return null;
                        } catch (AccountDoesNotExistException e) {
                            e.printStackTrace();
                        } catch (TransactionAttributeException e) {

                            invalid.showAndWait();
                            return null;
                        }
                    }
                    else if(outgoingInterest_recipientText.getText().equals(name)){
                        IncomingTransfer t = null;
                        try {
                            t = new IncomingTransfer(dateText.getText(),Double.parseDouble(amountText.getText()), descriptionText.getText(), incomingInterest_senderText.getText(), outgoingInterest_recipientText.getText());
                        } catch (TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        try{
                            bank.addTransaction(name, t);
                        } catch (TransactionAlreadyExistException e) {

                            invalid.showAndWait();
                            return null;
                        } catch (AccountDoesNotExistException e) {
                            e.printStackTrace();
                        } catch (TransactionAttributeException e) {
                            invalid.showAndWait();
                            return null;
                        }
                    }
                    else{

                        invalid.showAndWait();
                        return null;
                    }

                    updateTransactionList(bank.getTransactions(name));
                    textAccount.setText(String.format("Konto: %s | Kontostand: %.2f€",name,bank.getAccountBalance(name)));
                }
                return null;

            });
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Setup für Kontext Menü
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteTransaction = new MenuItem("Transaktion löschen");
        contextMenu.getItems().add(deleteTransaction);
        //Füge Kontextmenü zu ListView hinzu
        transactionsListView.setContextMenu(contextMenu);

        //Leichterer Zugriff auf aktuell ausgewählte Transaktion
        AtomicReference<Transaction> selectedTransaction = new AtomicReference<>();
        transactionsListView.setOnMouseClicked(mouseEvent -> selectedTransaction.set(transactionsListView.getSelectionModel().getSelectedItem()));


        //Funktionalität der Sortierungs-Buttons
        descending.setOnAction(event-> updateTransactionList(bank.getTransactionsSorted(name, false)));

        ascending.setOnAction(event-> updateTransactionList(bank.getTransactionsSorted(name, true)));

        negative.setOnAction(event-> updateTransactionList(bank.getTransactionsByType(name, false)));

        positive.setOnAction(event-> updateTransactionList(bank.getTransactionsByType(name, true)));

        //Löschen einer Transaktion
        deleteTransaction.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Löschung bestätigen.");
            confirm.setContentText("Möchten sie die Transaktion wirklich löschen?");
            Optional<ButtonType> result = confirm.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK){
                try{
                    bank.removeTransaction(name, selectedTransaction.get());
                } catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
                    e.printStackTrace();
                }
                updateTransactionList(bank.getTransactions(name));
                textAccount.setText(String.format("Konto: %s | Kontostand: %.2f€",name,bank.getAccountBalance(name)));
            }
        });


        payment.setOnAction(event -> addTransaction("payment"));

        transaction.setOnAction(event -> addTransaction("transfer"));



        //Wechsel zurück zur Konten-Übersicht
        backButton.setOnMouseClicked(mouseEvent -> {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("mainpage.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setTitle("MainView");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}
