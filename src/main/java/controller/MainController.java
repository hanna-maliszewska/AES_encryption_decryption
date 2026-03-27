package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextArea plainText;

    @FXML
    private TextArea encryptedText;

    @FXML
    private TextField keyField;

    @FXML
    public void generate() {
        String input = plainText.getText();
        String key = keyField.getText();

        // testing :3
        encryptedText.setText("Input: " + input + "\nKey: " + key);
    }
}