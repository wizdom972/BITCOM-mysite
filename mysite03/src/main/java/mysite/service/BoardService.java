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
	private static final int LIST_SIZE = 5;
	private static final int PAGE_SIZE = 5;

	@Autowired
	private BoardRepository boardRepository;

	public void addContents(BoardVo vo) {
		boardRepository.insert(vo);
	}

	public BoardVo getContents(Long no) {
		BoardVo vo = boardRepository.findByNo(no);

		if (vo != null) {
			return vo;
		}

		return null;
	}

	public BoardVo getContents(Long no, Long userNo) {
		return boardRepository.findByNoAndUserNo(no, userNo);
	}

	public void updateContents(BoardVo vo) {
		boardRepository.update(vo);
	}

	public void deleteContents(Long no, Long userNo) {
		boardRepository.delete(no, userNo);
	}

	public Map<String, Object> getContentsList(int currentPage, String keyword) {

		int totalCount = boardRepository.getTotalCount(keyword);
		int pageCount = (int) Math.ceil((double) totalCount / LIST_SIZE);
		int blockCount = (int) Math.ceil((double) pageCount / PAGE_SIZE);
		int currentBlock = (int) Math.ceil((double) currentPage / PAGE_SIZE);

		if (currentPage > pageCount) {
			currentPage = pageCount;
			currentBlock = (int) Math.ceil((double) currentPage / PAGE_SIZE);
		}

		if (currentPage < 1) {
			currentPage = 1;
			currentBlock = 1;
		}

		int beginPage = currentBlock == 0 ? 1 : (currentBlock - 1) * PAGE_SIZE + 1;
		int prevPage = (currentBlock > 1) ? (currentBlock - 1) * PAGE_SIZE : 0;
		int nextPage = (currentBlock < blockCount) ? currentBlock * PAGE_SIZE + 1 : 0;
		int endPage = (nextPage > 0) ? (beginPage - 1) + LIST_SIZE : pageCount;

		List<BoardVo> list = boardRepository.findAllByPageAndKeword(keyword, currentPage, LIST_SIZE);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("list", list);
		map.put("totalCount", totalCount);
		map.put("listSize", LIST_SIZE);
		map.put("currentPage", currentPage);
		map.put("beginPage", beginPage);
		map.put("endPage", endPage);
		map.put("prevPage", prevPage);
		map.put("nextPage", nextPage);
		map.put("keyword", keyword);

		return map;
	}
}
