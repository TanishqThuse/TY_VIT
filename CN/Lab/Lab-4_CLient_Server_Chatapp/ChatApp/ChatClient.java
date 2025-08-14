import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        Thread receiveThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("bye")) {
                        System.out.println("Server ended the chat.");
                        socket.close();
                        System.exit(0);
                    }
                    System.out.println("Server: " + msg);
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
                System.exit(0);
            }
        }
    }
}
