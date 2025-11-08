import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 5000;
    private static Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();
    private static Map<String, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("Campus Communication Portal Server Started");
        System.out.println("===========================================");
        
        try {
            // Display server IP address
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Server IP Address: " + localHost.getHostAddress());
            System.out.println("Server Port: " + PORT);
            System.out.println("Waiting for clients to connect...\n");
            
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected from: " + clientSocket.getInetAddress().getHostAddress());
                    
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private String username;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                
                // Receive username
                username = in.readUTF();
                onlineUsers.put(username, this);
                System.out.println("[" + getCurrentTime() + "] " + username + " joined the chat (Total users: " + onlineUsers.size() + ")");
                
                // Broadcast join message
                broadcastMessage("SERVER", username + " joined the chat");
                broadcastUserList();
                
                // Handle messages
                while (true) {
                    String messageType = in.readUTF();
                    
                    switch (messageType) {
                        case "MESSAGE":
                            handleTextMessage();
                            break;
                        case "FILE":
                            handleFileTransfer();
                            break;
                        case "PRIVATE":
                            handlePrivateMessage();
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println("[" + getCurrentTime() + "] " + username + " disconnected");
            } finally {
                cleanup();
            }
        }
        
        private void handleTextMessage() throws IOException {
            String message = in.readUTF();
            System.out.println("[" + getCurrentTime() + "] " + username + ": " + message);
            broadcastMessage(username, message);
        }
        
        private void handleFileTransfer() throws IOException {
            String fileName = in.readUTF();
            long fileSize = in.readLong();
            
            byte[] fileData = new byte[(int) fileSize];
            in.readFully(fileData);
            
            System.out.println("[" + getCurrentTime() + "] " + username + " sent file: " + fileName + " (" + fileSize + " bytes)");
            broadcastFile(username, fileName, fileData);
        }
        
        private void handlePrivateMessage() throws IOException {
            String recipient = in.readUTF();
            String message = in.readUTF();
            
            System.out.println("[" + getCurrentTime() + "] Private message from " + username + " to " + recipient);
            
            ClientHandler recipientHandler = onlineUsers.get(recipient);
            if (recipientHandler != null) {
                recipientHandler.sendPrivateMessage(username, message);
                sendPrivateMessage("You", message); // Echo to sender
            } else {
                sendMessage("SERVER", "User " + recipient + " is not online");
            }
        }
        
        private void broadcastMessage(String sender, String message) {
            for (ClientHandler client : clientHandlers) {
                client.sendMessage(sender, message);
            }
        }
        
        private void broadcastFile(String sender, String fileName, byte[] fileData) {
            for (ClientHandler client : clientHandlers) {
                if (client != this) {
                    client.sendFile(sender, fileName, fileData);
                }
            }
        }
        
        private void broadcastUserList() {
            String userList = String.join(",", onlineUsers.keySet());
            for (ClientHandler client : clientHandlers) {
                try {
                    client.out.writeUTF("USERLIST");
                    client.out.writeUTF(userList);
                    client.out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void sendMessage(String sender, String message) {
            try {
                out.writeUTF("MESSAGE");
                out.writeUTF(sender);
                out.writeUTF(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void sendPrivateMessage(String sender, String message) {
            try {
                out.writeUTF("PRIVATE");
                out.writeUTF(sender);
                out.writeUTF(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void sendFile(String sender, String fileName, byte[] fileData) {
            try {
                out.writeUTF("FILE");
                out.writeUTF(sender);
                out.writeUTF(fileName);
                out.writeLong(fileData.length);
                out.write(fileData);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void cleanup() {
            try {
                clientHandlers.remove(this);
                if (username != null) {
                    onlineUsers.remove(username);
                    System.out.println("[" + getCurrentTime() + "] " + username + " left (Total users: " + onlineUsers.size() + ")");
                    broadcastMessage("SERVER", username + " left the chat");
                    broadcastUserList();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private String getCurrentTime() {
            return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }
}