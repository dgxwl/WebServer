package pac.vo;

public class UserInfo {
	private String username;
	private String password;
	private String nickname;
	private String phonenumber;
	
	public UserInfo() {
		
	}

	public UserInfo(String username, String password, String nickname, String phonenumber) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.phonenumber = phonenumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", password=" + password + ", nickname=" + nickname + ", phonenumber="
				+ phonenumber + "]";
	}
	
	
}
