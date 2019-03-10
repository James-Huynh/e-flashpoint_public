package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;

public class ClientManager {
	
	private ClientInputThread inputThread;
	private ClientOutputThread outputThread;
//	private ObjectInputStream ois;
//	private ObjectOutputStream oos;
	
	public ClientManager(ClientInputThread input, ClientOutputThread output) {
		this.inputThread = input;
		this.outputThread = output;
		
	}
	
	public boolean readMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		boolean flag = false;
		System.out.println("Reading on Client side Started");
		Object readObject = ois.readObject();
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;
			switch(read_tranObject.getType()) {
			case SUCCESS:
				System.out.println("Succesuful return");
				System.out.println(read_tranObject.getType());
				User xyz = (User) read_tranObject.getObject();
				System.out.println(xyz.getId());
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public boolean connectionRequest(User client) {
		System.out.println("Getting here");
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.CONNECT);
		objectToSend.setObject(client);
		outputThread.setMsg(objectToSend);
		return false;
	}
	
}
