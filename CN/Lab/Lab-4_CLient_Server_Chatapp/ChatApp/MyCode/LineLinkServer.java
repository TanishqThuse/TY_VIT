import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LineLinkServer {

    // keep port same on both sides
    private static final int PORT_GATE = 5000;

    public static void main(String[] argv) {
        ServerSocket gate = null;
        Socket peer = null;
        BufferedReader netIn = null;
        PrintWriter netOut = null;
        BufferedReader userIn = null;

        try {
            gate = new ServerSocket(PORT_GATE);
            System.out.println("[server] gate open on " + PORT_GATE + ", waiting...");

            peer = gate.accept();
            System.out.println("[server] peer online: " + peer.getRemoteSocketAddress());

            netIn = new BufferedReader(new InputStreamReader(peer.getInputStream()));
            netOut = new PrintWriter(peer.getOutputStream(), true);
            userIn = new BufferedReader(new InputStreamReader(System.in));

            final Socket peerRef = peer;
            final ServerSocket gateRef = gate;
            final BufferedReader netInRef = netIn;

            Thread inboundPump = new Thread(() -> {
                try {
                    String line;
                    while ((line = netInRef.readLine()) != null) {
                        if ("bye".equalsIgnoreCase(line.trim())) {
                            System.out.println("[server] peer said bye. closing.");
                            safeClose(peerRef);
                            safeClose(gateRef);
                            System.exit(0);
                        }
                        System.out.println("[client] " + line);
                    }
                } catch (IOException ex) {
                    System.out.println("[server] link down.");
                } finally {
                    safeClose(peerRef);
                    safeClose(gateRef);
                }
            }, "inbound-pump");
            inboundPump.setDaemon(true);
            inboundPump.start();

            String outbound;
            while ((outbound = userIn.readLine()) != null) {
                netOut.println(outbound);
                if ("bye".equalsIgnoreCase(outbound.trim())) {
                    System.out.println("[server] session ended.");
                    break;
                }
            }
        } catch (IOException ioe) {
            System.out.println("[server] error: " + ioe.getMessage());
        } finally {
            safeClose(netIn);
            if (netOut != null) netOut.close();
            safeClose(userIn);
            safeClose(peer);
            safeClose(gate);
        }
    }

    private static void safeClose(ServerSocket s) {
        if (s != null) {
            try { s.close(); } catch (IOException ignored) {}
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
