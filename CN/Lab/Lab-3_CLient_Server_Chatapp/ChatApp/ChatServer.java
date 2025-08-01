package ChatApp;
import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        Thread receiveThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("bye")) {
                        System.out.println("Client ended the chat.");
                        socket.close();
                        serverSocket.close();
                        System.exit(0);
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });

        receiveThread.start();

        String msg;
        while ((msg = keyboard.readLine()) != null) {
            out.println(msg);
            if (msg.equalsIgnoreCase("bye")) {
                System.out.println("Chat ended.");
                socket.close();
                serverSocket.close();
                System.exit(0);
            }
        }
    }
}
