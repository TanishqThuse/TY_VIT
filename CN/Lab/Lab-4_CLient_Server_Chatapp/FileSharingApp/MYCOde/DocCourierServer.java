import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DocCourierServer {

    private static final int DOORWAY = 5000;

    public static void main(String[] args) {
        ServerSocket hub = null;
        Socket clientLink = null;
        BufferedInputStream fileFeed = null;
        OutputStream netPipe = null;

        try {
            hub = new ServerSocket(DOORWAY);
            System.out.println("[srv] Ready on port " + DOORWAY + ", waiting for pickup...");

            clientLink = hub.accept();
            System.out.println("[srv] Client connected: " + clientLink.getRemoteSocketAddress());

            File parcel = new File("D:/Backup/Tanishq/VIT/VIT_File/TY_VIT/CN/Lab/Lab-4_CLient_Server_Chatapp/fileToShare.txt");
            if (!parcel.exists() || !parcel.isFile()) {
                System.out.println("[srv] Missing parcel: " + parcel.getAbsolutePath());
                return;
            }

            fileFeed = new BufferedInputStream(new FileInputStream(parcel));
            netPipe = clientLink.getOutputStream();

            byte[] packet = new byte[4096];
            int n;
            while ((n = fileFeed.read(packet)) != -1) {
                netPipe.write(packet, 0, n);
            }
            netPipe.flush();

            System.out.println("[srv] Parcel dispatched.");
        } catch (IOException e) {
            System.out.println("[srv] Problem: " + e.getMessage());
        } finally {
            closeQuietly(fileFeed);
            closeQuietly(netPipe);
            closeQuietly(clientLink);
            closeQuietly(hub);
        }
    }

    private static void closeQuietly(AutoCloseable c) {
        if (c != null) {
            try { c.close(); } catch (Exception ignored) {}
        }
    }
}
