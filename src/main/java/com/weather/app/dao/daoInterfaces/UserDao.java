package com.weather.app.dao.daoInterfaces;


import com.weather.app.models.UserAccount;

public interface UserDao {

	public UserAccount createUserAccount(UserAccount user);

	public UserAccount getUserByUserName(String username);

}
