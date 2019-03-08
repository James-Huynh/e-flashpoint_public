package client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * å®¢æˆ·ç«¯
 * 
 * @author way
 * 
 */
public class Client {

	private Socket socket;
	private ClientThread clientThread;
	private String ip;
	private int port;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public boolean start() {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 3000);
			if (socket.isConnected()) {
				clientThread = new ClientThread(socket);
				clientThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// ç›´æŽ¥é€šè¿‡clientå¾—åˆ°è¯»çº¿ç¨‹
	public ClientInputThread getClientInputThread() {
		return clientThread.getIn();
	}

	// ç›´æŽ¥é€šè¿‡clientå¾—åˆ°å†™çº¿ç¨‹
	public ClientOutputThread getClientOutputThread() {
		return clientThread.getOut();
	}

	// ç›´æŽ¥é€šè¿‡clientå�œæ­¢è¯»å†™æ¶ˆæ�¯
	public void setIsStart(boolean isStart) {
		clientThread.getIn().setStart(isStart);
		clientThread.getOut().setStart(isStart);
	}
	
	public class ClientThread extends Thread {

		private ClientInputThread in;
		private ClientOutputThread out;

		public ClientThread(Socket socket) {
			in = new ClientInputThread(socket);
			out = new ClientOutputThread(socket);
		}

		public void run() {
			in.setStart(true);
			out.setStart(true);
			in.start();
			out.start();
		}

		// å¾—åˆ°è¯»æ¶ˆæ�¯çº¿ç¨‹
		public ClientInputThread getIn() {
			return in;
		}

		// å¾—åˆ°å†™æ¶ˆæ�¯çº¿ç¨‹
		public ClientOutputThread getOut() {
			return out;
		}
	}
	public static void main(String args[]) {
		Client ccc=new Client("142.157.104.187", 8888);
		 ccc.start();
	}
}
