package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import actions.ActionList;
import chat.ChatMsgEntity;
import commons.bean.TextMessage;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import commons.util.Constants;
import commons.util.MyDate;
import game.GameState;
import lobby.Lobby;
import token.Vehicle;


public class ServerInputThread extends Thread {
	private Socket socket;
	private OutputThread out;
	private OutputThreadMap map;
	private ObjectInputStream ois;
	private boolean isStart = true;
	ServerManager serverManager;
	Random rand = new Random();

	public ServerInputThread(Socket socket, OutputThread out, OutputThreadMap map, ServerManager newServerManager) {
		this.socket = socket;
		this.out = out;
		this.map = map;
		this.serverManager = newServerManager;
		try {
			ois = new ObjectInputStream(socket.getInputStream());// 实锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public void run() {
//		serverManager = new ServerManager();
		try {
			while (isStart) {
				//System.out.println("Looping?");
//				serverManager.readMessage(out, ois);
				// JUNHA : this is supposed to be serverManager.readMessage();
				 readMessage();

			}
			if (ois != null)
				System.out.println("Object Input Stream is null");
			ois.close();
			if (socket != null)
				System.out.println("Socket is null error");
			socket.close();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound Exception");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception Error");
			e.printStackTrace();
		}

	}

	/**
	 * This method has been removed to ServerManager
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */

	public void readMessage() throws IOException, ClassNotFoundException {
		Object readObject = ois.readObject();// 锟斤拷锟斤拷锟叫讹拷取锟斤拷锟斤拷
		//System.out.println("Insinde readMessage");
//		UserDao dao = UserDaoFactory.getInstance();// 通锟斤拷dao模式锟斤拷锟斤拷锟教�
		if (readObject != null && readObject instanceof TranObject) {
		//	System.out.println("Entered IF");
			TranObject read_tranObject = (TranObject) readObject;// 转锟斤拷锟缴达拷锟斤拷锟斤拷锟�
			TranObject<User> returnObject;
			TranObject<GameState> returnGameState;
			TranObject<List> returnChat;
			User requestObject;
			int idGenerator;
			switch (read_tranObject.getType()) {
			case CONNECT:
				System.out.println("In connect request");
				returnObject = new TranObject<User>(TranObjectType.SUCCESS);
				requestObject = (User) read_tranObject.getObject();
				idGenerator = rand.nextInt(10);
				while(serverManager.getPlayers().containsKey(Integer.valueOf(idGenerator))) {
					idGenerator = rand.nextInt(10);
				}
				requestObject.setId(Integer.valueOf(idGenerator));
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case LOGIN:
				System.out.println("In login request");
				User loginUser = (User) read_tranObject.getObject();
				returnObject = new TranObject<User>(TranObjectType.LOGINSUCCESS);
				requestObject = (User) read_tranObject.getObject();

				if(serverManager.getAccounts().get(requestObject.getName()) != null) {
					if(serverManager.getAccounts().get(requestObject.getName()).equals(requestObject.getPassword())){
						System.out.println("is online ");
						requestObject.setIsOnline(1);	
						serverManager.createPlayer(requestObject.getName(), requestObject.getPassword(), requestObject.getId());
					}	
				}
				else { //User doesn't exist
					//System.out.println("is online set to 0");
					requestObject.setIsOnline(0);
				}
				returnObject.setObject(requestObject);
				
				out.setMessage(returnObject);
				map.add(loginUser.getId(), out);
				break;
			case REGISTER:
				System.out.println("In register request");
				returnObject = new TranObject<User>(TranObjectType.REGISTERSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				if(serverManager.getAccounts().containsKey(requestObject.getName())) {
					System.out.println("account already exists");
					requestObject.setIsRegistered(false);
				}else {
					System.out.println("account added");
					serverManager.getAccounts().put(requestObject.getName(), requestObject.getPassword());
					System.out.println(requestObject.getPassword());
					requestObject.setIsRegistered(true);
				}
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case CHATMESSAGE:
				System.out.println("chat message received");
				returnObject=new TranObject<User>(TranObjectType.CHATMESSAGE);
				requestObject = (User) read_tranObject.getObject();
				
				serverManager.getChatArray().add(requestObject.getChat());
				for(ChatMsgEntity m : serverManager.getChatArray()) {
					System.out.println(m.getName() + ":" + m.getMessage());
				}
				requestObject.setChatArray(serverManager.getChatArray());
				
				returnObject.setObject(requestObject);
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnObject);
				}
				break;
			case LOADSAVE:
				System.out.println("load request received");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setLobby(requestObject.getCurrentLobby());
				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
				//serverManager.getLobby().setCapacity(serverManager.getGameState().getListOfPlayers().size());
				//serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));  //
				//serverManager.getLobby().setDifficulty("serverManager.getGameState().getDifficulty()");
				//serverManager.getLobby().setMode("mode");
				returnObject = new TranObject<User>(TranObjectType.LOADSAVESUCCESS);
				returnObject.setObject(requestObject);
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnObject);
				}
				break;
			
			case STARTGAMESTATE:
				//System.out.println("In game state update request");
//				returnObject = new TranObject<User>(TranObjectType.STARTGAMESTATESUCCESS);
				returnGameState = new TranObject<GameState>(TranObjectType.STARTGAMESTATESUCCESS);
				serverManager.createGame();
//				requestObject = (User) read_tranObject.getObject();
//				requestObject.setCurrentState(serverManager.getGameState());
//				returnObject.setObject(requestObject);
				returnGameState.setObject(serverManager.getGameState());
//				out.setMessage(returnGameState);
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case STARTSAVEDGAMESTATE:
				requestObject = (User) read_tranObject.getObject();
				System.out.println(requestObject.getNum());
				serverManager.loadGame(requestObject.getNum());
				returnGameState = new TranObject<GameState>(TranObjectType.STARTSAVEDGAMESTATESUCCESS);
				returnGameState.setObject(serverManager.getGameState()); // this was already done earlier
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case FIREFIGHTERPLACEMENT:
				System.out.println("firefighter placement request");
				requestObject = (User) read_tranObject.getObject();
				int[] coords = requestObject.getCoords();
				serverManager.placeFirefighter(coords, requestObject.getId());
				returnGameState = new TranObject<GameState>(TranObjectType.FFPLACEMENTSUCCESS);
				returnGameState.setObject(serverManager.getGameState());
//				out.setMessage(returnGameState);
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
					System.out.println(java.lang.Thread.activeCount());
				}

				break;
			case ACTIONREQUEST:
				System.out.println("In action request");
				requestObject = (User) read_tranObject.getObject();
				
				if(requestObject.getAction().getTitle() == ActionList.Drive) {
					boolean popUpFlag;
					System.out.println("hello we are riding");
					if(requestObject.getAction().isAmbulance()) {
						popUpFlag = serverManager.askRelevantFirefighters(Vehicle.Ambulance);
					}
					else {
						popUpFlag = serverManager.askRelevantFirefighters(Vehicle.Engine);
					}
					
					if(popUpFlag) {
						System.out.println("we have asked");
						returnGameState = new TranObject<GameState>(TranObjectType.SENDRIDERECEIVED);
						returnGameState.setObject(serverManager.getGameState());
						for (OutputThread onOut : map.getAll()) {
							System.out.println("hello should be in the output thread");
							onOut.setMessage(returnGameState); 
						}
					}
					else {
						System.out.println("We didn't have to ask");
					}

					System.out.println("hello should be at the while" + serverManager.hasEveryoneResponded());
					if(popUpFlag) {
						while(!serverManager.hasEveryoneResponded()) {
							
						}
						System.out.println("Out of response checking loops");
					}
				}
				
				
				
				
				serverManager.performAction(requestObject.getAction());
				serverManager.resetHashMap();
				returnGameState = new TranObject<GameState>(TranObjectType.ACTIONSUCCESS);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState); 
				}
				break;
			case SENDRIDEREQUEST:
				requestObject = (User) read_tranObject.getObject();
				serverManager.askRelevantFirefighters(requestObject.getRidingObject());
				
				returnGameState = new TranObject<GameState>(TranObjectType.SENDRIDERECEIVED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState); 
				}
				break;
			case SENDRIDERESPONSE:
				requestObject = (User) read_tranObject.getObject();
				serverManager.updateResponse(requestObject.getRideResponse(), requestObject.getMyFFIndex());
				System.out.println("hello we have updated riderResponse");
//				returnGameState = new TranObject<GameState>(TranObjectType.RIDERESPONSEACK);
//				returnGameState.setObject(serverManager.getGameState());
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnGameState); 
//				}
				break;
			case LOBBYCREATION:
				//System.out.println("In lobby creation");
				returnObject = new TranObject<User>(TranObjectType.LOBBYCREATIONSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				serverManager.setLobby(requestObject.getCurrentLobby());
				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
				requestObject.setCurrentLobby(serverManager.getLobby());
				System.out.println(requestObject.getCurrentLobby().getPlayers().get(0).getUserName());
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case FINDLOBBY:
				System.out.println("In find lobby");
				returnObject = new TranObject<User>(TranObjectType.FINDLOBBYSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				requestObject.setLobbyList(serverManager.getLobbyList());
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case JOINLOBBY:
//				System.out.println("In join lobby");
//				returnObject = new TranObject<User>(TranObjectType.JOINLOBBYSUCCESS);
//				requestObject = (User) read_tranObject.getObject();
//				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
//				requestObject.setCurrentLobby(serverManager.getLobby());
//				returnObject.setObject(requestObject);
//				System.out.println("test 2 this should be entered username of second user" + requestObject.getCurrentLobby().getPlayers().get(1).getUserName());
////				out.setMessage(returnObject);
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnObject);// 广播一下用户上线
//				}
//				break;
				
				System.out.println("request join lobby");
				TranObject returnLobby = new TranObject<Lobby>(TranObjectType.JOINLOBBYSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
//				requestObject.setCurrentLobby(serverManager.getLobby());
				returnLobby.setObject(serverManager.getLobby());
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnLobby);// 广播一下用户上线
//				}
				for(Player p : serverManager.getLobby().getPlayers()) {
					map.getById(p.getID()).setMessage(returnLobby);;
				}
				break;
			case ENDTURN:
				System.out.println(" end turn");
				TranObject returnGameStateEnd = new TranObject<GameState>(TranObjectType.ENDTURNSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				serverManager.endTurn();
				returnGameStateEnd.setObject(serverManager.getGameState());
				for(OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameStateEnd);
				}
				break;
			case SAVEGAME:
				System.out.println("save game");
				returnObject = new TranObject<User>(TranObjectType.ENDTURNSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				//@CAO this is where you would save the game state
				serverManager.saveGame();
				
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
				
			case REQUESTSAVEDLIST:
				System.out.println("request saved game state objects");
				returnObject = new TranObject<User>(TranObjectType.REQUESTSAVEDLISTSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				requestObject.setSavedGameStates(serverManager.getSavedGames());

				System.out.println("list length is: " + requestObject.getsavedGameStates().size() );
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
				
			case MESSAGE:
				
				
				
				
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(read_tranObject);
				}
				
					
				
			
				break;
			case VEHICLEPLACEMENT:
				System.out.println("Vehicle placement request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.placeVehicle(requestObject.getVehicleIndex(), requestObject.getVehicleType());
				returnGameState = new TranObject<GameState>(TranObjectType.VEHICLEPLACEMENTSUCCESS);
				returnGameState.setObject(serverManager.getGameState());

				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case SPECIALITYSELECTREQUEST:
				System.out.println("Speciality selection request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setSpeciality(requestObject, requestObject.getDesiredSpeciality() );
				returnGameState = new TranObject<GameState>(TranObjectType.SPECIALITYSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case SELECTENDREQUEST:
				System.out.println("Speciality end request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setSpecialitySelecting(false);
				returnGameState = new TranObject<GameState>(TranObjectType.SPECIALITYENDSUCCESS);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case FIREFIGHTERSELECTREQUEST:
				System.out.println("Firefighter selection request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setFirefighter(requestObject);
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case ROLLDICEFORME:
				System.out.println("Rolling Dice for Driver request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setDice();
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case ROLLREDDICEFORME:
				System.out.println("ReRolling Red Dice for Driver request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setDice(0);
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case ROLLBLACKDICEFORME:
				System.out.println("ReRolling Black Dice for Driver request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setDice(1);
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
				
			case LOGOUT:
				User logoutUser = (User) read_tranObject.getObject();
				int offId = logoutUser.getId();
				System.out
						.println(MyDate.getDateCN() + " 用户：" + offId + " 下线了");
			
				isStart = false;
				map.remove(offId);// 从缓存的线程中移除
				out.setMessage(null);// 先要设置一个空消息去唤醒写线程
				out.setStart(false);// 再结束写线程循环

				TranObject<User> offObject = new TranObject<User>(
						TranObjectType.LOGOUT);
				User logout2User = new User();
				logout2User.setId(logoutUser.getId());
				offObject.setObject(logout2User);
				for (OutputThread offOut : map.getAll()) {// 广播用户下线消息
					offOut.setMessage(offObject);
				}
				break;
//			case REGISTER:// 锟斤拷锟斤拷没锟斤拷锟阶拷锟�
//				User registerUser = (User) read_tranObject.getObject();
////				int registerResult = dao.register(registerUser);
//				System.out.println(MyDate.getDateCN() + " 锟斤拷锟矫伙拷注锟斤拷:"
////						+ registerResult);
//				// 锟斤拷锟矫伙拷锟截革拷锟斤拷息
//				TranObject<User> register2TranObject = new TranObject<User>(
//						TranObjectType.REGISTER);
//				User register2user = new User();
////				register2user.setId(registerResult);
//				register2TranObject.setObject(register2user);
//				out.setMessage(register2TranObject);
//				break;
//			case LOGIN:
//				System.out.println("Am I here??");
//				System.out.println("server loginUser:");
//				User loginUser = (User) read_tranObject.getObject();
//				System.out.println("server loginUser:"+loginUser);
//				
//				if(loginUser.getName() == "Zaid") {
//					TranObject<User> o = new TranObject<User>(TranObjectType.LOGINSUCCESS);
//					User u = new User();
//					
//
//					o.setObject(u);
//					out.setMessage(o);
//				}
			/*	ArrayList<User> list = dao.login(loginUser);
				TranObject<ArrayList<User>> login2Object = new TranObject<ArrayList<User>>(
						TranObjectType.LOGIN);
				if (list != null) {// 锟斤拷锟斤拷录锟缴癸拷
					TranObject<User> onObject = new TranObject<User>(
							TranObjectType.LOGIN);
					User login2User = new User();
					login2User.setId(loginUser.getId());
					onObject.setObject(login2User);
					for (OutputThread onOut : map.getAll()) {
						onOut.setMessage(onObject);// 锟姐播一锟斤拷锟矫伙拷锟斤拷锟斤拷
					}
					map.add(loginUser.getId(), out);// 锟饺广播锟斤拷锟劫把讹拷应锟矫伙拷id锟斤拷写锟竭程达拷锟斤拷map锟叫ｏ拷锟皆憋拷转锟斤拷锟斤拷息时锟斤拷锟斤拷
					login2Object.setObject(list);// 锟窖猴拷锟斤拷锟叫憋拷锟斤拷锟截革拷锟侥讹拷锟斤拷锟斤拷
				} else {
					login2Object.setObject(null);
				}
				out.setMessage(login2Object); */

//				System.out.println(MyDate.getDateCN() + "user"
//						+ loginUser.getId() + " is online");
//				break;
//			case LOGOUT:// 锟斤拷锟斤拷锟斤拷顺锟斤拷锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷状态锟斤拷同时群锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫伙拷
//				User logoutUser = (User) read_tranObject.getObject();
//				int offId = logoutUser.getId();
//				System.out
//						.println(MyDate.getDateCN() + " 锟矫伙拷锟斤拷" + offId + " 锟斤拷锟斤拷锟斤拷");
//				dao.logout(offId);
//				isStart = false;// 锟斤拷锟斤拷锟皆硷拷锟侥讹拷循锟斤拷
//				map.remove(offId);// 锟接伙拷锟斤拷锟斤拷叱锟斤拷锟斤拷瞥锟�
//				out.setMessage(null);// 锟斤拷要锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷息去锟斤拷锟斤拷写锟竭筹拷
//				out.setStart(false);// 锟劫斤拷锟斤拷写锟竭筹拷循锟斤拷
//
//				TranObject<User> offObject = new TranObject<User>(
//						TranObjectType.LOGOUT);
//				User logout2User = new User();
//				logout2User.setId(logoutUser.getId());
//				offObject.setObject(logout2User);
//				for (OutputThread offOut : map.getAll()) {// 锟姐播锟矫伙拷锟斤拷锟斤拷锟斤拷息
//					offOut.setMessage(offObject);
//				}
//				break;
			
//			case REFRESH:
//				List<User> refreshList = dao.refresh(read_tranObject
//						.getFromUser());
//				TranObject<List<User>> refreshO = new TranObject<List<User>>(
//						TranObjectType.REFRESH);
//				refreshO.setObject(refreshList);
//				out.setMessage(refreshO);
//				break;
//			case CHOICEA:
//				if(Constants.switch1==1){
//				User studenta = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studenta.getId());
//				}
//				break;
//			case CHOICEB:
//				if(Constants.switch1==1){
//				User studentb = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studentb.getId());
//				}
//				break;
//			case CHOICEC:
//				if(Constants.switch1==1){
//				User studentc = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studentc.getId());
//				}
//				break;
//			case CHOICED:
//				if(Constants.switch1==1){
//				User studentd = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studentd.getId());
//				}
//				break;
//			case CHOICEE:
//				if(Constants.switch1==1){
//				User studente = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studente.getId());
//				}
//				break;
			case NEXTQ:
				User student = (User) read_tranObject.getObject();
				Constants.questionnum+=1;
				Constants.switch1=1;
				 TranObject<User> onObject = new TranObject<User>(  
                         TranObjectType.NEXTQ); 
				out.setMessage(onObject);
				break;
			case CLOSEQ:
				Constants.switch1=0;
				 TranObject<User> on2Object = new TranObject<User>(  
                         TranObjectType.CLOSEQ);
				 out.setMessage(on2Object);
				break;
			default:
				break;
			}
		}
		//System.out.println("Did not enter IF -- exiting method");
	}
}
