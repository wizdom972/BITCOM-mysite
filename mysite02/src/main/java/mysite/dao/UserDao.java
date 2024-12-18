package mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mysite.vo.UserVo;

public class UserDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.123:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

		} catch (ClassNotFoundException e) {
			System.out.println("Driver loading error: " + e);
		}

		return conn;
	}

	public boolean insert(UserVo vo) {
		boolean result = false;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT INTO user(name, email, password, gender, join_date) VALUES (?, ?, ?, ?, now())",
						Statement.RETURN_GENERATED_KEYS);) {

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			result = pstmt.executeUpdate() == 1;

			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					vo.setId(rs.getLong(1)); // 생성된 no를 VO에 설정
				}
			}

		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}

		return result;
	}

	public List<UserVo> findAll() {
		List<UserVo> result = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user");
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				UserVo vo = new UserVo();
				vo.setId(rs.getLong(1));
				vo.setName(rs.getString(2));
				vo.setEmail(rs.getString(3));
				vo.setPassword(rs.getString(4));
				vo.setGender(rs.getString(5));
				vo.setJoinDate(rs.getDate(6).toString());
				result.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}

		return result;
	}

	public boolean deleteById(Long id) {
		boolean result = false;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user WHERE id = ?")) {

			pstmt.setLong(1, id);
			result = pstmt.executeUpdate() == 1;

		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}

		return result;
	}
}
