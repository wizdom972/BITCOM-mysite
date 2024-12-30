package mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import mysite.vo.BoardVo;

@Repository
public class BoardRepository {

    // 페이지당 게시물 개수 고정
    private static final int PAGE_SIZE = 5;

    /**
     * DB 연결
     */
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

    /**
     * [1] 게시글 목록 조회 (검색 + 페이징)
     *  - 한 페이지에 최대 PAGE_SIZE(=5)개의 게시물만 보여줌
     *  - keyword가 null/빈문자열이면 검색조건 없이 전체 조회
     *  - 정렬: group_no DESC, order_no ASC
     */
    public List<BoardVo> findList(String keyword, int currentPage) {
        List<BoardVo> result = new ArrayList<>();

        // 기본 SQL
        String sql = 
            " SELECT no, title, content, author, hits, " +
            "        group_no, order_no, depth, " +
            "        DATE_FORMAT(reg_date, '%Y-%m-%d %H:%i:%s') AS reg_date_format " +
            "   FROM board ";

        // 검색 조건
        boolean hasKeyword = (keyword != null && !"".equals(keyword.trim()));
        if (hasKeyword) {
            sql += " WHERE title LIKE ? OR content LIKE ? ";
        }

        // 정렬
        sql += " ORDER BY group_no DESC, order_no ASC ";

        // LIMIT (고정 PAGE_SIZE)
        sql += " LIMIT ?, ? ";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;

            // 검색 키워드 바인딩
            if (hasKeyword) {
                String searchKeyword = "%" + keyword + "%";
                pstmt.setString(paramIndex++, searchKeyword);
                pstmt.setString(paramIndex++, searchKeyword);
            }

            // 페이징 바인딩: startIndex, PAGE_SIZE
            int startIndex = (currentPage - 1) * PAGE_SIZE;
            pstmt.setInt(paramIndex++, startIndex);
            pstmt.setInt(paramIndex++, PAGE_SIZE);

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
                    vo.setReg_date(rs.getString("reg_date_format"));

                    result.add(vo);
                }
            }
        } catch (SQLException e) {
            System.out.println("error:" + e);
        }

        return result;
    }

    /**
     * [2] 총 게시글 수(검색 포함)
     *  - keyword 없으면 전체 count, 있으면 검색 count
     */
    public int getTotalCount(String keyword) {
        int totalCount = 0;

        String sql = " SELECT COUNT(*) FROM board ";
        boolean hasKeyword = (keyword != null && !"".equals(keyword.trim()));
        if (hasKeyword) {
            sql += " WHERE title LIKE ? OR content LIKE ? ";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (hasKeyword) {
                String searchKeyword = "%" + keyword + "%";
                pstmt.setString(1, searchKeyword);
                pstmt.setString(2, searchKeyword);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("error:" + e);
        }

        return totalCount;
    }

    /**
     * [3] 게시글 상세조회
     *  - userNo가 있으면 (no + userNo)로 조회
     *  - 없으면 no만 조회
     */
    public BoardVo findByNo(Long no, Long userNo) {
        BoardVo vo = null;

        String sql = 
            " SELECT no, title, content, author, hits, group_no, order_no, depth, reg_date " +
            "   FROM board " +
            "  WHERE no = ? ";

        if (userNo != null) {
            sql += " AND user_no = ? ";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, no);
            if (userNo != null) {
                pstmt.setLong(2, userNo);
            }

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
            System.out.println("error:" + e);
        }

        return vo;
    }

    /**
     * [4] 새 글(원글) 작성
     *  - group_no: max(group_no) + 1
     *  - order_no: 1
     *  - depth: 0
     */
    public int insert(BoardVo vo) {
        int count = 0;

        String sqlMaxGroup = " SELECT COALESCE(MAX(group_no), 0) + 1 FROM board ";
        String sqlInsert = 
            " INSERT INTO board (title, content, author, hits, group_no, order_no, depth, reg_date, user_no) " +
            " VALUES (?, ?, ?, 0, ?, 1, 0, NOW(), ?)";

        try (Connection conn = getConnection()) {
            // 1) 새 group_no 구하기
            Long newGroupNo = 0L;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMaxGroup);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    newGroupNo = rs.getLong(1);
                }
            }

            // 2) INSERT
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, vo.getTitle());
                pstmt.setString(2, vo.getContent());
                pstmt.setString(3, vo.getAuthor());
                pstmt.setLong(4, newGroupNo);
                // user_no가 vo에 있다고 가정(필드명에 따라 변경)
                pstmt.setLong(5, 0L); 
                count = pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("error:" + e);
        }

        return count;
    }

    /**
     * [5] 답글 작성
     *  - 1) 같은 group_no 에서 order_no >= vo.order_no 인 것 +1
     *  - 2) INSERT
     *  - 트랜잭션
     */
    public int reply(BoardVo vo) {
        int count = 0;

        String sqlUpdateOrder = 
            " UPDATE board " +
            "    SET order_no = order_no + 1 " +
            "  WHERE group_no = ? " +
            "    AND order_no >= ? ";

        String sqlInsertReply = 
            " INSERT INTO board (title, content, author, hits, group_no, order_no, depth, reg_date, user_no) " +
            " VALUES (?, ?, ?, 0, ?, ?, ?, NOW(), ?)";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 1) order_no 밀기
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateOrder)) {
                pstmt.setLong(1, vo.getGroup_no());
                pstmt.setLong(2, vo.getOrder_no());
                pstmt.executeUpdate();
            }

            // 2) 답글 Insert
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertReply)) {
                pstmt.setString(1, vo.getTitle());
                pstmt.setString(2, vo.getContent());
                pstmt.setString(3, vo.getAuthor());
                pstmt.setLong(4, vo.getGroup_no());
                pstmt.setLong(5, vo.getOrder_no());
                pstmt.setLong(6, vo.getDepth());
                pstmt.setLong(7, 0L);
                count = pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("error:" + e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("rollback error:" + ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("close error:" + ex);
                }
            }
        }

        return count;
    }

    /**
     * [6] 게시글 수정
     */
    public int update(BoardVo vo) {
        int count = 0;

        String sql = 
            " UPDATE board " +
            "    SET title = ?, content = ? " +
            "  WHERE no = ? ";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vo.getTitle());
            pstmt.setString(2, vo.getContent());
            pstmt.setLong(3, vo.getNo());

            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error:" + e);
        }

        return count;
    }

    /**
     * [7] 게시글 삭제
     *  - userNo != null -> no+userNo 모두 매칭
     *  - null -> no만 매칭
     */
    public int delete(Long no, Long userNo) {
        int count = 0;

        String sql = 
            " DELETE FROM board " +
            "  WHERE no = ? ";

        if (userNo != null) {
            sql += " AND user_no = ? ";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, no);
            if (userNo != null) {
                pstmt.setLong(2, userNo);
            }

            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error:" + e);
        }

        return count;
    }

    /**
     * [8] 조회수 증가
     */
    public int incrementHit(Long no) {
        int count = 0;

        String sql = 
            " UPDATE board " +
            "    SET hits = hits + 1 " +
            "  WHERE no = ? ";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, no);
            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error:" + e);
        }

        return count;
    }
}
