package server;

import java.io.IOException;
import java.io.ObjectInputStream;

import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;

public class ServerManager {
	
	
	public ServerManager() {
		
	}
	
	public void readMessage(OutputThread out, ObjectInputStream ois) throws IOException, ClassNotFoundException {
		System.out.println("server loginUser1234:");
		Object readObject = ois.readObject();
		System.out.println("Here?");

		if (readObject != null && readObject instanceof TranObject) {
			System.out.println("Entered IF");
			TranObject read_tranObject = (TranObject) readObject;// 转锟斤拷锟缴达拷锟斤拷锟斤拷锟�
			switch (read_tranObject.getType()) {
			case CONNECT:
				TranObject<User> register2TranObject = new TranObject<User>(TranObjectType.SUCCESS);
				User newUser = (User) read_tranObject.getObject();
				newUser.setId(12345);
				register2TranObject.setObject(newUser);
				out.setMessage(register2TranObject);
				break;
			}
		}
			
	}


}
