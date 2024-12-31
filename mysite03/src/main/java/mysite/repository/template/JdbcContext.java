package mysite.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JdbcContext {

	@Autowired
	private DataSource dataSource;

	public <E> List<E> queryForList(String sql, RowMapper<E> rowMapper) {
		return queryForListWithStatmentStrategy(new StatementStrategy() {

			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				return connection.prepareStatement(sql);
			}
		}, rowMapper);
	}

	private <E> List<E> queryForListWithStatmentStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {

		List<E> result = new ArrayList<E>();

		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = statementStrategy.makeStatement(conn);
				ResultSet rs = pstmt.executeQuery();) {
			
			while (rs.next()) {
				E e = rowMapper.mapRow(rs, rs.getRow());
				result.add(e);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	public int excuteUpdate(String sql, Object[] parameters) {
		return excuteUpdateWithStatementStrategy(new StatementStrategy() {

			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {

				PreparedStatement pstmt = connection.prepareStatement(sql);

				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i + 1, parameters[i]);
				}

				return pstmt;
			}
		});
	}

	private int excuteUpdateWithStatementStrategy(StatementStrategy statementStrategy) {

		int count = 0;

		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = statementStrategy.makeStatement(conn);) {

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return count;
	}

}
