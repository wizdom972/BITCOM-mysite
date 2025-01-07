package mysite.repository;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

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
		StopWatch sw = new StopWatch();
		sw.start();
		
		UserVo userVo = sqlSession.selectOne("user.findByEmailAndPassword", Map.of("email", email, "password", password));
		
		sw.stop();
		long totalTime = sw.getTotalTimeMillis();
		System.out.println("[Execution Time][UserRepository.findByEmailAndPassword] " + totalTime + "millis");
		
		return userVo;
	}	

	public UserVo findById(Long userId) {
		return sqlSession.selectOne("user.findById", userId);
	}
	
	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}
}
