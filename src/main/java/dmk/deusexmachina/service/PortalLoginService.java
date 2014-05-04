package dmk.deusexmachina.service;

import java.util.List;

public interface PortalLoginService {
	public List<String> userNameToLogin(String[] userNames);
	public String[] checkUsersStatus(String html, String[] users);
	public String loginHomePage();
	public boolean checkIn(String userName);
}