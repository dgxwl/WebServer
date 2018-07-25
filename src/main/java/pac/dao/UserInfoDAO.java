package pac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pac.util.DBUtils;
import pac.vo.UserInfo;

public class UserInfoDAO {
	
	public boolean update(UserInfo userInfo) {
		return false;
	}
	
	public boolean save(UserInfo userInfo) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into user values (?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userInfo.getUsername());
			ps.setString(2, userInfo.getPassword());
			ps.setString(3, userInfo.getNickname());
			ps.setString(4, userInfo.getPhonenumber());
			ps.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeConnection(conn);
		}
		
		return false;
	}
	
	public UserInfo findByUsername(String username) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from user where username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String uname = rs.getString(1);
				String password = rs.getString(2);
				String nickname = rs.getString(3);
				String phonenumber = rs.getString(4);
				UserInfo userInfo = new UserInfo(uname, password, nickname, phonenumber);
				return userInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeConnection(conn);
		}
		
		return null;
	}
}
