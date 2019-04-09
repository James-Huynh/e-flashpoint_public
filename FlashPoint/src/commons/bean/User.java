package commons.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import actions.Action;
import chat.ChatMsgEntity;
import game.GameState;
import lobby.Lobby;
import tile.Tile;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;


public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
//	private String email;
	private String password;
	private int isOnline;
//	private int img;
//	private int group;
	private String ip;
//	private int port;
	private GameState currentState;
	private ChatMsgEntity currentChat;
	private Tile[][] matTiles;
    private boolean isRegistered;
    private int[] coords;
    private boolean placed;
    private boolean rideResponse;
    private actions.Action action;
    private Lobby currentLobby;
    private ArrayList<Lobby> currentLobbies;
    private TextMessage message;
    private int vehicleIndex;
    private int myFFIndex;
    private Vehicle vehicleType;
    private Vehicle ridingObject;
    private Speciality desiredSpeciality;
    private int desiredFirefighter;
    private int loadNum;
    private int loadIndex;
    private ArrayList<GameState> savedGameStates;
    private int[] dices;
    private actions.Action dodgeAction;
    private token.Colour desiredFirefighterColour;
    private boolean dodgeResponseBoolean;
    
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private String gameName;
	public Integer getId() {
		return id;
	}

	public void setNum(int loadNum) {
		this.loadNum = loadNum;
	}
	
	public int getLoadIndex() {
		return this.loadIndex;
	}
	
	public void setLoadIndex(int loadIndex) {
		this.loadIndex = loadIndex;
	}
	
	public int getNum() {
		return this.loadNum;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	
	public void setChat(ChatMsgEntity currentChat) {
		this.currentChat=currentChat;
	}
	public ChatMsgEntity getChat() {
		return currentChat;
	}
	public List<ChatMsgEntity> getChatArray() {
		return mDataArrays;
	}
	public void setChatArray(List<ChatMsgEntity> mDataArrays) {
		this.mDataArrays=mDataArrays;
	}

//	public int getPort() {
//		return port;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}

//	public int getGroup() {
//		return group;
//	}
//
//	public void setGroup(int group) {
//		this.group = group;
//	}

//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}
	
	public GameState getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}

	public boolean getIsRegistered() {
		
		return isRegistered;
	}
	
	public void setIsRegistered(boolean bool) {
		this.isRegistered = bool;
	}

	public int[] getCoords() {
		return coords;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public boolean isPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}
	
	public actions.Action getAction(){
		return this.action;
	}
	
	public void setAction(actions.Action action) {
		this.action = action;
	}

	public Lobby getCurrentLobby() {
		return currentLobby;
	}

	public void setCurrentLobby(Lobby currentLobby) {
		this.currentLobby = currentLobby;
	}

	public Tile[][] getMatTiles() {
		return matTiles;
	}

	public void setMatTiles(Tile[][] matTiles) {
		this.matTiles = matTiles;
	}

	public ArrayList<Lobby> getLobbyList(){
		return this.currentLobbies;
	}
	
	public void setLobbyList(ArrayList<Lobby> lobbyList) {
		this.currentLobbies = lobbyList;
		
	}

	public TextMessage getMessage() {
		
		return message;
	}
	
	public void setMessage(TextMessage message) {
		this.message=message;
	}

	public int getVehicleIndex() {
		return vehicleIndex;
	}

	public void setVehicleIndex(int vehicleIndex) {
		this.vehicleIndex = vehicleIndex;
	}

	public Vehicle getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Vehicle vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Vehicle getRidingObject() {
		return ridingObject;
	}

	public void setRidingObject(Vehicle ridingObject) {
		this.ridingObject = ridingObject;
	}

	public void setDesiredSpeciality(Speciality s) {
		// TODO Auto-generated method stub
		this.desiredSpeciality = s;
		
	}
	
	public Speciality getDesiredSpeciality() {
		return this.desiredSpeciality;
	}
	
	public void setSavedGameStates(ArrayList<GameState> savedGameStates) {
		this.savedGameStates = savedGameStates;
	}
	
	public ArrayList<GameState> getsavedGameStates(){
		return this.savedGameStates;
	}

	public void setDesiredFirefighter(int i) {
		this.desiredFirefighter = i;
		
	}
	
	public int getDesiredFirefighter() {
		return this.desiredFirefighter;
	}
	
	public void setDesiredFirefighterColour(token.Colour i) {
		this.desiredFirefighterColour = i;
		
	}
	
	public token.Colour getDesiredFirefighterColour() {
		return this.desiredFirefighterColour;
	}
	
	public void setDices(int[] dices) {
		this.dices = dices;
	}
	
	public int[] getDices() {
		return this.dices;
	}

	public boolean getRideResponse() {
		return rideResponse;
	}

	public void setRideResponse(boolean rideResponse) {
		this.rideResponse = rideResponse;
	}

	public int getMyFFIndex() {
		return myFFIndex;
	}

	public void setMyFFIndex(int myFFIndex) {
		this.myFFIndex = myFFIndex;
	}

	public void setDodgeAction(Action a) {
		this.dodgeAction = a;
		
	}
	public actions.Action getDodgeAction(){
		return this.dodgeAction;
	}

	public String getGameName() {
		return this.gameName;
	}
	
	public void setGameName(String s) {
		this.gameName = s;
	}
	
	public void resetUserAfterGame() {
		this.currentState = null;
		this.currentChat = null;
		this.matTiles = null;
		this.coords = null;
		this.placed = false;
		this.rideResponse = false;
		this.action = null;
		this.currentLobbies = null;
		this.currentLobby = null;
		this.message = null;
		this.vehicleIndex = 5;
		this.vehicleType = null;
		this.myFFIndex = 7;
		this.ridingObject = null;
		this.dodgeAction = null;
		this.desiredFirefighter = 7;
		this.desiredFirefighterColour = null;
		this.desiredSpeciality = null;
		this.dices = null;
		
	}

	public boolean getDodgeResponseBoolean() {
		return dodgeResponseBoolean;
	}

	public void setDodgeResponseBoolean(boolean dodgeResponseBoolean) {
		this.dodgeResponseBoolean = dodgeResponseBoolean;
	}

//	public int getImg() {
//		return img;
//	}
//
//	public void setImg(int img) {
//		this.img = img;
//	}

	//@Override
//	public boolean equals(Object o) {
//		if (o instanceof User) {
//			User user = (User) o;
//			if (user.getId() == id && user.getIp().equals(ip)
//					&& user.getPort() == port) {
//				return true;
//			}
//		}
//
//		return false;
//	}

	//@Override
//	public String toString() {
//		return "User [id=" + id + ", name=" + name + ", email=" + email
//				+ ", password=" + password + ", isOnline=" + isOnline
//				+ ", img=" + img + ", group=" + group + ", ip=" + ip
//				+ ", port=" + port + "]";
//	}

}
