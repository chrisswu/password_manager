package ui;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import backend.PasswordManager;

import java.util.Optional;

public class Main extends Application {

    private PasswordManager pwManager = new PasswordManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Password Manager");

        primaryStage.setScene(loginScreen());
        primaryStage.show();
    }

    public Scene loginScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        // sign in
        Button signInBtn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signInBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 3);
        signInBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (pwManager.checkMasterPassword(pwBox.getText())) {
                    Stage primaryStage = (Stage) signInBtn.getScene().getWindow();
                    primaryStage.setScene(pwManagerScreen());
                } else {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Incorrect master password");
                }
            }
        });

        // if master pw exists, you can change master pw
        // otherwise, you can set a master pw
        if (pwManager.checkMasterPasswordExist()) {
            // change master password
            Button changePwBtn = new Button("Change master password");
            grid.add(changePwBtn, 1, 5);
            changePwBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Stage primaryStage = (Stage) changePwBtn.getScene().getWindow();
                    primaryStage.setScene(pwManagerScreen());
                }
            });
        } else {
            // set master password
            Button setPwBtn = new Button("Set master password");
            grid.add(setPwBtn, 1, 5);
            setPwBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Stage primaryStage = (Stage) setPwBtn.getScene().getWindow();
                    primaryStage.setScene(pwManagerScreen());
                }
            });
        }
        return scene;
    }

    public Scene setMasterPasswordScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Text text = new Text();
        text.setText("~ Set master password ~\nIt must contain:\n- lower case\n- upper case\n- numeric\n- special\n");
        grid.add(text, 0, 0);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 1);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 1);

        Label pw1 = new Label("Re-Enter:");
        grid.add(pw1, 0, 2);
        PasswordField pw1Box = new PasswordField();
        grid.add(pw1Box, 1, 2);

        Button btn = new Button("Confirm");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (pwBox.getText().equals(pw1Box.getText())) {
                    if (pwManager.checkString(pwBox.getText())) {
                        pwManager.addMasterPassword(pwBox.getText());
                        Stage primaryStage = (Stage) btn.getScene().getWindow();
                        primaryStage.setScene(successfulPwSetScreen());
                    } else {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Password does not meet specifications");
                    }
                } else {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Password does not match");
                }
            }
        });
        return scene;
    }

    public Scene successfulPwSetScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Text text = new Text("Successfully set master password! You can now use it to log in.");
        text.setFill(Color.GREEN);
        grid.add(text, 0, 0);

        Button btn = new Button("Continue");
        grid.add(btn, 0, 5);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Stage primaryStage = (Stage) btn.getScene().getWindow();
                primaryStage.setScene(pwManagerScreen());
            }
        });
        return scene;
    }

    public Scene pwManagerScreen() {
        pwManager.init();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Label name = new Label("Name of account:");
        grid.add(name, 0, 2);
        TextField nameBox = new TextField();
        grid.add(nameBox, 1, 2);

        Button searchBtn = new Button("Search");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(searchBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 3);

        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Dialog dialog = pwInputDialog();
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(password -> {
                    if (pwManager.retrievePassword(password, nameBox.getText()) == null) {
                        actiontarget.setText("The account name you entered does not exist.");
                    } else {
                        actiontarget.setText(pwManager.retrievePassword(password, nameBox.getText()));
                    }
                });
                nameBox.clear();
            }
        });

        Button addPwdBtn = new Button("Add account");
        grid.add(addPwdBtn, 0,4);

        addPwdBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage primaryStage = (Stage) addPwdBtn.getScene().getWindow();
                primaryStage.setScene(addPasswordScreen());
            }
        });
        return scene;
    }

    public Scene addPasswordScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Label name = new Label("Name of account:");
        grid.add(name, 0, 2);
        TextField nameBox = new TextField();
        grid.add(nameBox, 1, 2);

        Label password = new Label("Password:");
        grid.add(password, 0, 3);
        TextField passwordBox = new TextField();
        grid.add(passwordBox, 1, 3);

        Label masterPassword = new Label("Master Password:");
        grid.add(masterPassword, 0, 4);
        PasswordField masterPasswordBox = new PasswordField();
        grid.add(masterPasswordBox, 1, 4);

        Button confirmBtn = new Button("Confirm");
        grid.add(confirmBtn, 1, 6);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 5);

        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                actionTarget.setText(
                        pwManager.addPassword(masterPasswordBox.getText(), nameBox.getText(), passwordBox.getText())
                );
                nameBox.clear();
                passwordBox.clear();
                masterPasswordBox.clear();
            }
        });

        Button backBtn = new Button("Back");
        grid.add(backBtn, 0, 6);
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage primaryStage = (Stage) backBtn.getScene().getWindow();
                primaryStage.setScene(pwManagerScreen());
            }
        });


        return scene;
    }

    // custom pw dialog box
    public Dialog pwInputDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Verification");
        dialog.setHeaderText("Enter master password");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pw = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Password:"), pw);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pw.getText();
            }
            return null;
        });
        return dialog;
    }

    // idk about this function
    public Scene changeMasterPassword() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Text text = new Text();
        text.setText("Enter master password");
        grid.add(text, 0, 0);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 1);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 1);

        Button btn = new Button("Confirm");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (pwManager.checkMasterPassword(pwBox.getText())) {
                    actiontarget.setFill(Color.GREEN);
                    actiontarget.setText("You may now change your master password");
                } else {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Master password incorrect");
                }
            }
        });
        return scene;
    }
}
