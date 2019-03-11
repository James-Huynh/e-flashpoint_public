package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import game.GameState;

public class ClientManager {
	
	private ClientInputThread inputThread;
	private ClientOutputThread outputThread;
	private User client; 
//	private ObjectInputStream ois;
//	private ObjectOutputStream oos;
	
	public ClientManager(ClientInputThread input, ClientOutputThread output) {
		this.inputThread = input;
		this.outputThread = output;
		
	}
	
	public boolean readMessage() throws IOException, ClassNotFoundException {
		boolean flag = false;
		System.out.println("Reading on Client side Started");
		Object readObject = inputThread.readInputStream();
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;
			switch(read_tranObject.getType()) {
			case SUCCESS:
				System.out.println("Succesuful return");
				System.out.println(read_tranObject.getType());
				client = (User) read_tranObject.getObject();
				flag = true;
				break;
			case LOGINSUCCESS:
				System.out.println("Succesuful login request");
				client = (User) read_tranObject.getObject();
				flag = true;
				break;
			case REGISTERSUCCESS:
				System.out.println("Succesuful login request");
				client = (User) read_tranObject.getObject();
				flag = true;
				break;
				
			}
		}
		return flag;
	}
	
	public boolean connectionRequest(User inputClient) {
		boolean flag = false;
		System.out.println("Getting here");
		client = inputClient;
		System.out.println(client.getId());
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.CONNECT);
		objectToSend.setObject(client);
		outputThread.setMsg(objectToSend);
		System.out.println("check!");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		System.out.println("|2|" + client.getId()); 
//		System.out.println("|3|" + client.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		return flag;
	}

	public GameState gameStateRequest(User userOne) {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.GAMESTATEUPDATE);
		objectToSend.setObject(client);
		outputThread.setMsg(objectToSend);
		System.out.println("test check");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		System.out.println("|3|" + client.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		return client.getCurrentState();
		
	}

	public boolean loginRequest(String username, char[] password) {
		boolean flag = false;
		client.setName(username);
		client.setPassword(password.toString());
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOGIN);
		objectToSend.setObject(client);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			if(client.getIsOnline() == 1) {
				flag = true;
			}
			//if client.getIsOnline() == 0 flag = false;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
	
	}

	public boolean registerRequest(String username, char[] password) {
		boolean flag = false;
		client.setName(username);
		client.setPassword(password.toString());
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.REGISTER);
		objectToSend.setObject(client);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			flag = client.getIsRegistered();
			
			//if client.getIsOnline() == 0 flag = false;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
	}
	
}
