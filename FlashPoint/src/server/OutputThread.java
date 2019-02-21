package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 * output message
 * 
 * @author eric
 * 
 */
public class OutputThread extends Thread {
	private OutputThreadMap map;
	private ObjectOutputStream oos;
	private TranObject object;
	private boolean isStart = true;
	private Socket socket;

	public OutputThread(Socket socket, OutputThreadMap map) {
		try {
			this.socket = socket;
			this.map = map;
			oos = new ObjectOutputStream(socket.getOutputStream());//output stream
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	// Using the writing method and wake up run
	public void setMessage(TranObject object) {
		this.object = object;
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				// wait
				synchronized (this) {
					wait();
				}
				if (object != null) {
					oos.writeObject(object);
					oos.flush();
				}
			}
			if (oos != null)// 
				oos.close();
			if (socket != null)
				socket.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}