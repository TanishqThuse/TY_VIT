import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class HelloUDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket dataSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName("localhost");
        Scanner userScanner = new Scanner(System.in);

        System.out.println("Hello Client Started. Type messages (bye to exit):");

        while (true) {
            System.out.print("You: ");
            String msgText = userScanner.nextLine();
            byte[] sendBytes = msgText.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, serverIP, 5001);
            dataSocket.send(sendPacket);

            if (msgText.trim().equalsIgnoreCase("bye")) {
                break;
            }

            byte[] recvBytes = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvBytes, recvBytes.length);
            dataSocket.receive(recvPacket);
            String reply = new String(recvPacket.getData(), 0, recvPacket.getLength());
            System.out.println("Server says: " + reply);
        }
        dataSocket.close();
        userScanner.close();
    }
}
