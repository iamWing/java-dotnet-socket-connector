/*
 * Copyright (c) $year Wing Chau
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

public class SocketConnection {

    private String remoteAddr = "";
    private int port;

    /**
     * Initialises an instance of {@link SocketConnection}
     * and define the values of remote socket server
     * address & port number. Then returns the instance
     * initialised.
     *
     * @param remoteAddr IPv4 address of the remote socket
     *                   server
     * @param port       port number of the remote socket
     *                   server
     * @return instance of {@link SocketConnection}
     */
    public static SocketConnection init(String remoteAddr, int port) {
        SocketConnection instance = new SocketConnection();

        instance.remoteAddr = remoteAddr;
        instance.port = port;

        return instance;
    }

}
