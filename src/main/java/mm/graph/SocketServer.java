package mm.graph;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collection;

public class SocketServer extends WebSocketServer {

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    public SocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    public void sendToAll(String text) {
        Collection<WebSocket> con = this.connections();
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }
}

