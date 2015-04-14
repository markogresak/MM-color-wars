package mm.graph;

public class SendInfinite implements Runnable {

    private final SocketServer ws;
    private final String message;

    public SendInfinite(SocketServer ws, String message) {
        this.ws = ws;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ws.sendToAll(message);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
