package client;

import java.io.IOException;
import java.util.ArrayList;

import actions.Action;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import game.FamilyGame;
import game.GameState;
import gui.Launcher;
import lobby.Lobby;
import chat.ChatMsgEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientManager {
	
	private ClientInputThread inputThread;
	private ClientOutputThread outputThread;
	private User requestObject; 
//	private ObjectInputStream ois;
//	private ObjectOutputStream oos;
	private GameState currentGameState;
	private Lobby currentLobby;
//	private ArrayList<Lobby> onlineLobbies;
	private int startGameFlag = 0;
	private TranObjectType anyString;
	private Launcher launcher;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	
	public ClientManager(ClientInputThread input, ClientOutputThread output, Launcher myLauncher) {
		this.inputThread = input;
		this.outputThread = output;
		this.launcher = myLauncher;
		
	}
	
	public boolean readMessage() throws IOException, ClassNotFoundException {
		boolean flag = false;
		System.out.println("Reading on Client side Started");
		Object readObject = inputThread.readInputStream();
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;
			switch(read_tranObject.getType()) {
			case STARTGAMESTATESUCCESS:
				System.out.println("Succesuful gameStateRetrieval");
				System.out.println(read_tranObject.getType());
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				//requestObject.setCurrentState(currentState);
				flag = true;
				startGameFlag = 2;
				break;
			case SUCCESS:
				System.out.println("Succesuful connection");
				System.out.println(read_tranObject.getType());
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case LOGINSUCCESS:
				System.out.println("Succesuful login request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case REGISTERSUCCESS:
				System.out.println("Succesuful register request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case FFPLACEMENTSUCCESS:
				System.out.println("Succesuful placement request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				flag = true;
				startGameFlag = 1;
				anyString = read_tranObject.getType();
				//System.out.println(requestObject.getMatTiles()[0][0].getFirefighterList().get(0).getOwner().getUserName() + "haha we made it!"); //this is tester
				//requestObject.getCurrentState().setTiles(requestObject.getMatTiles());
				break;
			case ACTIONSUCCESS:
				System.out.println("Succesuful action request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				startGameFlag = 1;
				flag = true;
				break;
			case LOBBYCREATIONSUCCESS:
				System.out.println("Successful lobby request");
				requestObject = (User) read_tranObject.getObject();
				currentLobby = requestObject.getCurrentLobby();
				flag = true;
				System.out.println(requestObject.getCurrentLobby().getPlayers().get(0).getUserName()); //this is tester
				break;
			case FINDLOBBYSUCCESS:
				System.out.println("Successful findlobby request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				System.out.println(requestObject.getLobbyList().get(0).getPlayers().get(0).getUserName());
				break;
			case JOINLOBBYSUCCESS:
				System.out.println("Successful joinlobby request");
				//currentLobby = (Lobby) read_tranObject.getObject();
				//requestObject.setCurrentLobby(currentLobby);
				requestObject.setCurrentLobby((Lobby) read_tranObject.getObject());
				startGameFlag = 1;
				flag = true;
				/*
				 * if(requestObject.getCurrentLobby().getPlayers().size() == 2) {
				 * 
				 * }else ifrequestObject.getCurrentLobby().getPlayers().size() == 1()
				 */
				System.out.println(requestObject.getCurrentLobby().getPlayers().get(1).getUserName());
				break;
			case ENDTURNSUCCESS:
				System.out.println("Successful endTurn request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
//				launcher.refreshBoard();
				startGameFlag = 1;
				flag = true;
				break;
			case CHATMESSAGE:
				requestObject = (User) read_tranObject.getObject();
				
			
					ChatMsgEntity entity = null ;
						if (entity.getName().equals("")) {
							entity.setName(requestObject.getName());
						}
				
							entity.setImg(requestObject.getId());
							entity.setMessage(requestObject.getMessage());
						
						mDataArrays.add(entity);
					}
					Collections.reverse(mDataArrays);
				
			
		
		
			
			
		}
		return flag;
	}
	
	public boolean connectionRequest(User inputClient) {
		boolean flag = false;
		System.out.println("Getting here");
		requestObject = inputClient;
		System.out.println(requestObject.getId());
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.CONNECT);
		objectToSend.setObject(requestObject);
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
		System.out.println("|2|" + requestObject.getId()); 
//		System.out.println("|3|" + client.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		return flag;
	}

	public boolean loginRequest(String username, String password) {
		boolean flag = false;
		requestObject.setName(username);
		requestObject.setPassword(password.toString());
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOGIN);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			if(requestObject.getIsOnline() == 1) {
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

	public boolean registerRequest(String username, String password) {
		boolean flag = false;
		requestObject.setName(username);
		requestObject.setPassword(password.toString());
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.REGISTER);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			flag = requestObject.getIsRegistered();
			
			//if client.getIsOnline() == 0 flag = false;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
	}
	
	// James
	/**
	 * Asks the server to create a lobby
	 * @return boolean indicating the status of the operation
	 */
	public boolean createLobbyRequest(String name, String mode, int capacity, String difficulty, String board) {
		boolean flag = false;
		
//		Lobby lobby = requestObject.getCurrentLobby();
		Lobby lobby = new Lobby();
		lobby.setName(name);
		lobby.setMode(mode);
		lobby.setCapacity(capacity);
		lobby.setDifficulty(difficulty);
		lobby.setBoard(board);
		if(mode.equals("Family")){
			lobby.setFamilyGame();
		}else{
			if(difficulty.equals("Recruit")) lobby.setRecruitGame();
			else if(difficulty.equals("Veteran")) lobby.setVeteranGame();
			else lobby.setHeroicGame();
			
		}
		
		requestObject.setCurrentLobby(lobby);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOBBYCREATION);
		
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);

		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		
		catch(Exception E) {
			System.out.println("Exception occured during createLobbyRequest.");
		}
		
		
		return flag;
	}
	
	public boolean gameStateRequest(User userOne) {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.STARTGAMESTATE);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		System.out.println("|3|" + requestObject.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		return flag;
		
	}

	public boolean placeFFRequest(int[] coords) {
		boolean flag = false;
		requestObject.setCoords(coords);
		requestObject.setPlaced(false);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.FIREFIGHTERPLACEMENT);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		System.out.println("place check");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
			System.out.println(anyString);
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		
//		return requestObject.getCurrentState();
		return flag;
	}
	
	public GameState getUsersGameState() {
		return requestObject.getCurrentState();
	}
	public Lobby getLobby() {
		return requestObject.getCurrentLobby();
	}
	public ArrayList<Lobby> getLobbyList(){
		return requestObject.getLobbyList();
	}

	public boolean ActionRequest(Action a) {
		boolean flag = false;
		requestObject.setAction(a);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.ACTIONREQUEST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		System.out.println("place check");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
//		return requestObject.getCurrentState();
	}

	public boolean lobbyListRequest() {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.FINDLOBBY);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			if(requestObject.getIsOnline() == 1) {
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

	public boolean joinLobbyRequest(Lobby lobby) {
		
		boolean flag = false;
		
		requestObject.setCurrentLobby(lobby);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.JOINLOBBY);
		
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);

		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		
		catch(Exception E) {
			System.out.println("Exception occured during joinLobbyRequest.");
		}
		
		
		return flag;
		
	}
	
	public int listenForResponses() {
		System.out.println("I am listening in client manager");
//		
//		startGameFlag = 0;
		boolean flag = false;
		try {
			while(readMessage() != true) {
				
			}
//			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return startGameFlag;
	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return this.requestObject.getName();
	}

	public boolean endTurnRequest() {
		boolean flag = false;
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.ENDTURN);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
	}
	
}
