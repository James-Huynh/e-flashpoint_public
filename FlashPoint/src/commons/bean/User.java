package commons.bean;

import java.io.Serializable;

import game.GameState;


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
    private boolean isRegistered;
    private int[] coords;
    private boolean placed;
    private actions.Action action;

	public Integer getId() {
		return id;
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
