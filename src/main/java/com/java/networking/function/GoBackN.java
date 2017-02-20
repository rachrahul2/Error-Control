package com.java.networking.function;

import com.java.networking.main.MainUI;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GoBackN extends AbstractMethod {

    int GBN_WINDOW_SIZE = 3;
    int VERSION_SIZE = 4;
    int PACKET_SIZE = 1024;
    int LAST_VERSION = 99999;
    int VERSION = 1;
    private final Random r = new Random(System.currentTimeMillis());
    private final int window;
    private final List<byte[]> packets = new LinkedList<>();

    public GoBackN(int loss, MainUI f) {
        super(loss, f);
        this.window = GBN_WINDOW_SIZE;
    }

    @Override
    public void start(String host, int port, InputStream inputStream) {
        f.appendMessage("Sending file by GoBackN method...");
        packets.clear();
        long start = System.currentTimeMillis();
        int sent = 0;
        int lost = 0;
        int v = VERSION;
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(1500);
            while (true) {
                while (packets.size() < window) {
                    byte[] data = new byte[PACKET_SIZE];
                    data = ContentHandle.read(inputStream, data,
                            VERSION_SIZE);
                    if (data == null) {
                        break;
                    }
                    packets.add(data);
                }

                if (packets.size() == 0) {
                    packets.add(new byte[VERSION_SIZE]);
                    v = LAST_VERSION;
                }

                for (int i = 0; i < packets.size(); i++) {
                    byte[] data = packets.get(i);
                    int version = v + i;
                    byte[] vb = ContentHandle.before(version);
                    System.arraycopy(vb, 0, data, 0,
                            vb.length);
                    int length = data.length;
                    sent += length;

                    int randomNumber = r.nextInt(100);
                    if (randomNumber < loss) {
                        lost += length;
                        f.appendMessage("Lost package " + version);
                    } else {
                        f.appendMessage("Sending package " + version + " (" + length + ")");
                        InetAddress ipAddress = InetAddress.getByName(host);
                        DatagramPacket sendPacket = new DatagramPacket(data,
                                length, ipAddress, port);
                        socket.send(sendPacket);
                    }
                }

                int maxV = Integer.MIN_VALUE;
                byte[] responseData = new byte[VERSION_SIZE];
                DatagramPacket responsePacket = new DatagramPacket(responseData,
                        responseData.length);
                while (true) {
                    try {
                        socket.receive(responsePacket);
                        responseData = responsePacket.getData();
                        int rV = ContentHandle.after(responseData);
                        if (rV > maxV) {
                            maxV = rV;
                        }
                    } catch (IOException e) {
                        break;
                    }
                }

                if (maxV == LAST_VERSION) {
                    break;
                }

                for (; v <= maxV; v++) {
                    packets.remove(0);
                }
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
