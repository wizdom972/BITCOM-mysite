package mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import mysite.vo.UserVo;

@Repository
public class UserRepository {

	public int insert(UserVo vo) {
		int count = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("insert into user values(null, ?, ?, ?, ?, now())");) {
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());

			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return count;
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		UserVo userVo = null;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("select id, name from user where email=? and password=?");) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Long id = rs.getLong(1);
				String name = rs.getString(2);

				userVo = new UserVo();
				userVo.setId(id);
				userVo.setName(name);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return userVo;
	}

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");

			String url = "jdbc:mariadb://192.168.0.123:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}

		return conn;
	}

	public UserVo findById(Long id) {
		UserVo userVo = null;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select * from user where id=?");) {
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getLong(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);
				String gender = rs.getString(5);
				String joinDate = rs.getDate(6).toString();

				userVo = new UserVo();
				userVo.setId(id);
				userVo.setName(name);
				userVo.setEmail(email);
				userVo.setPassword(password);
				userVo.setGender(gender);
				userVo.setJoinDate(joinDate);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return userVo;
	}

	public int update(UserVo vo) {

		int count = 0;

		try (Connection conn = getConnection()) {
			String sql = "update user set name=?, gender=?";
			if (vo.getPassword() != null && !vo.getPassword().isEmpty()) {
				sql += ", password=?";
			}
			sql += " where id=?";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());

				if (vo.getPassword() != null && !vo.getPassword().isEmpty()) {
					pstmt.setString(3, vo.getPassword());
					pstmt.setLong(4, vo.getId());
				} else {
					pstmt.setLong(3, vo.getId());
				}

				count = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return count;

	}
}