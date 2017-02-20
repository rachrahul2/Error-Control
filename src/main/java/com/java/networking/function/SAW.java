package com.java.networking.function;

import com.java.networking.main.MainUI;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class SAW extends AbstractMethod {

    int VERSION_SIZE = 4;
    int PACKET_SIZE = 1024;
    int LAST_VERSION = 99999;
    int VERSION = 1;

    private final Random r = new Random(System.currentTimeMillis());

    public SAW(int loss, MainUI f) {
        super(loss, f);
    }

    @Override
    public void start(String host, int port, InputStream inputStream) {
        f.appendMessage("Sending file by Stop and Wait method...");
        int ver = VERSION;
        int sent = 0;
        int lost = 0;
        long start = System.currentTimeMillis();
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(1000);
            byte[] data = new byte[PACKET_SIZE];
            while (true) {
                int size = inputStream.read(data, VERSION_SIZE,
                        data.length - VERSION_SIZE);
                if (size < 0) {
                    size = 0;
                    ver= LAST_VERSION;
                    data = new byte[VERSION_SIZE];
                }
                size += VERSION_SIZE;

                do {
                    byte[] vb = ContentHandle.before(ver);
                    System.arraycopy(vb, 0, data, 0,
                            vb.length);
                    sent += size;

                    int randomNumber = r.nextInt(100);
                    if (randomNumber < loss) {
                        lost += size;
                        f.appendMessage("Lost package " + ver);
                    } else {
                        f.appendMessage("Sending package " + ver + " (" + size + ")");
                        InetAddress ip = InetAddress.getByName(host);
                        DatagramPacket packet = new DatagramPacket(data,
                                size, ip, port);
                        socket.send(packet);
                    }

                    byte[] responseData = new byte[VERSION_SIZE];
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData, responseData.length);
                    try {
                        socket.receive(responsePacket);
                        int responseV = ContentHandle.after(responseData);
                        if (responseV == ver) {
                            break;
                        }
                    } catch (IOException e) {
                        ;
                    }
                } while (true);

                if (ver == LAST_VERSION) {
                    break;
                }
                ver++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        f.appendMessage("Time Taken to send: " + (end - start) + "ms");
        f.appendMessage("Sent: " + sent + " bytes");
        f.appendMessage("Lost: " + lost + " bytes");
    }
}
