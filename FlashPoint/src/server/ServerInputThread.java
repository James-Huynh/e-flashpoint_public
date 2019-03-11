package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import commons.util.Constants;
import dao.UserDao;
import commons.bean.TextMessage;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import commons.util.MyDate;
import dao.UserDaoFactory;
import game.GameState;
import lobby.Lobby;

public class ServerInputThread extends Thread {
	private Socket socket;// socket锟斤拷锟斤拷
	private OutputThread out;// 锟斤拷锟捷斤拷锟斤拷锟斤拷写锟斤拷息锟竭程ｏ拷锟斤拷为锟斤拷锟斤拷要锟斤拷锟矫伙拷锟截革拷锟斤拷息锟斤拷
	private OutputThreadMap map;// 写锟斤拷息锟竭程伙拷锟斤拷锟斤拷
	private ObjectInputStream ois;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private boolean isStart = true;// 锟角凤拷循锟斤拷锟斤拷锟斤拷息息
	ServerManager serverManager;

	public ServerInputThread(Socket socket, OutputThread out, OutputThreadMap map) {
		this.socket = socket;
		this.out = out;
		this.map = map;
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
		serverManager = new ServerManager();
		try {
			while (isStart) {
				System.out.println("Looping?");
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
		System.out.println("Insinde readMessage");
//		UserDao dao = UserDaoFactory.getInstance();// 通锟斤拷dao模式锟斤拷锟斤拷锟教�
		if (readObject != null && readObject instanceof TranObject) {
			System.out.println("Entered IF");
			TranObject read_tranObject = (TranObject) readObject;// 转锟斤拷锟缴达拷锟斤拷锟斤拷锟�
			TranObject<User> register2TranObject;
			User newUser;
			switch (read_tranObject.getType()) {
			case CONNECT:
				System.out.println("In connect request");
				register2TranObject = new TranObject<User>(TranObjectType.SUCCESS);
				newUser = (User) read_tranObject.getObject();
				newUser.setId(12345);
				register2TranObject.setObject(newUser);
				out.setMessage(register2TranObject);
				break;
			case LOGIN:
				TranObject<User> resultOfLogin = new TranObject<User>(TranObjectType.LOGINSUCCESS);
				User updatedUser = (User) read_tranObject.getObject();
				if(serverManager.getAccounts().get(updatedUser.getName()) != null) {
					System.out.println((String) serverManager.getAccounts().get(updatedUser.getName()));
				}
				if(serverManager.getAccounts().get(updatedUser.getName()).equals(updatedUser.getPassword())) {
					System.out.println("is online set to 1");
					updatedUser.setIsOnline(1);	
				}
				else {
					System.out.println("is online set to 0");
					updatedUser.setIsOnline(0);
				}
				resultOfLogin.setObject(updatedUser);
				out.setMessage(resultOfLogin);
				break;
			case REGISTER:
				System.out.println("check");
				TranObject<User> resultOfRegister = new TranObject<User>(TranObjectType.REGISTERSUCCESS);
				User updatedUserTwo = (User) read_tranObject.getObject();
				if(serverManager.getAccounts().containsKey(updatedUserTwo.getName())) {
					System.out.println("account already exists");
					updatedUserTwo.setIsRegistered(false);
				}else {
					System.out.println("account added");
					serverManager.getAccounts().put(updatedUserTwo.getName(), updatedUserTwo.getPassword());
					System.out.println(updatedUserTwo.getPassword());
					updatedUserTwo.setIsRegistered(true);
				}
				resultOfRegister.setObject(updatedUserTwo);
				out.setMessage(resultOfRegister);
				break;
			case GAMESTATEUPDATE:
				System.out.println("In game state update request");
				register2TranObject = new TranObject<User>(TranObjectType.SUCCESS);
				newUser = (User) read_tranObject.getObject();
				GameState gs = GameState.getInstance();
				Lobby lobby = new Lobby();
				gs.updateGameStateFromLobby(lobby);
				newUser.setCurrentState(gs);
				register2TranObject.setObject(newUser);
				out.setMessage(register2TranObject);
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
			case MESSAGE:// 锟斤拷锟斤拷锟阶拷锟斤拷锟较拷锟斤拷锟斤拷锟斤拷群锟斤拷锟斤拷
				// 锟斤拷取锟斤拷息锟斤拷要转锟斤拷锟侥讹拷锟斤拷id锟斤拷然锟斤拷锟饺★拷锟斤拷锟侥该讹拷锟斤拷锟叫达拷叱锟�
				int id2 = read_tranObject.getToUser();
				OutputThread toOut = map.getById(id2);
				if (toOut != null) {// 锟斤拷锟斤拷没锟斤拷锟斤拷锟�
					toOut.setMessage(read_tranObject);
				} else {// 锟斤拷锟轿拷眨锟剿碉拷锟斤拷没锟斤拷丫锟斤拷锟斤拷锟�,锟截革拷锟矫伙拷
					TextMessage text = new TextMessage();
					text.setMessage("锟阶ｏ拷锟皆凤拷锟斤拷锟斤拷锟斤拷哦锟斤拷锟斤拷锟斤拷锟较拷锟斤拷锟绞憋拷锟斤拷锟斤拷诜锟斤拷锟斤拷锟�");
					TranObject<TextMessage> offText = new TranObject<TextMessage>(
							TranObjectType.MESSAGE);
					offText.setObject(text);
					offText.setFromUser(0);
					out.setMessage(offText);
				}
				break;
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
		System.out.println("Did not enter IF -- exiting method");
	}

	/*
	 * public void readMessage() throws IOException, ClassNotFoundException {
	 * System.out.println("server loginUser1234:"); Object readObject =
	 * ois.readObject(); System.out.println("Here?");
	 * 
	 * if (readObject != null && readObject instanceof TranObject) {
	 * System.out.println("Entered IF"); TranObject read_tranObject = (TranObject)
	 * readObject; switch (read_tranObject.getType()) { case CONNECT:
	 * TranObject<User> register2TranObject = new
	 * TranObject<User>(TranObjectType.SUCCESS); User newUser = (User)
	 * read_tranObject.getObject(); newUser.setId(12345);
	 * register2TranObject.setObject(newUser); out.setMessage(register2TranObject);
	 * break; // case REGISTER:// 锟斤拷锟斤拷没锟斤拷锟阶拷锟� // User registerUser = (User)
	 * read_tranObject.getObject(); //// int registerResult =
	 * dao.register(registerUser); // System.out.println(MyDate.getDateCN() +
	 * " 锟斤拷锟矫伙拷注锟斤拷:" //// + registerResult); // // 锟斤拷锟矫伙拷锟截革拷锟斤拷息 //
	 * TranObject<User> register2TranObject = new TranObject<User>( //
	 * TranObjectType.REGISTER); // User register2user = new User(); ////
	 * register2user.setId(registerResult); //
	 * register2TranObject.setObject(register2user); //
	 * out.setMessage(register2TranObject); // break; case LOGIN:
	 * System.out.println("server loginUser:"); User loginUser = (User)
	 * read_tranObject.getObject();
	 * System.out.println("server loginUser:"+loginUser);
	 * 
	 * if(loginUser.getName() == "Zaid") { TranObject<User> o = new
	 * TranObject<User>(TranObjectType.LOGINSUCCESS); User u = new User();
	 * 
	 * 
	 * o.setObject(u); out.setMessage(o); } ArrayList<User> list =
	 * dao.login(loginUser); TranObject<ArrayList<User>> login2Object = new
	 * TranObject<ArrayList<User>>( TranObjectType.LOGIN); if (list != null) {//
	 * 锟斤拷锟斤拷录锟缴癸拷 TranObject<User> onObject = new TranObject<User>(
	 * TranObjectType.LOGIN); User login2User = new User();
	 * login2User.setId(loginUser.getId()); onObject.setObject(login2User); for
	 * (OutputThread onOut : map.getAll()) { onOut.setMessage(onObject);//
	 * 锟姐播一锟斤拷锟矫伙拷锟斤拷锟斤拷 } map.add(loginUser.getId(), out);//
	 * 锟饺广播锟斤拷锟劫把讹拷应锟矫伙拷id锟斤拷写锟竭程达拷锟斤拷map锟叫ｏ拷锟皆憋拷转锟斤拷锟斤拷息时锟斤拷锟斤拷
	 * login2Object.setObject(list);// 锟窖猴拷锟斤拷锟叫憋拷锟斤拷锟截革拷锟侥讹拷锟斤拷锟斤拷 } else {
	 * login2Object.setObject(null); } out.setMessage(login2Object);
	 * 
	 * System.out.println(MyDate.getDateCN() + "user" + loginUser.getId() +
	 * " is online"); break; // case LOGOUT://
	 * 锟斤拷锟斤拷锟斤拷顺锟斤拷锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷状态锟斤拷同时群锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫伙拷 // User
	 * logoutUser = (User) read_tranObject.getObject(); // int offId =
	 * logoutUser.getId(); // System.out // .println(MyDate.getDateCN() + " 锟矫伙拷锟斤拷"
	 * + offId + " 锟斤拷锟斤拷锟斤拷"); // dao.logout(offId); // isStart = false;//
	 * 锟斤拷锟斤拷锟皆硷拷锟侥讹拷循锟斤拷 // map.remove(offId);// 锟接伙拷锟斤拷锟斤拷叱锟斤拷锟斤拷瞥锟� //
	 * out.setMessage(null);// 锟斤拷要锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷息去锟斤拷锟斤拷写锟竭筹拷 //
	 * out.setStart(false);// 锟劫斤拷锟斤拷写锟竭筹拷循锟斤拷 // // TranObject<User> offObject =
	 * new TranObject<User>( // TranObjectType.LOGOUT); // User logout2User = new
	 * User(); // logout2User.setId(logoutUser.getId()); //
	 * offObject.setObject(logout2User); // for (OutputThread offOut : map.getAll())
	 * {// 锟姐播锟矫伙拷锟斤拷锟斤拷锟斤拷息 // offOut.setMessage(offObject); // } // break; case
	 * MESSAGE:// 锟斤拷锟斤拷锟阶拷锟斤拷锟较拷锟斤拷锟斤拷锟斤拷群锟斤拷锟斤拷 //
	 * 锟斤拷取锟斤拷息锟斤拷要转锟斤拷锟侥讹拷锟斤拷id锟斤拷然锟斤拷锟饺★拷锟斤拷锟侥该讹拷锟斤拷锟叫达拷叱锟� int id2 =
	 * read_tranObject.getToUser(); OutputThread toOut = map.getById(id2); if (toOut
	 * != null) {// 锟斤拷锟斤拷没锟斤拷锟斤拷锟� toOut.setMessage(read_tranObject); } else {//
	 * 锟斤拷锟轿拷眨锟剿碉拷锟斤拷没锟斤拷丫锟斤拷锟斤拷锟�,锟截革拷锟矫伙拷 TextMessage text = new TextMessage();
	 * text.setMessage("锟阶ｏ拷锟皆凤拷锟斤拷锟斤拷锟斤拷哦锟斤拷锟斤拷锟斤拷锟较拷锟斤拷锟绞憋拷锟斤拷锟斤拷诜锟斤拷锟斤拷锟�");
	 * TranObject<TextMessage> offText = new TranObject<TextMessage>(
	 * TranObjectType.MESSAGE); offText.setObject(text); offText.setFromUser(0);
	 * out.setMessage(offText); } break; // case REFRESH: // List<User> refreshList
	 * = dao.refresh(read_tranObject // .getFromUser()); // TranObject<List<User>>
	 * refreshO = new TranObject<List<User>>( // TranObjectType.REFRESH); //
	 * refreshO.setObject(refreshList); // out.setMessage(refreshO); // break; //
	 * case CHOICEA: // if(Constants.switch1==1){ // User studenta = (User)
	 * read_tranObject.getObject(); //
	 * dao.setanswer(Constants.questionnum,studenta.getId()); // } // break; // case
	 * CHOICEB: // if(Constants.switch1==1){ // User studentb = (User)
	 * read_tranObject.getObject(); //
	 * dao.setanswer(Constants.questionnum,studentb.getId()); // } // break; // case
	 * CHOICEC: // if(Constants.switch1==1){ // User studentc = (User)
	 * read_tranObject.getObject(); //
	 * dao.setanswer(Constants.questionnum,studentc.getId()); // } // break; // case
	 * CHOICED: // if(Constants.switch1==1){ // User studentd = (User)
	 * read_tranObject.getObject(); //
	 * dao.setanswer(Constants.questionnum,studentd.getId()); // } // break; // case
	 * CHOICEE: // if(Constants.switch1==1){ // User studente = (User)
	 * read_tranObject.getObject(); //
	 * dao.setanswer(Constants.questionnum,studente.getId()); // } // break; case
	 * NEXTQ: User student = (User) read_tranObject.getObject();
	 * Constants.questionnum+=1; Constants.switch1=1; TranObject<User> onObject =
	 * new TranObject<User>( TranObjectType.NEXTQ); out.setMessage(onObject); break;
	 * case CLOSEQ: Constants.switch1=0; TranObject<User> on2Object = new
	 * TranObject<User>( TranObjectType.CLOSEQ); out.setMessage(on2Object); break;
	 * default: break; } } System.out.println("Did not enter IF -- exiting method");
	 * 
	 * }
	 */
}
