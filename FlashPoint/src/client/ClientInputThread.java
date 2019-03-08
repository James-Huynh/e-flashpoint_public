package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;

/**
 * Ã¥Â®Â¢Ã¦Ë†Â·Ã§Â«Â¯Ã¨Â¯Â»Ã¦Â¶Ë†Ã¦ï¿½Â¯Ã§ÂºÂ¿Ã§Â¨â€¹
 * 
 * @author way
 * 
 */
public class ClientInputThread extends Thread {
	private Socket socket;
	public TranObject msg;
	private boolean isStart = true;
	private ObjectInputStream ois;
	private MessageListener messageListener;// Ã¦Â¶Ë†Ã¦ï¿½Â¯Ã§â€ºâ€˜Ã¥ï¿½Â¬Ã¦Å½Â¥Ã¥ï¿½Â£Ã¥Â¯Â¹Ã¨Â±Â¡

	public ClientInputThread(Socket socket) {
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ã¦ï¿½ï¿½Ã¤Â¾â€ºÃ§Â»â„¢Ã¥Â¤â€“Ã©Æ’Â¨Ã§Å¡â€žÃ¦Â¶Ë†Ã¦ï¿½Â¯Ã§â€ºâ€˜Ã¥ï¿½Â¬Ã¦â€“Â¹Ã¦Â³â€¢
	 * 
	 * @param messageListener
	 *            Ã¦Â¶Ë†Ã¦ï¿½Â¯Ã§â€ºâ€˜Ã¥ï¿½Â¬Ã¦Å½Â¥Ã¥ï¿½Â£Ã¥Â¯Â¹Ã¨Â±Â¡
	 */
	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	
	public boolean readMessage() throws IOException, ClassNotFoundException {
		boolean flag = false;
		System.out.println("Reading on Client side Started");
		Object readObject = ois.readObject();// 锟斤拷锟斤拷锟叫讹拷取锟斤拷锟斤拷
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;// 转锟斤拷锟缴达拷锟斤拷锟斤拷锟�
			switch(read_tranObject.getType()) {
			case LOGINSUCCESS:
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				msg = (TranObject) ois.readObject();
				// Ã¦Â¯ï¿½Ã¦â€�Â¶Ã¥Ë†Â°Ã¤Â¸â‚¬Ã¦ï¿½Â¡Ã¦Â¶Ë†Ã¦ï¿½Â¯Ã¯Â¼Å’Ã¥Â°Â±Ã¨Â°Æ’Ã§â€�Â¨Ã¦Å½Â¥Ã¥ï¿½Â£Ã§Å¡â€žÃ¦â€“Â¹Ã¦Â³â€¢Ã¯Â¼Å’Ã¥Â¹Â¶Ã¤Â¼Â Ã¥â€¦Â¥Ã¨Â¯Â¥Ã¦Â¶Ë†Ã¦ï¿½Â¯Ã¥Â¯Â¹Ã¨Â±Â¡Ã¯Â¼Å’Ã¥Â¤â€“Ã©Æ’Â¨Ã¥Å“Â¨Ã¥Â®Å¾Ã§Å½Â°Ã¦Å½Â¥Ã¥ï¿½Â£Ã§Å¡â€žÃ¦â€“Â¹Ã¦Â³â€¢Ã¦â€”Â¶Ã¯Â¼Å’Ã¥Â°Â±Ã¥ï¿½Â¯Ã¤Â»Â¥Ã¥ï¿½Å Ã¦â€”Â¶Ã¥Â¤â€žÃ§ï¿½â€ Ã¤Â¼Â Ã¥â€¦Â¥Ã§Å¡â€žÃ¦Â¶Ë†Ã¦ï¿½Â¯Ã¥Â¯Â¹Ã¨Â±Â¡Ã¤Âºâ€ 
				messageListener.Message(msg);
			}
			ois.close();
			if (socket != null&&!socket.isClosed())
				socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
