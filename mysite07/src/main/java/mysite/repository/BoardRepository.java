package mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mysite.vo.BoardVo;

@Repository
public class BoardRepository {

    @Autowired
    private SqlSession sqlSession;

    public int insert(BoardVo boardVo) {
        return sqlSession.insert("board.insert", boardVo);
    }

    public List<BoardVo> findAllByPageAndKeword(String keyword, Integer page, Integer size) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        map.put("startIndex", (page - 1) * size);
        map.put("size", size);

        return sqlSession.selectList("board.findAllByPageAndKeword", map);
    }

    public int update(BoardVo boardVo) {
        return sqlSession.update("board.update", boardVo);
    }

    public int delete(Long no, Long userId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("no", no);
        map.put("userId", userId);

        return sqlSession.delete("board.delete", map);
    }

    public BoardVo findById(Long no) {
        return sqlSession.selectOne("board.findById", no);
    }

    public BoardVo findByIdAndUserId(Long no, Long userId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("no", no);
        map.put("userId", userId);
        
        return sqlSession.selectOne("board.findByIdAndUserId", map);
    }

    public int updateHit(Long no) {
        return sqlSession.update("board.updateHit", no);
    }

    public int updateOrderNo(Integer groupNo, Integer orderNo) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("groupNo", groupNo);
        map.put("orderNo", orderNo);

        return sqlSession.update("board.updateOrederNo", map);
    }

    public int getTotalCount(String keyword) {
        return sqlSession.selectOne("board.totalCount", keyword);
    }
}