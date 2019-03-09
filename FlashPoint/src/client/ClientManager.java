package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import commons.bean.User;
import commons.tran.bean.TranObject;

public class ClientManager {
	
	private ObjectInputStream ois;
	
	public ClientManager() {
		
	}
	
	public boolean readMessage() throws IOException, ClassNotFoundException {
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
	
}
