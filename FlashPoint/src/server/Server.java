package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import commons.util.Constants;
import commons.util.MyDate;


public class Server {
	private ExecutorService executorService;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private boolean isStarted = true;
	ServerManager serverManager;

	public Server() {
		try {
		
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors() * 50);
			serverSocket = new ServerSocket(Constants.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
			quit();
		}
	}

	public void start() {
		System.out.println(MyDate.getDateCN() + " server online...");
	 Constants.questionnum=0;
	 	serverManager = new ServerManager();
		try {
			while (isStarted) {
				socket = serverSocket.accept();
				String ip = socket.getInetAddress().toString();
				System.out.println(MyDate.getDateCN() + " user" + ip + " connected");
				// 为支锟街讹拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟绞ｏ拷锟斤拷锟斤拷锟竭程池癸拷锟斤拷每一锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
				if (socket.isConnected())
					executorService.execute(new SocketTask(socket, serverManager));// 锟斤拷拥锟斤拷叱坛锟�
			}
			if (socket != null)
				socket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			// isStarted = false;
		}
	}

	private final class SocketTask implements Runnable {
		private Socket socket = null;
		private ServerInputThread in;
		private OutputThread out;
		private OutputThreadMap map;
		private ServerManager serverManager;

		public SocketTask(Socket socket, ServerManager newServerManager) {
			this.socket = socket;
			map = OutputThreadMap.getInstance();
			this.serverManager = newServerManager;
		}

		@Override
		public void run() {
			out = new OutputThread(socket, map);//
			// 锟斤拷实锟斤拷写锟斤拷息锟竭筹拷,锟斤拷锟窖讹拷应锟矫伙拷锟斤拷写锟竭程达拷锟斤拷map锟斤拷锟斤拷锟斤拷锟叫ｏ拷
			in = new ServerInputThread(socket, out, map, serverManager);// 锟斤拷实锟斤拷锟斤拷锟较拷叱锟�
			out.setStart(true);
			in.setStart(true);
			in.start();
			out.start();
		}
	}

	/**
	 * 锟剿筹拷
	 */
	public void quit() {
		try {
			this.isStarted = false;
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();
	}
}
