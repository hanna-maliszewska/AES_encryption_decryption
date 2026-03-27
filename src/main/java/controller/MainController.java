package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private RadioButton textRadio;

    @FXML
    private RadioButton fileRadio;

    @FXML
    private Button readFileButton1;

    @FXML
    private Button writeFileButton1;

    @FXML
    private TextArea plainText;

    @FXML
    private TextField keyField;

    @FXML
    private Button generateKeyButton;

    @FXML
    private Button loadKeyButton;

    @FXML
    private Button saveKeyButton;

    @FXML
    private Button readFileButton2;

    @FXML
    private Button writeFileButton2;

    @FXML
    private TextArea encryptedText;


    @FXML
    public void initialize() {
        // Listener zmiany wyboru Text / File
        ToggleGroup textOrFileGroup = textRadio.getToggleGroup();
        textOrFileGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == textRadio) {
                // Text wybrane
                plainText.setEditable(true);
                readFileButton1.setDisable(true);
                writeFileButton1.setDisable(true);
                readFileButton2.setDisable(true);
                writeFileButton2.setDisable(true);
            } else if (newToggle == fileRadio) {
                // File wybrane
                plainText.setEditable(false);
                readFileButton1.setDisable(false);
                writeFileButton1.setDisable(false);
                readFileButton2.setDisable(false);
                writeFileButton2.setDisable(false);
            }
        });

        // Blokada pola klucza do wpisywania ręcznego
        keyField.setEditable(false);
    }

    @FXML
    public void generate() {
        String input = plainText.getText();
        String key = keyField.getText();

        // testing :3
        encryptedText.setText("Input: " + input + "\nKey: " + key);
    }
}