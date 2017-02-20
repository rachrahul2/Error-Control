package com.java.networking.function;

import com.java.networking.main.MainUI;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class NetworkReceiver implements Runnable {

    int VERSION_SIZE = 4;
    int PACKET_SIZE = 1024;
    int VERSION_MAX = 99999;
    int GBN_WINDOW_SIZE = 3;
    int VERSION = 1;
    private final int port;
    private final MainUI f;
    private int nextVersion = VERSION;

    public NetworkReceiver(int port, MainUI f) {
        this.f = f;
        this.port = port;
        new Thread(this).start();
    }

    @Override
    public void run() {
        f.appendMessage("Receiving...");
        byte[] data = new byte[PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try (OutputStream outputStream = new FileOutputStream(
                "COSC635_P2_DataRecieved.txt");
                DatagramSocket socket = new DatagramSocket(port);) {
            socket.setSoTimeout(1000);
            for (;;) {
                try {
                    socket.receive(packet);
                    byte[] b = Arrays.copyOf(packet.getData(), packet.getLength());
                    ContentHandle content = new ContentHandle(b);

                    int v = content.getVersion();
                    if (v == VERSION_MAX) {
                        f.appendMessage("Completed...");
                        nextVersion = VERSION;
                    } else if (v == nextVersion) {
                        f.appendMessage("Received packet " + nextVersion + " ("
                                + content.getData().length + ")");
                        outputStream.write(content.getData());
                        nextVersion++;
                    } else {
                        f.appendMessage("Received unknown packet " + v);
                        v = nextVersion - 1;
                    }

                    byte[] responseData = ContentHandle.before(v);
                    InetAddress ip = packet.getAddress();
                    int port = packet.getPort();
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData, responseData.length, ip, port);
                    socket.send(responsePacket);

                    if (v == VERSION_MAX) {
                        break;
                    }
                } catch (Exception e) {

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
