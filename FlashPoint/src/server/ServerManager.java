package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import game.GameState;
import lobby.Lobby;

public class ServerManager {
	
	HashMap<String,String> accounts;
	
	
	public ServerManager() {

	accounts = new HashMap<String,String>();
	
	}
	
	public void readMessage(OutputThread out, ObjectInputStream ois) throws IOException, ClassNotFoundException {
		System.out.println("server loginUser1234:");
		Object readObject = ois.readObject();
		System.out.println("Here? now");
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
			case LOGIN:
				TranObject<User> resultOfLogin = new TranObject<User>(TranObjectType.LOGINSUCCESS);
				User updatedUser = (User) read_tranObject.getObject();
				if(accounts.get(updatedUser.getName()).equals(updatedUser.getPassword())) {
				updatedUser.setIsOnline(1);	
				}
				else {
				updatedUser.setIsOnline(0);
				}
				resultOfLogin.setObject(updatedUser);
				out.setMessage(resultOfLogin);
				break;
			case REGISTER:
				TranObject<User> resultOfRegister = new TranObject<User>(TranObjectType.REGISTERSUCCESS);
				User updatedUserTwo = (User) read_tranObject.getObject();
				if(accounts.containsKey(updatedUserTwo.getName())) {
					updatedUserTwo.setIsRegistered(false);
				}else {
				accounts.put(updatedUserTwo.getName(), updatedUserTwo.getPassword());
				updatedUserTwo.setIsRegistered(true);
				}
				resultOfRegister.setObject(updatedUserTwo);
				out.setMessage(resultOfRegister);
				break;
				
			case GAMESTATEOUT:
				TranObject<GameState> gameStateOutput = new TranObject<GameState>(TranObjectType.GAMESTATEUPDATE);
				Lobby lobby1 = new Lobby();
				GameState gamestate1 = GameState.getInstance();
				gamestate1.updateGameStateFromLobby(lobby1);
				gameStateOutput.setObject(gamestate1);
				out.setMessage(gameStateOutput);
		}
		}
			
	}


}
