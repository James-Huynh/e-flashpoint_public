package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import commons.tran.bean.TranObject;

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
	ClientManager clientManager;

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
	
	//JUNHA : This method was removed to ClientManager
	
//	  public boolean readMessage() throws IOException, ClassNotFoundException {
//		  boolean flag = false; 
//		  System.out.println("Reading on Client side Started");
//		  Object readObject = ois.readObject();
//		  if (readObject !=null && readObject instanceof TranObject) { 
//			  TranObject read_tranObject =(TranObject) readObject;
//			  switch(read_tranObject.getType()){ 
//			  case SUCCESS: System.out.println("Succesuful return");
//			  System.out.println(read_tranObject.getType()); 
//			  User xyz = (User)read_tranObject.getObject(); 
//			  System.out.println(xyz.getId()); 
//			  flag = true;
//			  break; 
//			  } 
//			 } 
//		  return flag; 
//		  }
	public ObjectInputStream getInputStream() {
		return ois;
	}
	
	public Object readInputStream() {
		Object giveBack = null;
		try{
			giveBack = ois.readObject();
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k){
			
		}
		return giveBack;
	}

	@Override
	public void run() {
//		clientManager = new ClientManager();
		
		try {
			while (isStart) {
			//	System.out.println("enter clientInputThread");
			//	clientManager.readMessage(ois);
			}
			ois.close();
			if (socket != null&&!socket.isClosed())
				socket.close();
		}	catch(Exception e) {
			e.printStackTrace();
		}
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
