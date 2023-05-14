package ui;
import java.io.IOException;
import java.util.*;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import bank.*;
import bank.exceptions.*;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.text.Text.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;


public class mainPageController {

    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button addAccountButton;
    @FXML
    private ListView<String> accountsListView;
    @FXML
    private Label mylabel;
    @FXML
    private ContextMenu cont;
    @FXML
    private MenuItem select;
    @FXML
    private MenuItem delete;

    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;



     PrivateBank privateBank = new PrivateBank("Bank", 0.25, 0.3);

    /**
     * mainPageController
     */
    public mainPageController() {
        try {
            privateBank.createAccount("Kevin", List.of(
                    new Payment("24.03.2001", 300.1, "Payment", 0.8, 0.5),
                    new Payment("24.03.2001", 405, "Payment", 0.3, 0.6),
                    new Payment("24.03.2001", 231, "Payment", 0.4, 0.5),
                    new OutgoingTransfer("20.11.2022", 20, "Transfer", "Mark", "Kevin"),
                    new Payment("24.06.2001", -300.1, "Payment", 0.1, 0.2)
            ));
            privateBank.createAccount("Christian", List.of(
                    new OutgoingTransfer("09.01.2015", 734.12, "Transfer", "Mark", "Christian"),
                    new Payment("24.06.2001", -300.1, "Payment", 0.1, 0.2),
                    new IncomingTransfer("09.01.2015", 734.12, "Transfer", "Christian", "Mark"),
                    new Payment("24.03.2001", 308, "Payment", 0.37, 0.23),
                    new IncomingTransfer("09.01.2015", 734.12, "Transfer", "Christian", "Betty")
            ));
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e);
        }
    }
    String[] arr = {"Hwllo","World"};

    /**
     * initialize FXML
     */
    @FXML
    public void initialize() {


        accountsListView.getItems().addAll(privateBank.getAllAccounts());
        accountsListView.setContextMenu(cont);

    }

    /**
     *
     * select account
     */
    @FXML
    private void select() {
        String account = accountsListView.getSelectionModel().getSelectedItem().toString().replace("[", "").replace("]", "");
        stage = (Stage) root.getScene().getWindow();
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("accountview.fxml"));
            root = fxmlLoader.load();

            AccountviewController accountController = fxmlLoader.getController();
            accountController.setUpAccount(privateBank, account.toString().replace("[", "").replace("]", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }

        scene = new Scene(root);
        stage.setTitle(account.toString());
        stage.setScene(scene);
        stage.show();

    }

    /**
     * shows confirm Dailog by delete
     * @param title
     * @param message
     * @return
     */
    public boolean showConfirmDialog(String title, String message) {
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, yesButton, noButton);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    /**
     * deletes an account
     */
    @FXML
    private void delete() {
        String account = accountsListView.getSelectionModel().getSelectedItem().toString().replace("[", "").replace("]", "");
        boolean confirmation = showConfirmDialog("Delete account?", "Are you sure you want to delete the account '" + account + "'?");
        if (confirmation) {
            try {
                privateBank.deleteAccount(account);
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            accountsListView.getItems().remove(account);
        }
    }

    /**
     * adds an Account
     * @param actionEvent
     */
    public void AddAcc(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Account");
        dialog.setHeaderText("Enter the name of the new account:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                privateBank.createAccount(result.get());
            } catch (AccountAlreadyExistsException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Error");
                alert.show();
                throw new RuntimeException(e);
            }
            accountsListView.getItems().add(result.get());
        }
    }
}
