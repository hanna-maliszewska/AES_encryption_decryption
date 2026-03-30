package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.AES;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    private File selectedInputFile = null;
    private File selectedOutputFile = null;

    @FXML
    private RadioButton textRadio;

    @FXML
    private RadioButton fileRadio;

    @FXML
    private TextArea plainText;

    @FXML
    private TextField keyField;

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
    public void readPlainFile() {
        FileChooser chooser = new FileChooser();
        selectedInputFile = chooser.showOpenDialog(null);
        if (selectedInputFile != null) {
            plainText.setText("file: " + selectedInputFile.getAbsolutePath());
        }
    }

    @FXML
    public void readEncryptedFile() {
        FileChooser chooser = new FileChooser();
        selectedInputFile = chooser.showOpenDialog(null);
        if (selectedInputFile != null) {
            encryptedText.setText("encrypted file: " + selectedInputFile.getAbsolutePath());
        }
    }

    @FXML
    public void writePlainFile() throws IOException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(null);
        byte[] data = plainText.getText().getBytes();
        Files.write(file.toPath(), data);
    }

    @FXML
    public void writeEncryptedFile() throws IOException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(null);
        byte[] data = hexToBytes(encryptedText.getText());
        Files.write(file.toPath(), data);
    }

    @FXML
    public void saveKey() throws IOException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(null);
        byte[] data = keyField.getText().getBytes();
        Files.write(file.toPath(), data);
    }

    @FXML
    public void loadKey() throws IOException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);

        byte[] data = Files.readAllBytes(file.toPath());
        keyField.setText(new String(data));
    }

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
        byte[] key = new byte[16];
        new java.security.SecureRandom().nextBytes(key);

        keyField.setText(bytesToHex(key));

        keyField.setText(bytesToHex(key));
    }

    @FXML
    public void generate192() {
        byte[] key = new byte[24];
        new java.security.SecureRandom().nextBytes(key);

        keyField.setText(bytesToHex(key));

        keyField.setText(bytesToHex(key));
    }

    @FXML
    public void generate256() {
        byte[] key = new byte[32];
        new java.security.SecureRandom().nextBytes(key);

        keyField.setText(bytesToHex(key));

        keyField.setText(bytesToHex(key));
    }

    @FXML
    public void encrypt() {
        if (keyField.getText().isEmpty()) {
            encryptedText.setText("Generate key first!");
            return;
        }

        try {
            byte[] key = hexToBytes(keyField.getText());

            if (textRadio.isSelected()) {
                //TEXT
                byte[] input = plainText.getText().getBytes();
                byte[] result = AES.encryptData(input, key);
                encryptedText.setText(bytesToHex(result));

            } else if (fileRadio.isSelected()) {
                // FILES
                if (selectedInputFile == null) {
                    encryptedText.setText("Choose a file to encrypt first!");
                    return;
                }

                // Load bytes from file
                byte[] fileBytes = Files.readAllBytes(selectedInputFile.toPath());

                // encrypt
                byte[] encryptedFileBytes = AES.encryptData(fileBytes, key);

                // where to save encrypted file?
                FileChooser chooser = new FileChooser();
                selectedOutputFile = chooser.showSaveDialog(null);

                if (selectedOutputFile != null) {
                    // Save encrypted bytes to file
                    Files.write(selectedOutputFile.toPath(), encryptedFileBytes);
                    encryptedText.setText("Encrypted file to: " + selectedOutputFile.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void decrypt() {
        if (keyField.getText().isEmpty()) {
            plainText.setText("Generate key first!");
            return;
        }

        try {
            byte[] key = hexToBytes(keyField.getText());

            if (textRadio.isSelected()) {
                // TEXT
                byte[] input = hexToBytes(encryptedText.getText());

                byte[] result = AES.decryptData(input, key);

                plainText.setText(new String(result));

            } else if (fileRadio.isSelected()) {
                // FILE
                if (selectedInputFile == null) {
                    plainText.setText("Choose a file to decrypt first!");
                    return;
                }

                // LOAD bytes from file
                // no hextobytes just bytes
                byte[] fileBytes = Files.readAllBytes(selectedInputFile.toPath());

                // decrypt
                byte[] decryptedFileBytes = AES.decryptData(fileBytes, key);

                // where to save decrypted file?
                FileChooser chooser = new FileChooser();
                selectedOutputFile = chooser.showSaveDialog(null);

                if (selectedOutputFile != null) {
                    // save decrypted bytes to file
                    Files.write(selectedOutputFile.toPath(), decryptedFileBytes);
                    plainText.setText("Success! Decrypted file to: " + selectedOutputFile.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (fileRadio.isSelected()) {
                plainText.setText("Error! (wrong key or file).");
            } else {
                plainText.setText("Text decryption error!");
            }
        }
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