import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private MessageListener messageListener;
    private boolean running = true;
    
    public interface MessageListener {
        void onMessageReceived(String sender, String message);
        void onPrivateMessageReceived(String sender, String message);
        void onFileReceived(String sender, String fileName, byte[] fileData);
        void onUserListUpdated(String[] users);
    }
    
    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }
    
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }
    
    public void login(String username) throws IOException {
        out.writeUTF(username);
        out.flush();
        
        // Start listening thread
        new Thread(() -> {
            try {
                listenForMessages();
            } catch (IOException e) {
                if (running) {
                    System.err.println("Connection lost: " + e.getMessage());
                }
            }
        }).start();
    }
    
    private void listenForMessages() throws IOException {
        while (running) {
            String messageType = in.readUTF();
            
            switch (messageType) {
                case "MESSAGE":
                    String sender = in.readUTF();
                    String message = in.readUTF();
                    if (messageListener != null) {
                        messageListener.onMessageReceived(sender, message);
                    }
                    break;
                    
                case "PRIVATE":
                    String privateSender = in.readUTF();
                    String privateMessage = in.readUTF();
                    if (messageListener != null) {
                        messageListener.onPrivateMessageReceived(privateSender, privateMessage);
                    }
                    break;
                    
                case "FILE":
                    String fileSender = in.readUTF();
                    String fileName = in.readUTF();
                    long fileSize = in.readLong();
                    byte[] fileData = new byte[(int) fileSize];
                    in.readFully(fileData);
                    if (messageListener != null) {
                        messageListener.onFileReceived(fileSender, fileName, fileData);
                    }
                    break;
                    
                case "USERLIST":
                    String userList = in.readUTF();
                    String[] users = userList.split(",");
                    if (messageListener != null) {
                        messageListener.onUserListUpdated(users);
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }
    
    public void sendMessage(String message) throws IOException {
        out.writeUTF("MESSAGE");
        out.writeUTF(message);
        out.flush();
    }
    
    public void sendPrivateMessage(String recipient, String message) throws IOException {
        out.writeUTF("PRIVATE");
        out.writeUTF(recipient);
        out.writeUTF(message);
        out.flush();
    }
    
    public void sendFile(String fileName, byte[] fileData) throws IOException {
        out.writeUTF("FILE");
        out.writeUTF(fileName);
        out.writeLong(fileData.length);
        out.write(fileData);
        out.flush();
    }
    
    public void disconnect() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}