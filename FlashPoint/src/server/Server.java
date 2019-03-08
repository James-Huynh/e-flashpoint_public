package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import commons.util.Constants;

import commons.util.MyDate;


public class Server {
	private ExecutorService executorService;// �̳߳�
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private boolean isStarted = true;

	public Server() {
		try {
			// �����̳߳أ����о���(cpu����*50)���߳�
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors() * 50);
			serverSocket = new ServerSocket(Constants.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
			quit();
		}
	}

	public void start() {
		System.out.println(MyDate.getDateCN() + " ������������...");
	 Constants.questionnum=0;
		try {
			while (isStarted) {
				socket = serverSocket.accept();
				String ip = socket.getInetAddress().toString();
				System.out.println(MyDate.getDateCN() + " �û���" + ip + " �ѽ�������");
				// Ϊ֧�ֶ��û��������ʣ������̳߳ع���ÿһ���û�����������
				if (socket.isConnected())
					executorService.execute(new SocketTask(socket));// ��ӵ��̳߳�
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

		public SocketTask(Socket socket) {
			this.socket = socket;
			map = OutputThreadMap.getInstance();
		}

		@Override
		public void run() {
			out = new OutputThread(socket, map);//
			// ��ʵ��д��Ϣ�߳�,���Ѷ�Ӧ�û���д�̴߳���map�������У�
			in = new ServerInputThread(socket, out, map);// ��ʵ�����Ϣ�߳�
			out.setStart(true);
			in.setStart(true);
			in.start();
			out.start();
		}
	}

	/**
	 * �˳�
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
