package chat;

import java.io.Serializable;

/**
 * 
 * @author Eric
 *
 */
public class ChatMsgEntity  implements Serializable {
	private String name;
	private String date;
	private String message;
	private int img;
	private boolean isComMeg = true;
	private String colour;

	public ChatMsgEntity() {

	}

	public ChatMsgEntity(String name, String date, String text, int img,
			boolean isComMsg) {
		super();
		this.name = name;
		this.date = date;
		this.message = text;
		this.img = img;
		this.isComMeg = isComMsg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}
	public void setColour(String colour) {
		this.colour=colour;
	}
	public String getColour() {
		return colour;
	}


}
