import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LineLinkClient {

    private static final String HOST_ALIAS = "localhost";
    private static final int PORT_GATE = 5000;

    public static void main(String[] args) {
        Socket wire = null;
        BufferedReader netIn = null;
        PrintWriter netOut = null;
        BufferedReader userIn = null;

        try {
            wire = new Socket(HOST_ALIAS, PORT_GATE);
            System.out.println("[client] connected to " + HOST_ALIAS + ":" + PORT_GATE);

            netIn = new BufferedReader(new InputStreamReader(wire.getInputStream()));
            netOut = new PrintWriter(wire.getOutputStream(), true);
            userIn = new BufferedReader(new InputStreamReader(System.in));

            final Socket wireRef = wire;
            final BufferedReader netInRef = netIn;

            Thread rxLoop = new Thread(() -> {
                try {
                    String incoming;
                    while ((incoming = netInRef.readLine()) != null) {
                        if ("bye".equalsIgnoreCase(incoming.trim())) {
                            System.out.println("[client] server said bye. closing.");
                            safeClose(wireRef);
                            System.exit(0);
                        }
                        System.out.println("[server] " + incoming);
                    }
                } catch (IOException e) {
                    System.out.println("[client] link closed.");
                } finally {
                    safeClose(wireRef);
                }
            }, "client-receiver");
            rxLoop.setDaemon(true);
            rxLoop.start();

            String outLine;
            while ((outLine = userIn.readLine()) != null) {
                netOut.println(outLine);
                if ("bye".equalsIgnoreCase(outLine.trim())) {
                    System.out.println("[client] session ended.");
                    break;
                }
            }

        } catch (IOException ioe) {
            System.out.println("[client] error: " + ioe.getMessage());
        } finally {
            safeClose(netIn);
            if (netOut != null) netOut.close();
            safeClose(userIn);
            safeClose(wire);
        }
    }

    private static void safeClose(Socket s) {
        if (s != null) {
            try { s.close(); } catch (IOException ignored) {}
        }
    }
    private static void safeClose(BufferedReader r) {
        if (r != null) {
            try { r.close(); } catch (IOException ignored) {}
        }
    }
}
