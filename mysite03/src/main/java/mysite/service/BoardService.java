package mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysite.repository.BoardRepository;
import mysite.vo.BoardVo;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	/**
	 * 게시글 등록(원글 작성)
	 */
	public void addContents(BoardVo vo) {
		// 단순히 insert
		boardRepository.insert(vo);
	}
	
    // [답글 등록]
    public void addReply(BoardVo vo) {
        boardRepository.reply(vo);
    }

	/**
	 * 게시글 상세조회 (조회수 증가 포함)
	 * - userNo가 필요 없는 경우(단순 조회)
	 */
	public BoardVo getContents(Long no) {
		// 1) 글 가져오기
		BoardVo board = boardRepository.findByNo(no, null);

		// 2) 조회수 증가
		if (board != null) {
			boardRepository.incrementHit(no);
			// 조회수 최신화
			board.setHits(board.getHits() + 1);
		}

		return board;
	}

	/**
	 * 게시글 상세조회 (수정/삭제를 위해 userNo 확인이 필요한 경우)
	 * - 작성자 본인만 볼 수 있도록 하거나, 권한 체크 시 사용 가능
	 */
	public BoardVo getContents(Long no, Long userNo) {
		// userNo가 있으면 no+userNo 모두 매칭되는 글만 조회
		BoardVo board = boardRepository.findByNo(no, userNo);
		if (board != null) {
			// 조회수 증가
			boardRepository.incrementHit(no);
			board.setHits(board.getHits() + 1);
		}
		return board;
	}

	/**
	 * 게시글 수정
	 */
	public void updateContents(BoardVo vo) {
		// 제목, 내용 등을 DB에서 업데이트
		boardRepository.update(vo);
	}

	/**
	 * 게시글 삭제
	 * - userNo가 있으면 권한 체크( no + userNo )
	 * - userNo가 null이면 no만 매칭 (권한 체크 없이)
	 */
	public void deleteContents(Long no, Long userNo) {
		boardRepository.delete(no, userNo);
	}

	/**
	 * 게시글 목록 조회(페이징 + 검색)
	 * - 메서드 시그니처 고정: 리턴타입/이름 변경 불가
	 * - 파라미터는 자유롭게 조정 가능
	 * 
	 * 예시로 currentPage, keyword를 함께 받아서 처리
	 */
	public Map<String, Object> getContentsList(int currentPage, String keyword) {
		// 1) 전체 게시글 수 (검색어 반영)
		int totalCount = boardRepository.getTotalCount(keyword);

		// 2) 현재 페이지 목록 불러오기 (페이지당 5개) 
		List<BoardVo> list = boardRepository.findList(keyword, currentPage);

		// 3) 총 페이지 수 계산 (한 페이지당 5개)
		final int PAGE_SIZE = 5; // (Repo에서도 5개지만, Service 단에서도 명시)
		int totalPageCount = (int) Math.ceil((double) totalCount / PAGE_SIZE);

		// 4) Service → Controller → JSP 로 넘길 정보 구성
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("currentPage", currentPage);
		map.put("totalCount", totalCount);
		map.put("totalPageCount", totalPageCount);
		map.put("keyword", keyword);

		return map;
	}

	/**
	 * 메서드 시그니처와 리턴타입을 바꿀 수 없다고 했으므로
	 * 파라미터 없는 오버로드 버전도 예시로 만들어둘 수 있음
	 * (만약 꼭 필요 없다면 삭제하셔도 됩니다)
	 */
	public Map<String, Object> getContentsList() {
		// 디폴트로 1페이지, 검색어 없는 상태
		return getContentsList(1, "");
	}
	
	

}
