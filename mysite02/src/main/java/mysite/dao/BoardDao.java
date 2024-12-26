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
				+ "FROM board ORDER BY group_no DESC, order_no ASC, no DESC";

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

		String getMaxGroupSql = "SELECT COALESCE(MAX(group_no), 0) + 1 FROM board";
		String insertSql = "INSERT INTO board (title, content, author, hits, group_no, order_no, depth, reg_date) "
				+ "VALUES (?, ?, ?, 0, ?, 1, 0, NOW())";

		try (Connection conn = getConnection()) {
			Long newGroupNo = 0L;

			try (PreparedStatement pstmt = conn.prepareStatement(getMaxGroupSql); ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					newGroupNo = rs.getLong(1);
					System.out.println("New group_no: " + newGroupNo);

				}
			}

			try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContent());
				pstmt.setString(3, vo.getAuthor());
				pstmt.setLong(4, newGroupNo);

				count = pstmt.executeUpdate();
			}

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

	public BoardVo findByNo(Long no) {
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

	public List<BoardVo> search(String keyword) {
		List<BoardVo> list = new ArrayList<>();

		String sql = "SELECT no, title, content, author, hits, group_no, order_no, depth, reg_date " + "FROM board "
				+ "WHERE title LIKE ? OR content LIKE ? " + "ORDER BY no DESC";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			String searchKeyword = "%" + keyword + "%";

			pstmt.setString(1, searchKeyword);
			pstmt.setString(2, searchKeyword);

			try (ResultSet rs = pstmt.executeQuery()) {

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
					vo.setReg_date(rs.getString("reg_date"));

					list.add(vo);
				}
			}

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		return list;
	}

	public int reply(BoardVo vo) {
		int count = 0;
		Connection conn = null;

		String updateOrderSql = "UPDATE board SET order_no = order_no + 1 WHERE group_no = ? AND order_no >= ?";
		String insertReplySql = "INSERT INTO board (title, content, author, hits, group_no, order_no, depth, reg_date) "
				+ "VALUES (?, ?, ?, 0, ?, ?, ?, NOW())";
		try {
			conn = getConnection();
			conn.setAutoCommit(false);

			try (PreparedStatement pstmt = conn.prepareStatement(updateOrderSql)) {
				pstmt.setLong(1, vo.getGroup_no());
				pstmt.setLong(2, vo.getOrder_no());
				pstmt.executeUpdate();
			}

			try (PreparedStatement pstmt = conn.prepareStatement(insertReplySql)) {
				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContent());
				pstmt.setString(3, vo.getAuthor());
				pstmt.setLong(4, vo.getGroup_no());
				pstmt.setLong(5, vo.getOrder_no());
				pstmt.setLong(6, vo.getDepth());
				count = pstmt.executeUpdate();
			}

			conn.commit();

		} catch (SQLException e) {
			System.out.println("error: " + e);

			if (conn != null) {
				try {
					conn.rollback(); // 트랜잭션 롤백

				} catch (SQLException rollbackEx) {
					System.out.println("rollback error: " + rollbackEx);
				}
			}

		} finally {
			if (conn != null) {
				try {
					conn.close(); // 연결 해제
				} catch (SQLException closeEx) {
					System.out.println("close error: " + closeEx);
				}
			}
		}

		return count;
	}

	public int getTotalCount() {
		int totalCount = 0;

		String sql = "SELECT COUNT(*) FROM board";

		try (Connection conn = getConnection();
				PreparedStatement psmt = conn.prepareStatement(sql);
				ResultSet rs = psmt.executeQuery()) {

			if (rs.next()) {
				totalCount = rs.getInt(1);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return totalCount;
	}

	public List<BoardVo> findBoardList(int currentPage, int pageSize) {
		List<BoardVo> list = new ArrayList<>();

		String sql = "SELECT no, title, content, author, hits, date_format(reg_date,'%Y-%m-%d %h:%i:%s') as reg_date_format, group_no, order_no, depth "
				+ "FROM board "
				+ "ORDER BY group_no DESC, order_no ASC LIMIT ?,?;";

		try (Connection conn = getConnection(); PreparedStatement psmt = conn.prepareStatement(sql)) {
			psmt.setInt(1, (currentPage - 1) * pageSize);
			psmt.setInt(2, pageSize);

			try (ResultSet rs = psmt.executeQuery()) {

				while (rs.next()) {
					BoardVo board = new BoardVo();
					board.setNo(rs.getLong(1));
					board.setTitle(rs.getString(2));
					board.setContent(rs.getString(3));
					board.setAuthor(rs.getString(4));
					board.setHits(rs.getLong(5));
					board.setReg_date(rs.getString(6));
					board.setGroup_no(rs.getLong(7));
					board.setOrder_no(rs.getLong(8));
					board.setDepth(rs.getLong(9));

					list.add(board);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
