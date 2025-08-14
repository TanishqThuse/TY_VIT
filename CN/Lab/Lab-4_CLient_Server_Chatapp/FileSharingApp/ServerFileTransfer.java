import java.io.*;
import java.net.*;

public class ServerFileTransfer {
    public static void main(String[] args) throws IOException {
        // Create server socket on port 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("ServerFileTransfer started. Waiting for client...");

        // Accept client connection
        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        // Open the file to send
        File file = new File("D:/Backup/Tanishq/VIT/VIT_File/TY_VIT/CN/Lab/Lab-3_CLient_Server_Chatapp/fileToShare.txt" + //
                        "");
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            socket.close();
            serverSocket.close();
            return;
        }

        byte[] buffer = new byte[4096];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        OutputStream os = socket.getOutputStream();

        int bytesRead;
        while ((bytesRead = bis.read(buffer)) > 0) {
            os.write(buffer, 0, bytesRead);
        }

        System.out.println("File sent.");

        bis.close();
        socket.close();
        serverSocket.close();
    }
}
