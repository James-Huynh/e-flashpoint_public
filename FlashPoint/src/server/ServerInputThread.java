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


public class ServerInputThread extends Thread {
	private Socket socket;// socket锟斤拷锟斤拷
	private OutputThread out;// 锟斤拷锟捷斤拷锟斤拷锟斤拷写锟斤拷息锟竭程ｏ拷锟斤拷为锟斤拷锟斤拷要锟斤拷锟矫伙拷锟截革拷锟斤拷息锟斤拷
	private OutputThreadMap map;// 写锟斤拷息锟竭程伙拷锟斤拷锟斤拷
	private ObjectInputStream ois;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private boolean isStart = true;// 锟角凤拷循锟斤拷锟斤拷锟斤拷息

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
		try {
			while (isStart) {
				// 锟斤拷取锟斤拷息
				readMessage();
			}
			if (ois != null)
				ois.close();
			if (socket != null)
				socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 锟斤拷锟斤拷息锟皆硷拷锟斤拷锟斤拷锟斤拷息锟斤拷锟阶筹拷锟届常
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readMessage() throws IOException, ClassNotFoundException {
		System.out.println("server loginUser123:");
		Object readObject = ois.readObject();// 锟斤拷锟斤拷锟叫讹拷取锟斤拷锟斤拷
		UserDao dao = UserDaoFactory.getInstance();// 通锟斤拷dao模式锟斤拷锟斤拷锟教�
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;// 转锟斤拷锟缴达拷锟斤拷锟斤拷锟�
			switch (read_tranObject.getType()) {
			case REGISTER:// 锟斤拷锟斤拷没锟斤拷锟阶拷锟�
				User registerUser = (User) read_tranObject.getObject();
				int registerResult = dao.register(registerUser);
				System.out.println(MyDate.getDateCN() + " 锟斤拷锟矫伙拷注锟斤拷:"
						+ registerResult);
				// 锟斤拷锟矫伙拷锟截革拷锟斤拷息
				TranObject<User> register2TranObject = new TranObject<User>(
						TranObjectType.REGISTER);
				User register2user = new User();
				register2user.setId(registerResult);
				register2TranObject.setObject(register2user);
				out.setMessage(register2TranObject);
				break;
			case LOGIN:
				System.out.println("server loginUser:");
				User loginUser = (User) read_tranObject.getObject();
				System.out.println("server loginUser:"+loginUser);
				
				if(loginUser.getName() == "Zaid") {
					TranObject<User> o = new TranObject<User>(TranObjectType.SUCCESS);
					User u = new User();
					

					o.setObject(u);
					out.setMessage(o);
				}
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

				System.out.println(MyDate.getDateCN() + "user"
						+ loginUser.getId() + " is online");
				break;
			case LOGOUT:// 锟斤拷锟斤拷锟斤拷顺锟斤拷锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷状态锟斤拷同时群锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫伙拷
				User logoutUser = (User) read_tranObject.getObject();
				int offId = logoutUser.getId();
				System.out
						.println(MyDate.getDateCN() + " 锟矫伙拷锟斤拷" + offId + " 锟斤拷锟斤拷锟斤拷");
				dao.logout(offId);
				isStart = false;// 锟斤拷锟斤拷锟皆硷拷锟侥讹拷循锟斤拷
				map.remove(offId);// 锟接伙拷锟斤拷锟斤拷叱锟斤拷锟斤拷瞥锟�
				out.setMessage(null);// 锟斤拷要锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷息去锟斤拷锟斤拷写锟竭筹拷
				out.setStart(false);// 锟劫斤拷锟斤拷写锟竭筹拷循锟斤拷

				TranObject<User> offObject = new TranObject<User>(
						TranObjectType.LOGOUT);
				User logout2User = new User();
				logout2User.setId(logoutUser.getId());
				offObject.setObject(logout2User);
				for (OutputThread offOut : map.getAll()) {// 锟姐播锟矫伙拷锟斤拷锟斤拷锟斤拷息
					offOut.setMessage(offObject);
				}
				break;
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
			case REFRESH:
				List<User> refreshList = dao.refresh(read_tranObject
						.getFromUser());
				TranObject<List<User>> refreshO = new TranObject<List<User>>(
						TranObjectType.REFRESH);
				refreshO.setObject(refreshList);
				out.setMessage(refreshO);
				break;
			case CHOICEA:
				if(Constants.switch1==1){
				User studenta = (User) read_tranObject.getObject();
				dao.setanswer(Constants.questionnum,studenta.getId());
				}
				break;
			case CHOICEB:
				if(Constants.switch1==1){
				User studentb = (User) read_tranObject.getObject();
				dao.setanswer(Constants.questionnum,studentb.getId());
				}
				break;
			case CHOICEC:
				if(Constants.switch1==1){
				User studentc = (User) read_tranObject.getObject();
				dao.setanswer(Constants.questionnum,studentc.getId());
				}
				break;
			case CHOICED:
				if(Constants.switch1==1){
				User studentd = (User) read_tranObject.getObject();
				dao.setanswer(Constants.questionnum,studentd.getId());
				}
				break;
			case CHOICEE:
				if(Constants.switch1==1){
				User studente = (User) read_tranObject.getObject();
				dao.setanswer(Constants.questionnum,studente.getId());
				}
				break;
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
	}
}
