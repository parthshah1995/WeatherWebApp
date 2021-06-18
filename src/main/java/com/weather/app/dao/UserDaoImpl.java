package com.weather.app.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.slf4j.*;
import com.weather.app.dao.daoInterfaces.UserDao;
import com.weather.app.dao.mappers.UserAccountMapper;
import com.weather.app.models.UserAccount;

@Repository
public class UserDaoImpl implements UserDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DataSourceTransactionManager transactionManager;

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public UserAccount createUserAccount(UserAccount user) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		org.springframework.transaction.TransactionStatus transactionStatus = transactionManager.getTransaction(def);

		try {
			String SQL = "INSERT INTO public.user_profile(username, password) VALUES (?, ?)";
			jdbcTemplate.update(SQL, user.getUserName(), user.getPassword());
			SQL = "INSERT INTO public.authority(username, role)VALUES (?, ?)";
			jdbcTemplate.update(SQL, user.getUserName(), "ROLE_USER");
			logger.info("Create User Name=" + user.getUserName());
			transactionManager.commit(transactionStatus);
		} catch (DataAccessException e) {
			System.out.println("Error in creating User Account Record, rolling back");
			transactionManager.rollback(transactionStatus);
			throw e;
		}

		return user;
	}

	@Override
	public UserAccount getUserByUserName(String username) {
		UserAccount userAccount = new UserAccount();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		org.springframework.transaction.TransactionStatus transactionStatus = transactionManager.getTransaction(def);
		try {
			String SQL = "SELECT username, password FROM public.user_profile where username=?";
			userAccount = jdbcTemplate.queryForObject(SQL, new Object[] { username }, new UserAccountMapper());
			logger.info("Retrived user Name=" + userAccount.getUserName());
			transactionManager.commit(transactionStatus);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return userAccount;
	}

}