/*
 * Copyright (c) 2017 Wing Chau
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.devtography.socket.javadotnet;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * This class provides the APIs to connect to a
 * socket server hosting by a .Net program.
 * <p>
 * Applications that need to create a connection
 * to a .Net socket server must initial an
 * instance of this class by calling
 * {@link #init(String, int)}, then open the
 * connection by calling {@link #connect()}.
 * {@link #disconnect()} should always be called
 * to close the input/output streams & the socket
 * to release system resources associated.
 */
public class ClientSocket {

    private String remoteAddr = "";
    private int port;

    private Socket socket;
    private BufferedInputStream in;
    private OutputStream out;

    // listeners
    private OnConnectionCreatedListener
            onConnectionCreatedListener;
    private OnReadStringCompleteListener
            onReadStringCompleteListener;

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
     * Returns the connection status of the socket.
     *
     * @return true if socket is connected to the
     * server, false if it is not.
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * Connects to remote socket server & opens the
     * input & output streams.
     * <p>
     * {@link #disconnect()} should be called when
     * there is no use of the connection anymore.
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

        // notifies the listener that the connection
        // has been established
        onConnectionCreatedListener.onConnected();
    }

    /**
     * Closes the input & output streams, then closes
     * the socket to release any system resources
     * associated.
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
     * The method then passes the message to the
     * {@link OnReadStringCompleteListener} without the
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
    public void readString(final int bufferSize,
                           final String delimiter)
            throws IOException {
        StringBuilder msg = new StringBuilder();
        byte[] buffer = new byte[bufferSize];

        // while the delimiter does exist in
        // the received message
        while (msg.lastIndexOf(delimiter) == -1) {
            int bytesRead = in.read(buffer);

            if (bytesRead > -1)
                msg.append(new String(buffer, 0, bytesRead));
        }

        // passes message string to listener without
        // the delimiter
        onReadStringCompleteListener.onComplete(
                msg.substring(0, msg.lastIndexOf(delimiter)));
    }

    /**
     * Writes string to the socket server. The
     * encoding codec used here must match the
     * codec used in the .Net socket server to
     * retrieve the string.
     * <p>
     * Codec from {@link StandardCharsets} is
     * recommended. The default codec is ASCII.
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
        Charset encoding = Optional.ofNullable(codec)
                .orElse(StandardCharsets.US_ASCII);

        byte[] toSend = str.getBytes(encoding);
        out.write(toSend);
        out.flush();
    }

    /* Listener setters */

    /**
     * Setter of {@link OnConnectionCreatedListener}.
     *
     * @param listener instance of
     *                 {@link OnConnectionCreatedListener}.
     */
    public void setOnConnectionCreatedListener(
            OnConnectionCreatedListener listener) {
        onConnectionCreatedListener = listener;
    }

    /**
     * Setter of {@link OnReadStringCompleteListener}.
     *
     * @param listener instance of
     *                 {@link OnReadStringCompleteListener}.
     */
    public void setOnReadStringCompleteListener(
            OnReadStringCompleteListener listener) {
        onReadStringCompleteListener = listener;
    }

    /* Listener interfaces */

    /**
     * Interface definition for a callback to be
     * invoked when the socket is connected to a
     * socket server.
     */
    public interface OnConnectionCreatedListener {

        /**
         * On socket connected to server
         * successfully.
         */
        void onConnected();
    }

    /**
     * Interface definition for a callback to be
     * invoked when the whole string message is
     * received when using
     * {@link #readString(int, String)}.
     */
    public interface OnReadStringCompleteListener {

        /**
         * On completed receives message.
         *
         * @param received the received string.
         */
        void onComplete(String received);
    }
}
