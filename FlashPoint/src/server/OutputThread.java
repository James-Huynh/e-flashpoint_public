package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import commons.tran.bean.TranObject;


public class OutputThread extends Thread {
	private OutputThreadMap map;
	private ObjectOutputStream oos;
	private TranObject object;
	private boolean isStart = true;// ѭ����־λ
	private Socket socket;

	public OutputThread(Socket socket, OutputThreadMap map) {
		try {
			this.socket = socket;
			this.map = map;
			oos = new ObjectOutputStream(socket.getOutputStream());// �ڹ���������ʵ�������������
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	// ����д��Ϣ�̣߳���������Ϣ֮�󣬻���run���������Խ�Լ��Դ
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
				// û����Ϣд����ʱ���̵߳ȴ�
				synchronized (this) {
					wait();
				}
				if (object != null) {
					oos.writeObject(object);
					oos.flush();
				}
			}
			if (oos != null)// ѭ�������󣬹ر������ͷ���Դ
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
