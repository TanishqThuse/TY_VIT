import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatGUI extends Application implements ChatClient.MessageListener {
    private ChatClient client;
    private String username;
    private TextArea chatArea;
    private TextField messageField;
    private ListView<String> userListView;
    private ComboBox<String> recipientCombo;
    private Label statusLabel;
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    
    @Override
    public void start(Stage primaryStage) {
        // Login dialog
        showLoginDialog(primaryStage);
    }
    
    private void showLoginDialog(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Campus Communication Portal - Login");
        
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #2c3e50;");
        
        Label titleLabel = new Label("Campus Communication Portal");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Connect. Share. Collaborate.");
        subtitleLabel.setFont(Font.font("Arial", 12));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(250);
        
        Button loginButton = new Button("Connect");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        loginButton.setMinWidth(100);
        
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        
        loginButton.setOnAction(e -> {
            String user = usernameField.getText().trim();
            if (user.isEmpty()) {
                errorLabel.setText("Username cannot be empty");
                return;
            }
            
            try {
                username = user;
                client = new ChatClient(SERVER_HOST, SERVER_PORT);
                client.setMessageListener(this);
                client.login(username);
                
                loginStage.close();
                showChatWindow(primaryStage);
            } catch (IOException ex) {
                errorLabel.setText("Connection failed: " + ex.getMessage());
            }
        });
        
        usernameField.setOnAction(e -> loginButton.fire());
        
        loginBox.getChildren().addAll(titleLabel, subtitleLabel, usernameField, loginButton, errorLabel);
        
        Scene scene = new Scene(loginBox, 400, 300);
        loginStage.setScene(scene);
        loginStage.show();
    }
    
    private void showChatWindow(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        // Top bar
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        // Left sidebar - User list
        VBox leftPanel = createUserListPanel();
        root.setLeft(leftPanel);
        
        // Center - Chat area
        VBox centerPanel = createChatPanel();
        root.setCenter(centerPanel);
        
        // Bottom - Status bar
        statusLabel = new Label("Connected as: " + username);
        statusLabel.setPadding(new Insets(5));
        statusLabel.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        root.setBottom(statusLabel);
        
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Campus Communication Portal - " + username);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
            if (client != null) {
                client.disconnect();
            }
            Platform.exit();
        });
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #2c3e50;");
        
        Label titleLabel = new Label("Campus Communication Portal");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button clearButton = new Button("Clear Chat");
        clearButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        clearButton.setOnAction(e -> chatArea.clear());
        
        topBar.getChildren().addAll(titleLabel, spacer, clearButton);
        return topBar;
    }
    
    private VBox createUserListPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setMinWidth(200);
        panel.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 0 1 0 0;");
        
        Label usersLabel = new Label("Online Users");
        usersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        userListView = new ListView<>();
        userListView.setPrefHeight(500);
        
        panel.getChildren().addAll(usersLabel, userListView);
        return panel;
    }
    
    private VBox createChatPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        
        // Chat display area
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setFont(Font.font("Consolas", 12));
        VBox.setVgrow(chatArea, Priority.ALWAYS);
        
        // Message input area
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);
        
        Label recipientLabel = new Label("To:");
        recipientCombo = new ComboBox<>();
        recipientCombo.getItems().add("Everyone");
        recipientCombo.setValue("Everyone");
        recipientCombo.setMinWidth(120);
        
        messageField = new TextField();
        messageField.setPromptText("Type your message...");
        HBox.setHgrow(messageField, Priority.ALWAYS);
        
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        sendButton.setMinWidth(80);
        sendButton.setOnAction(e -> sendMessage());
        
        Button fileButton = new Button("📎 File");
        fileButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        fileButton.setOnAction(e -> sendFile());
        
        messageField.setOnAction(e -> sendMessage());
        
        inputBox.getChildren().addAll(recipientLabel, recipientCombo, messageField, sendButton, fileButton);
        
        panel.getChildren().addAll(chatArea, inputBox);
        return panel;
    }
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) return;
        
        try {
            String recipient = recipientCombo.getValue();
            if (recipient.equals("Everyone")) {
                client.sendMessage(message);
            } else {
                client.sendPrivateMessage(recipient, message);
            }
            messageField.clear();
        } catch (IOException e) {
            showAlert("Error", "Failed to send message: " + e.getMessage());
        }
    }
    
    private void sendFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Share");
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
            try {
                byte[] fileData = Files.readAllBytes(file.toPath());
                client.sendFile(file.getName(), fileData);
                appendMessage("You", "Sent file: " + file.getName());
            } catch (IOException e) {
                showAlert("Error", "Failed to send file: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void onMessageReceived(String sender, String message) {
        Platform.runLater(() -> appendMessage(sender, message));
    }
    
    @Override
    public void onPrivateMessageReceived(String sender, String message) {
        Platform.runLater(() -> appendPrivateMessage(sender, message));
    }
    
    @Override
    public void onFileReceived(String sender, String fileName, byte[] fileData) {
        Platform.runLater(() -> {
            appendMessage(sender, "Sent a file: " + fileName);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File Received");
            alert.setHeaderText("File from " + sender);
            alert.setContentText("Would you like to save: " + fileName + " (" + fileData.length + " bytes)?");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(fileName);
                    File saveFile = fileChooser.showSaveDialog(null);
                    
                    if (saveFile != null) {
                        try {
                            Files.write(saveFile.toPath(), fileData);
                            appendMessage("System", "File saved successfully");
                        } catch (IOException e) {
                            showAlert("Error", "Failed to save file: " + e.getMessage());
                        }
                    }
                }
            });
        });
    }
    
    @Override
    public void onUserListUpdated(String[] users) {
        Platform.runLater(() -> {
            userListView.getItems().clear();
            recipientCombo.getItems().clear();
            recipientCombo.getItems().add("Everyone");
            
            for (String user : users) {
                userListView.getItems().add(user);
                if (!user.equals(username)) {
                    recipientCombo.getItems().add(user);
                }
            }
            
            if (recipientCombo.getValue() == null) {
                recipientCombo.setValue("Everyone");
            }
        });
    }
    
    private void appendMessage(String sender, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        chatArea.appendText(String.format("[%s] %s: %s\n", timestamp, sender, message));
    }
    
    private void appendPrivateMessage(String sender, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        chatArea.appendText(String.format("[%s] [PRIVATE] %s: %s\n", timestamp, sender, message));
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}