package dao;

//public class UserDaoImpl implements UserDao {

	/*igore this CLASS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * (non-Javadoc)
	 * @see dao.UserDao#setanswer(int, int)
	 * @eirc
	 */
	
	
	/*
	@Override
	public void setanswer(int id,int num) {
		Connection con = DButil.connect();
		try {
			String sql = "update user "+num+"=1 where _id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOn(id);
		
			ps.executeUpdate("create table 'num'(id int, name varchar(80))");
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	
		
	
	}
	
	@Override
	public int register(User u) {
		int id;
		Connection con = DButil.connect();
		String sql1 = "insert into user(_name,_password,_email,_time) values(?,?,?,?)";
		String sql2 = "select _id from user";

		try {
			PreparedStatement ps = con.prepareStatement(sql1);
			ps.setString(1, u.getName());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, MyDate.getDateCN());
			int res = ps.executeUpdate();
			if (res > 0) {
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ResultSet rs = ps2.executeQuery();
				if (rs.last()) {
					id = rs.getInt("_id");
					createFriendtable(id);// 娉ㄩ敓鏂ゆ嫹鏅掗敓鏂ゆ嫹铻咁儚閿熸枻鎷烽敓鎻紮鎷烽敓鏂ゆ嫹閿熸枻鎷锋病閿熺禒d涓洪敓鏂ゆ嫹閿熸枻鎷疯皨?閿熸枻鎷烽敓鑺傝揪鎷疯棔閿熸枻鎷烽敓鏂ゆ嫹閿熻緝锟�
					return id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return Constants.REGISTER_FAIL;
	}

	@Override
	public ArrayList<User> login(User user) {
		Connection con = DButil.connect();
		String sql = "select * from user where _id=? and _password=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user.getId());
			ps.setString(2, user.getPassword());
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				setOnline(user.getId());// 閿熸枻鎷烽敓閾版唻鎷风姸鎬佷负閿熸枻鎷烽敓鏂ゆ嫹
				ArrayList<User> refreshList = refresh(user.getId());
				return null;
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}
	*/
	
	
	/**
	 * 閿熸枻鎷烽敓鏂ゆ嫹閿熺殕纭锋嫹
	 */
	
	/*
	public User findMe(int id) {
		User me = new User();
		Connection con = DButil.connect();
		String sql = "select * from user where _id=?";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				me.setId(rs.getInt("_id"));
				me.setEmail(rs.getString("_email"));
				me.setName(rs.getString("_name"));
				me.setImg(rs.getInt("_img"));
			}
			return me;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

	public ArrayList<User> refresh(int id) {
		ArrayList<User> list = new ArrayList<User>();
		User me = findMe(id);
		list.add(me);// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风害閿燂拷
		Connection con = DButil.connect();
		String sql = "select * from _? ";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				do {
					User friend = new User();
					friend.setId(rs.getInt("_qq"));
					friend.setName(rs.getString("_name"));
					friend.setIsOnline(rs.getInt("_isOnline"));
					friend.setImg(rs.getInt("_img"));
					friend.setGroup(rs.getInt("_group"));
					list.add(friend);
				} while (rs.next());
			}
			return list;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

	
	public void setOnline(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update user set _isOnline=1 where _id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOn(id);// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙唻鎷风姸鎬佷负閿熸枻鎷烽敓鏂ゆ嫹
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	
	public void createFriendtable(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "create table _" + id
					+ " (_id int auto_increment not null primary key,"
					+ "_name varchar(20) not null,"
					+ "_isOnline int(11) not null default 0,"
					+ "_group int(11) not null default 0,"
					+ "_qq int(11) not null default 0,"
					+ "_img int(11) not null default 0)";
			PreparedStatement ps = con.prepareStatement(sql);
			int res = ps.executeUpdate();
			System.out.println(res);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}
	*/

	//@Override
	/**
	 * 閿熸枻鎷烽敓绔潻鎷烽敓鏂ゆ嫹鐘舵�佷负閿熸枻鎷烽敓鏂ゆ嫹
	 */
	
	/*
	public void logout(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update user set _isOnline=0 where _id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOff(id);
			// System.out.println(res);
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	
	public void updateAllOff(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update _? set _isOnline=0 where _qq=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for (int offId : getAllId()) {
				ps.setInt(1, offId);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	/**
	 * 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺煫浼欐嫹鐘舵�佷负閿熸枻鎷烽敓鏂ゆ嫹
	 * 
	 * @param id
	 */
	
	/*
	public void updateAllOn(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update _? set _isOnline=1 where _qq=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for (int OnId : getAllId()) {
				ps.setInt(1, OnId);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	public List<Integer> getAllId() {
		Connection con = DButil.connect();
		List<Integer> list = new ArrayList<Integer>();
		try {
			String sql = "select _id from user";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				do {
					int id = rs.getInt("_id");
					list.add(id);
				} while (rs.next());
			}
			// System.out.println(list);
			return list;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

	public static void main(String[] args) {
		User u = new User();
		UserDaoImpl dao = new UserDaoImpl();
	
	
		// u.setPassword("123456");
		// u.setEmail("153342219@qq.com");
		// System.out.println(dao.register(u));
		
		// u.setName("閿熸枻鎷烽敓锟�);
		// u.setPassword("123456");
		// u.setEmail("153342229@qq.com");
		// System.out.println(dao.register(u));

		
//		 u.setId(2031);
//		 u.setPassword("123456");
//		 System.out.println(dao.login(u));
		
		
		 //妯￠敓鏂ゆ嫹娴呴敓锟�
		 dao.logout(2018);
		 System.out.println("=================");
		 
		// dao.setOnline(2016);
		// dao.getAllId();
		// List<User> list = dao.refresh(2016);
		// System.out.println(list);

	}

}
*/