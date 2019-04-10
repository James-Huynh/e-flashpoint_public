package client;

import java.io.IOException;
import java.util.ArrayList;

import actions.Action;
import actions.ActionList;
import commons.bean.TextMessage;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import commons.util.MyDate;
import game.BoardOne;
import game.GameState;
import gui.Launcher;
import lobby.Lobby;
import server.Player;
import token.Colour;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;
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
	private String gameName;
	
	private boolean endTurnTrigger = false;
	private boolean dodgeRefreshFlag;
	
	public ClientManager(ClientInputThread input, ClientOutputThread output, Launcher myLauncher) {
		this.inputThread = input;
		this.outputThread = output;
		this.launcher = myLauncher;
		this.dodgeRefreshFlag = true;
		
	}
	
	public boolean readMessage() throws IOException, ClassNotFoundException {
		boolean flag = false;
		System.out.println("Reading on Client side Started");
//		if(inputThread.getInputStream().available() == 0) {
//			return false;
//		}
		Object readObject = inputThread.readInputStream();
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;
			switch(read_tranObject.getType()) {
			case LOGINFAILURE:
				System.out.println("fail to login!");
				break;
			case STARTGAMESTATESUCCESS:
				System.out.println("Succesuful gameStateRetrieval");
				System.out.println(read_tranObject.getType());
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				//requestObject.setCurrentState(currentState);
				flag = true;
				startGameFlag = 2;
				break;
			case STARTSAVEDGAMESTATESUCCESS:
				System.out.println("Succesuful 'saved' gameStateRetrieval");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				flag = true;
				startGameFlag = 2;
				break;
				
			case STARTSAVEDGAMESTATENAMESUCCESS:
				System.out.println("Succesuful 'saved' gameStateRetrieval Mat");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				requestObject.setGameName((String) read_tranObject.getObject());
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
				//System.out.println(requestObject.getMatTiles()[0][0].getFirefighterList().get(0).getOwner().getUserName() + "haha we made it!"); //this is tester
				//requestObject.getCurrentState().setTiles(requestObject.getMatTiles());
				break;
			case ACTIONSUCCESS:
				System.out.println("Succesuful action request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				startGameFlag = 1;
				flag = true;
				break;
			case SENDRIDERECEIVED:
				System.out.println("Ride request to intiate pop-up received");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				flag = true;
				break;
			case LOBBYCREATIONSUCCESS:
				System.out.println("Successful lobby request");
				requestObject = (User) read_tranObject.getObject();
				currentLobby = requestObject.getCurrentLobby();
				flag = true;
				System.out.println(requestObject.getCurrentLobby().getPlayers().get(0).getUserName()); //this is tester
				break;
			case LOADSAVESUCCESS:
				System.out.println("Successful saved game lobby request");
				requestObject = (User) read_tranObject.getObject();
				currentLobby = requestObject.getCurrentLobby();
				
				System.out.println("setting up lobby page" + currentLobby.getDifficulty());
				flag = true;
				//startGameFlag = 2;
				break;
			case REQUESTSAVEDLISTSUCCESS:
				System.out.println("Successful list of savedGames request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
				
			case FINDLOBBYSUCCESS:
				System.out.println("Successful findlobby request");
				requestObject = (User) read_tranObject.getObject();
				if (!requestObject.getLobbyList().isEmpty()) {
					flag = true;
					//System.out.println(requestObject.getLobbyList().get(0).getPlayers().get(0).getUserName());
					break;
				}
				flag = false;
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
			case VEHICLEPLACEMENTSUCCESS:
				System.out.println("Successful joinlobby request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				flag = true;
				break;
			case ENDTURNSUCCESS:
				System.out.println("Successful endTurn request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				startGameFlag = 1;
				setEndTurnTrigger(true);
				flag = true;
				break;
			case CHATMESSAGE:
				mDataArrays=(((User) read_tranObject.getObject()).getChatArray());
				for(ChatMsgEntity m: mDataArrays) {
					System.out.println(m.getMessage());
				}
				Collections.reverse(mDataArrays);
				startGameFlag = 1;
				flag = true;
				break;
				//launcher.refreshLobby();		// James
			
			case SPECIALITYSELECTED:
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				startGameFlag = 1;
				flag = true;
				break;
			case SPECIALITYENDSUCCESS:
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				startGameFlag = 1;
				flag = true;
				break;
			case FIREFIGHTERSELECTED:
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				startGameFlag = 1;
				flag = true;
				break;
			case ROLLDICEFORME:
				System.out.println("Successful dices request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case ROLLREDDICEFORME:
				System.out.println("Successful red dice request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case ROLLBLACKDICEFORME:
				System.out.println("Successful black dice request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case DODGERECEIVED:
				System.out.println("Successful dodge inform request");
				requestObject.setCurrentState((GameState) read_tranObject.getObject());
				flag = true;
				startGameFlag = 1;
				break;
				
			case ERROR:
				System.out.println("SOMEONEDISCONNECTED!!");
				startGameFlag = 3;
				flag = true;
				launcher.showPopUp();
				break;
			case ENDGAME:
				System.out.println("Successful EndGame request");
//				requestObject = (User) read_tranObject.getObject();
				requestObject.resetUserAfterGame();
				startGameFlag = 3;
				flag = true;
				launcher.createNewThread();
				break;
			case REFRESHSUCCESS:
				System.out.println("Succesful refresh request");
				requestObject = (User) read_tranObject.getObject();
				setDodgeRefreshFlag(requestObject.getDodgeResponseBoolean());
				startGameFlag = 1;
				flag = true;
				break;
			}
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
			System.out.println("3");
			if(readMessage()) {
				flag = true;
			}
			
		}
		catch(ClassNotFoundException l) {
			System.out.println("1");
		}
		catch(IOException k) {
			System.out.println("2");
		}
		System.out.println("|2|" + requestObject.getId()); 
//		System.out.println("|3|" + client.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		System.out.println(flag);
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
			}else {
				return false;
			}
			//if client.getIsOnline() == 0 flag = false;	
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return true;
	
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
		if(difficulty.equals("notSelected")) {
			difficulty = "Recruit";
		}
		Lobby lobby = new Lobby();
		lobby.setName(name);
		lobby.setMode(mode);
		lobby.setCapacity(capacity);
		lobby.setDifficulty(difficulty);
		lobby.setBoard(board);
		lobby.setIsLoadGame(false);
		
		lobby.createTemplate();
		
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
		if(requestObject.getCurrentLobby().getIsLoadGame() == false) {
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.STARTGAMESTATE);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		}else {
			TranObject<User> objectToSend = new TranObject<User>(TranObjectType.STARTSAVEDGAMESTATE);
			objectToSend.setObject(requestObject);
			outputThread.setMsg(objectToSend);
		}
//		try {
//			while(readMessage() != true) {
//				
//			}
//			flag = true;
//		}
//		catch(ClassNotFoundException l) {
//			
//		}
//		catch(IOException k) {
//
//		}
//		System.out.println("|3|" + requestObject.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
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
//		try {
//			while(readMessage() != true) {
//				
//			}
//			flag = true;
//			System.out.println(anyString);
//		}
//		catch(ClassNotFoundException l) {
//			
//		}
//		catch(IOException k) {
//			
//		}
//		return requestObject.getCurrentState();
		return true;

	}
	
	public void savedGameListRequest(){
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.REQUESTSAVEDLIST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			
		}
		
		catch(Exception E) {
			System.out.println("Exception occured during sacedGameListRequest.");
		}
		
		
		
	}
	
	public String getGameName() {
		return requestObject.getGameName();
	}
	
	public void setGameName(String gN) {
		this.gameName = gN;
	}
	
	public ArrayList<GameState> getSavedGameStates(){
		return requestObject.getsavedGameStates();
	}
	
	public ArrayList<String> getSavedNamesGameStates(){
		return requestObject.getSavedNamesGameStates();
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
//		try {
//			while(readMessage() != true) {
//				
//			}
//			flag = true;
//		}
//		catch(ClassNotFoundException l) {
//			
//		}
//		catch(IOException k) {
//			
//		}
		return true;
//		return requestObject.getCurrentState();
	}

	public boolean lobbyListRequest() {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.FINDLOBBY);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			boolean stop = readMessage();
			while(stop != true) {
				if(stop == false) {
					flag = false;
					break;
				}
				stop = readMessage();
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
		try {
			while(readMessage() != true) {
				System.out.println("im here 1212");
			}
//			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return this.startGameFlag;
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
		
//		try {
//			while(readMessage() != true) {
//				
//			}
//			flag = true;
//		}
//		catch(ClassNotFoundException l) {
//			
//		}
//		catch(IOException k) {
//			
//		}
		return true;
	}
	
	public boolean saveGameRequest() {
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.SAVEGAME);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		return true;
	}
	
	public boolean saveGameRequestString(String name) {
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.SAVEGAMENAME);
		requestObject.setGameName(name);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		return true;
	}

	public boolean getEndTurnTrigger() {
		return endTurnTrigger;
	}

	public void setEndTurnTrigger(boolean endTurnTrigger) {
		this.endTurnTrigger = endTurnTrigger;
	}
	
	public boolean loadGameLobbyRequest(int savedGameNum) {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOADSAVE);
		requestObject.setNum(savedGameNum); //which # savedGame the player selected
		
		Lobby lobby = new Lobby();
		lobby.setIsLoadGame(true);
		
		//lobby.setCapacity(capacity)
		
		
		requestObject.setCurrentLobby(lobby);
		
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
	
	public boolean loadGameLobbyRequestMat(int savedGameIndex) {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOADSAVE);
		//requestObject.setNum(savedGameNum); //which # savedGame the player selected
		requestObject.setLoadIndex(savedGameIndex);
		
		Lobby lobby = new Lobby();
		lobby.setIsLoadGame(true);
		//lobby.setCapacity(capacity)
		
		
		requestObject.setCurrentLobby(lobby);
		
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
	
	public boolean sendMsgRequest(TextMessage message) {
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.CHATMESSAGE);
		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setMessage(message.getMessage());
		entity.setDate(MyDate.getComDate());
		entity.setName(requestObject.getName());
		Colour colour = null;
		for(Player p : requestObject.getCurrentLobby().getPlayers()) {
			if(p.getUserName().equals(requestObject.getName())) {
				colour = p.getColour();
			}
		}
		System.out.println("sending");
		entity.setColour(colour);
		User a= new User();
		a.setChat(entity);
		objectToSend.setObject(a);
		
		
		//requestObject.setMessage(message);
		//objectToSend.setObject(objectToSend);
		
		outputThread.setMsg(objectToSend);
		return true;
	}
	
	public List<ChatMsgEntity> displayChat(){
		return mDataArrays;
	}
	
	public boolean placeVehicleRequest(int direction, Vehicle type) {
		boolean flag = false;

		requestObject.setVehicleIndex(direction);
		requestObject.setVehicleType(type);
		
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.VEHICLEPLACEMENT);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		System.out.println("Placing a Vehicle");
//		try {
//			while(readMessage() != true) {
//				
//			}
//			flag = true;
//		}
//		catch(ClassNotFoundException l) {
//			
//		}
//		catch(IOException k) {
//			
//		}
		
		return flag;
	}
	
	public List<ChatMsgEntity> getChatArray(){
		return this.mDataArrays;
	}
	
	public boolean sendRideRequests(Vehicle type) {
		boolean flag = false;
//
//		requestObject.setVehicleIndex(direction);
//		requestObject.setVehicleType(type);
		
		requestObject.setRidingObject(type);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.SENDRIDEREQUEST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		System.out.println("In ClientManager :- Sending Ride Requests to Everyone");
//		try {
//			while(readMessage() != true) {
//				
//			}
//			flag = true;
//		}
//		catch(ClassNotFoundException l) {
//			
//		}
//		catch(IOException k) {
//			
//		}
		
		return flag;
	}
	
	public boolean sendRideResponse(boolean val, int index) {
		boolean flag = true;
		
		requestObject.setRideResponse(val);
		requestObject.setMyFFIndex(index);
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.SENDRIDERESPONSE);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		return flag;
	}

	public boolean sendSpecialitySelectionRequest(Speciality s, int i) {
		// TODO Auto-generated method stub
		requestObject.setDesiredSpeciality(s);
		requestObject.setDesiredFirefighter(i);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.SPECIALITYSELECTREQUEST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		return true;
	}

	public boolean sendSelectionEndRequest() {
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.SELECTENDREQUEST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		return true;
	}

	public boolean fireFighterSelectionRequest(token.Colour i) {
		requestObject.setDesiredFirefighterColour(i);
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.FIREFIGHTERSELECTREQUEST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		return true;
	}

	public boolean dodgeAnswer(Action a, int myIndex) {
		requestObject.setDodgeAction(a);
		requestObject.setMyFFIndex(myIndex);
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.DODGERESPONSE);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		return true;
	} 
	public void sendEndGameRequest() {
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.ENDGAME);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
	}
	
	public void sendRefreshRequest(int[] myFFIndexes2) {
		boolean b = false;
		for(int i = 0; i<6; i++) {
			if(myFFIndexes2[i] == getUsersGameState().getActiveFireFighterIndex()) {
				b = true;
			}
		}
		if(b) {
			TranObject<User> objectToSend = new TranObject<User>(TranObjectType.REFRESH);
			objectToSend.setObject(requestObject);
			outputThread.setMsg(objectToSend);
			System.out.println("Refresh request being sent in CM");
		} 
	}

	public boolean getDodgeRefreshFlag() {
		return dodgeRefreshFlag;
	}

	public void setDodgeRefreshFlag(boolean dodgeRefreshFlag) {
		this.dodgeRefreshFlag = dodgeRefreshFlag;
	}
	
}
