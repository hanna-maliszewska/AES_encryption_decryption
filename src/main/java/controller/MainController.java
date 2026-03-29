package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.AES;

public class MainController {

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }

        return data;
    }

    @FXML
    private RadioButton textRadio;

    @FXML
    private RadioButton fileRadio;

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
    private Button readEncryptedFileButton;

    @FXML
    private Button writeEncryptedFileButton;

    @FXML
    private Button readPlainFileButton;

    @FXML
    private Button writePlainFileButton;

    @FXML
    private TextArea encryptedText;

    @FXML
    private Button generate128Button;

    @FXML
    private Button generate192Button;

    @FXML
    private Button generate256Button;

    @FXML
    private Button encryptButton;

    @FXML
    private Button decryptButton;

    @FXML
    public void initialize() {
        plainText.setEditable(true);
        readPlainFileButton.setDisable(true);
        writePlainFileButton.setDisable(true);
        encryptedText.setEditable(false);
        readEncryptedFileButton.setDisable(true);
        writeEncryptedFileButton.setDisable(true);
        // Listener zmiany wyboru Text / File
        ToggleGroup textOrFileGroup = textRadio.getToggleGroup();
        textOrFileGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == textRadio) {
                // Text wybrane
                plainText.setEditable(true);
                readPlainFileButton.setDisable(true);
                writePlainFileButton.setDisable(true);
                encryptedText.setEditable(true);
                readEncryptedFileButton.setDisable(true);
                writeEncryptedFileButton.setDisable(true);
            } else if (newToggle == fileRadio) {
                // File wybrane
                plainText.setEditable(false);
                readPlainFileButton.setDisable(false);
                writePlainFileButton.setDisable(false);
                encryptedText.setEditable(false);
                readEncryptedFileButton.setDisable(false);
                writeEncryptedFileButton.setDisable(false);
            }
        });

        // Blokada pola klucza do wpisywania ręcznego
        keyField.setEditable(false);
    }

    @FXML
    public void generate128() {
        byte[] key = new byte[16]; // dla 128
        new java.security.SecureRandom().nextBytes(key);

        keyField.setText(bytesToHex(key));

        keyField.setText(bytesToHex(key));
    }

    @FXML
    public void generate192() {
        String key = "2";

        keyField.setText(key);
    }

    @FXML
    public void generate256() {
        String key = "3";

        keyField.setText(key);
    }

    @FXML
    public void encrypt() {
        if (keyField.getText().isEmpty()) {
            encryptedText.setText("Generate key first!");
            return;
        }

        try {
            byte[] input = plainText.getText().getBytes();
            byte[] key = hexToBytes(keyField.getText());

            byte[] result = AES.encryptData(input, key);

            encryptedText.setText(bytesToHex(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void decrypt() {
        String text = encryptedText.getText();
        plainText.setText("DECRYPTED: " + text);
    }

    public static byte[] addPadding(byte[] input) {
        int blockSize = 16;
        int padding = blockSize - (input.length % blockSize);

        byte[] output = new byte[input.length + padding];

        // copy
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }

        // add padding
        for (int i = input.length; i < output.length; i++) {
            output[i] = (byte) padding;
        }

        return output;
    }

    public static byte[] removePadding(byte[] input) {
        int padding = input[input.length - 1] & 0xFF;

        byte[] output = new byte[input.length - padding];

        for (int i = 0; i < output.length; i++) {
            output[i] = input[i];
        }

        return output;
    }
}