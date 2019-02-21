package tranObject;

import java.io.Serializable;

import java.util.List;

/**
 * testing 
 * 
 *
 * @author eric
 */
public class TranObject<T> implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private TranObjectType type; 

	private int fromUser;
	private int toUser;

	private T object;
	private List<Integer> group;

	public TranObject(TranObjectType type) {
		this.type = type;
	}

	public int getFromUser() {
		return fromUser;
	}

	public void setFromUser(int fromUser) {
		this.fromUser = fromUser;
	}

	public int getToUser() {
		return toUser;
	}

	public void setToUser(int toUser) {
		this.toUser = toUser;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public TranObjectType getType() {
		return type;
	}
}
