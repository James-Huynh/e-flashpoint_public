package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import commons.tran.bean.TranObject;


public class OutputThread extends Thread {
	private OutputThreadMap map;
	private ObjectOutputStream oos;
	private TranObject object;
	private boolean isStart = true;// 循锟斤拷锟斤拷志位
	private Socket socket;

	public OutputThread(Socket socket, OutputThreadMap map) {
		try {
			this.socket = socket;
			this.map = map;
			oos = new ObjectOutputStream(socket.getOutputStream());// 锟节癸拷锟斤拷锟斤拷锟斤拷锟斤拷实锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}


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
				// 没锟斤拷锟斤拷息写锟斤拷锟斤拷时锟斤拷锟竭程等达拷
				synchronized (this) {
					wait();
				}
				if (object != null) {
					oos.flush();
					oos.reset();
					oos.writeObject(object);
					
				}
			}
			if (oos != null)// 循锟斤拷锟斤拷锟斤拷锟襟，关憋拷锟斤拷锟斤拷锟酵凤拷锟斤拷源
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
