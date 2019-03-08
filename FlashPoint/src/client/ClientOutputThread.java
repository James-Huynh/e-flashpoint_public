package client;

import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientOutputThread extends Thread {
	private Socket socket;
	private ObjectOutputStream oos;
	private boolean isStart = true;
	private TranObject msg;

	public ClientOutputThread(Socket socket) {
		this.socket = socket;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	// è¿™é‡Œå¤„ç�†è·Ÿæœ�åŠ¡å™¨æ˜¯ä¸€æ ·çš„
	public void setMsg(TranObject msg) {
		this.msg = msg;
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				if (msg != null) {
					System.out.println("msg:"+msg);
					oos.writeObject(msg);
					oos.flush();
					if (msg.getType() == TranObjectType.LOGOUT) {
						// å¦‚æžœæ˜¯å�‘é€�ä¸‹çº¿çš„æ¶ˆæ�¯ï¼Œå°±ç›´æŽ¥è·³å‡ºå¾ªçŽ¯
						break;
					}
					synchronized (this) {
						wait();// å�‘é€�å®Œæ¶ˆæ�¯å�Žï¼Œçº¿ç¨‹è¿›å…¥ç­‰å¾…çŠ¶æ€�
					}
				}
			}
			oos.close();// å¾ªçŽ¯ç»“æ�Ÿå�Žï¼Œå…³é—­è¾“å‡ºæµ�å’Œsocket
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
