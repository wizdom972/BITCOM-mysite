package mysite.repository;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import mysite.vo.UserVo;

@Repository
public class UserRepository {
	private SqlSession sqlSession;

	public UserRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public int insert(UserVo vo) {
		return sqlSession.insert("user.insert", vo);
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		return sqlSession.selectOne("user.findByEmailAndPassword", Map.of("email", email, "password", password));
	}

	public UserVo findById(Long userId) {
		return sqlSession.selectOne("user.findById", userId);
	}
	
	public UserVo findByEmail(String email) {
		return sqlSession.selectOne("user.findByEmail", email);
	}

	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}


}
