package com.weather.app.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.weather.app.dao.daoInterfaces.WeatherDao;
import com.weather.app.dao.mappers.WeatherDataMapper;
import com.weather.app.models.WeatherInfo;

@Repository
public class WeatherDaoImpl implements WeatherDao {
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
	public WeatherInfo addWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		org.springframework.transaction.TransactionStatus transactionStatus = transactionManager.getTransaction(def);
		try {
			String SQL = "INSERT INTO public.user_data(username, location)	VALUES (?, ?)";
			jdbcTemplate.update(SQL, weatherInfo.getUsername(), weatherInfo.getLocation());
			logger.info("Add Location =" + weatherInfo.getLocation());
			transactionManager.commit(transactionStatus);
		} catch (DataAccessException e) {
			System.out.println("Error in creating User Account Record, rolling back");
			transactionManager.rollback(transactionStatus);
			throw e;
		}

		return weatherInfo;
	}

	@Override
	public ArrayList<WeatherInfo> getAllWeatherInfoData(String userName) {
		ArrayList<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
		List<WeatherInfo> weatherInfosResult;
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		org.springframework.transaction.TransactionStatus transactionStatus = transactionManager.getTransaction(def);
		try {
			String SQL = "SELECT username, location FROM  public.user_data where username=?";
			weatherInfosResult = jdbcTemplate.query(SQL, new Object[] { userName }, new WeatherDataMapper());

			for (WeatherInfo weatherInfoData : weatherInfosResult) {
				weatherInfos.add(weatherInfoData);
			}
			transactionManager.commit(transactionStatus);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return weatherInfos;
	}

}
