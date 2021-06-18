package com.weather.app.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.weather.app.models.UserAccount;

public class UserAccountMapper implements RowMapper<UserAccount> {

	@Override
	public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		UserAccount userAccount = new UserAccount();
		userAccount.setUserName(rs.getString("username"));
		userAccount.setPassword(rs.getString("password"));
		return userAccount;
	}

}
