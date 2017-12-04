/*
 * Copyright (c) $year Wing Chau
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocket {

    private String remoteAddr = "";
    private int port;

    private BufferedInputStream in;
    private OutputStream out;

    /**
     * Initialises an instance of {@link ClientSocket}
     * and define the values of remote socket server
     * address & port number. Then returns the instance
     * initialised.
     *
     * @param remoteAddr IPv4 address of the remote socket
     *                   server.
     * @param port       port number of the remote socket
     *                   server.
     * @return instance of {@link ClientSocket}.
     */
    public static ClientSocket init(String remoteAddr, int port) {
        ClientSocket instance = new ClientSocket();

        instance.remoteAddr = remoteAddr;
        instance.port = port;

        return instance;
    }

    /**
     * Connects to remote socket server & opens the
     * input & output streams.
     *
     * @throws IOException when connection fails.
     */
    public void connect() throws IOException {

        // creating a socket to connect to the server
        Socket socket = new Socket(remoteAddr, port);
        in = new BufferedInputStream(socket.getInputStream());
        out = socket.getOutputStream();
    }

    /**
     * Reads bytes from input stream.
     * <p>
     * A custom string delimiter is needed to determine
     * when does it complete receiving the whole message.
     * The method then returns the message without the
     * delimiter once the whole message was received.
     *
     * @param bufferSize size of the byte array buffer.
     * @param delimiter  a specific string that defines
     *                   the end of the message received.
     * @return the string message without the delimiter.
     * @throws IOException if the input stream has been
     *                     closed by invoking its close()
     *                     method, or an I/O error occurs.
     */
    public String readString(final int bufferSize,
                             final String delimiter)
            throws IOException {
        StringBuilder msg = new StringBuilder();
        byte[] buffer = new byte[bufferSize];

        while (msg.lastIndexOf(delimiter) == -1) {
            int bytesRead = in.read(buffer);

            msg.append(new String(buffer, 0, bytesRead));
        }

        // returns message string without the delimiter
        return msg.substring(0, msg.lastIndexOf(delimiter));
    }
}
