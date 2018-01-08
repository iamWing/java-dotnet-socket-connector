# Java <-> .Net Socket Connector

A Java library that helps to make TCP/IP connections between a Java & .Net
socket.

PS: This has nothing to do with WebSocket / Socket.IO. At least not atm.

## Getting start

Obviously this library only works on the Java application (Android as well if
it must be said), and for sure a .Net application that accepts socket
connection is needed.

The codec used to encode and decode the message must be the same in both Java &
.Net application. Default codec used in the library is ASCII. Codec from
StandardCharset is recommended.

For more details, please refer to the Javadoc. I actually don't have much time
to write the instructions yet.

## Version history

__v0.1.1 *pre-release*__

- Fixed issue of unhandled exception may be thrown when invoking 
`readString(int, String)`
- _Change_ Flush buffer after data sent

__v0.1.0 *pre-release*__

- Implemented connection between Java client socket & .Net server socket

## License & copyright

Copyright (c) 2017 Wing Chau
<br />
All rights reserved.

This software may be modified and distributed under the terms
of the MIT license. See the LICENSE file for details.
