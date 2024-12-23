package mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mysite.vo.BoardVo;

public class BoardDao {

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

	public List<BoardVo> findAll() {
		List<BoardVo> list = new ArrayList<>();

		String sql = "SELECT no, title, content, author, hits, group_no, order_no, depth, reg_date "
				+ "FROM board ORDER BY group_no DESC, order_no ASC";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				BoardVo vo = new BoardVo();
				
				vo.setNo(rs.getLong("no"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setAuthor(rs.getString("author"));
				vo.setHits(rs.getLong("hits"));
				vo.setGroup_no(rs.getLong("group_no"));
				vo.setOrder_no(rs.getLong("order_no"));
				vo.setDepth(rs.getLong("depth"));
				vo.setReg_date(rs.getDate("reg_date").toString());

				list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return list;
	}

	public int insert(BoardVo vo) {
		int count = 0;

		String sql = "INSERT INTO board (title, content, author, hits, group_no, order_no, depth, reg_date) "
				+ "VALUES (?, ?, ?, 0, ?, ?, ?, NOW())";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getAuthor());
			pstmt.setLong(4, vo.getGroup_no());
			pstmt.setLong(5, vo.getOrder_no());
			pstmt.setLong(6, vo.getDepth());

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return count;
	}

	public int update(BoardVo vo) {

		int count = 0;

		String sql = "UPDATE board SET title = ?, content = ? WHERE no = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getNo());

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return count;
	}

	public int delete(Long no) {
		int count = 0;

		String sql = "DELETE FROM board WHERE no = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setLong(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return count;
	}

	public BoardVo findById(Long no) {
		BoardVo vo = null;

		String sql = "SELECT no, title, content, author, hits, group_no, order_no, depth, reg_date "
				+ "FROM board WHERE no = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setLong(1, no);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {
					vo = new BoardVo();

					vo.setNo(rs.getLong("no"));
					vo.setTitle(rs.getString("title"));
					vo.setContent(rs.getString("content"));
					vo.setAuthor(rs.getString("author"));
					vo.setHits(rs.getLong("hits"));
					vo.setGroup_no(rs.getLong("group_no"));
					vo.setOrder_no(rs.getLong("order_no"));
					vo.setDepth(rs.getLong("depth"));
					vo.setReg_date(rs.getString("reg_date"));
				}
			}

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return vo;
	}

	public int incrementHit(Long no) {
		int count = 0;

		String sql = "UPDATE board SET hits = hits + 1 WHERE no = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setLong(1, no);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return count;
	}
}
