package com.weather.app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UderDaoImpl implements UderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Collection<studentModel> getAllStudent() {
		System.out.println(new BCryptPasswordEncoder().encode("parth"));

		List<studentModel> students = jdbcTemplate.query("SELECT * FROM public.student ORDER BY id ASC",
				new RowMapper<studentModel>() {
					@Override
					public studentModel mapRow(ResultSet resultSet, int i) throws SQLException {
						studentModel student = new studentModel();
						student.setId(resultSet.getInt("id"));
						student.setName(resultSet.getString("name"));
						return student;
					}

				});
		// TODO Auto-generated method stub
		return students;
	}

}
