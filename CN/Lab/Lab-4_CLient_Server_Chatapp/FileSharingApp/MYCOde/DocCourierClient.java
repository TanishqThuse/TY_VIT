import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DocCourierClient {

    private static final String HOST_ADDR = "localhost";
    private static final int DOORWAY = 5000;

    public static void main(String[] args) {
        Socket link = null;
        InputStream netFeed = null;
        BufferedOutputStream savePipe = null;

        try {
            link = new Socket(HOST_ADDR, DOORWAY);
            System.out.println("[cli] Connected to " + HOST_ADDR + ":" + DOORWAY);

            netFeed = link.getInputStream();
            savePipe = new BufferedOutputStream(new FileOutputStream("receivedfile.txt"));

            byte[] block = new byte[4096];
            int n;
            while ((n = netFeed.read(block)) != -1) {
                savePipe.write(block, 0, n);
            }
            savePipe.flush();

            System.out.println("[cli] Parcel received & stored.");
        } catch (IOException e) {
            System.out.println("[cli] Problem: " + e.getMessage());
        } finally {
            closeQuietly(savePipe);
            closeQuietly(netFeed);
            closeQuietly(link);
        }
    }

    private static void closeQuietly(AutoCloseable c) {
        if (c != null) {
            try { c.close(); } catch (Exception ignored) {}
        }
    }
}
