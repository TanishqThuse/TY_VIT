import java.io.*;
import java.net.*;

public class ClientFileTransfer {
    public static void main(String[] args) throws IOException {
        // Connect to the server on localhost and port 5000
        Socket socket = new Socket("localhost", 5000);

        byte[] buffer = new byte[4096];
        InputStream is = socket.getInputStream();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("receivedfile.txt"));

        int bytesRead;
        while ((bytesRead = is.read(buffer)) > 0) {
            bos.write(buffer, 0, bytesRead);
        }

        System.out.println("File received.");

        bos.close();
        socket.close();
    }
}
