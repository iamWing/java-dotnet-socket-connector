/*
 * Copyright (c) $year Wing Chau
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

import com.sun.istack.internal.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ClientSocket {

    private String remoteAddr = "";
    private int port;

    private Socket socket;
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
     * @throws UnknownHostException if the IP address of
     *                              the host could not be
     *                              determined.
     * @throws IOException          if an I/O error occurs
     *                              when creating the
     *                              socket.
     */
    public void connect() throws IOException {

        // creating a socket to connect to the server
        socket = new Socket(remoteAddr, port);
        in = new BufferedInputStream(socket.getInputStream());
        out = socket.getOutputStream();
    }

    /**
     * Closes the input & output streams, then closes
     * the socket.
     *
     * @throws IOException if an I/O error occurs when
     *                     closing the input/output stream
     *                     or the socket.
     */
    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
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

        // while the delimiter does exist in
        // the received message
        while (msg.lastIndexOf(delimiter) == -1) {
            int bytesRead = in.read(buffer);

            msg.append(new String(buffer, 0, bytesRead));
        }

        // returns message string without the delimiter
        return msg.substring(0, msg.lastIndexOf(delimiter));
    }

    /**
     * Writes string to the socket server. The encoding
     * used here must match the encoding used in the
     * .Net socket server to retrieve the string.
     * Encoding from {@link StandardCharsets} is
     * recommended. The default encoding is ASCII.
     *
     * @param str   string that will be sent to
     *              the server.
     * @param codec encoding that will be used to
     *              convert the string to byte
     *              array. if null, ASCII codec
     * @throws IOException if an I/O error occurs.
     */
    public void writeString(final String str, @Nullable final Charset codec)
            throws IOException {
        Charset encoding = Optional.of(codec)
                .orElse(StandardCharsets.US_ASCII);

        byte[] toSend = str.getBytes(encoding);
        out.write(toSend);
    }

}
