import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HelloUDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket msgSocket = new DatagramSocket(5001);
        byte[] receiveData = new byte[1024];
        byte[] sendData;

        System.out.println("Hello Server Active. Waiting for messages...");

        while (true) {
            DatagramPacket recvPacket = new DatagramPacket(receiveData, receiveData.length);
            msgSocket.receive(recvPacket);
            String clientMsg = new String(recvPacket.getData(), 0, recvPacket.getLength());

            System.out.println("Client says: " + clientMsg);

            if (clientMsg.trim().equalsIgnoreCase("bye")) {
                System.out.println("Closing Server...");
                break;
            }

            String replyText = "Hello Client";
            sendData = replyText.getBytes();
            InetAddress clientAddr = recvPacket.getAddress();
            int clientPort = recvPacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddr, clientPort);
            msgSocket.send(sendPacket);
        }
        msgSocket.close();
    }
}
