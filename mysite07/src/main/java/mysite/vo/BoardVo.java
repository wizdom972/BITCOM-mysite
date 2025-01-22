package mysite.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardVo {
	private Long no;
	private String title;
	private String content;
	private String regDate;
	private Integer hits;
	private Integer groupNo;
	private Integer orderNo;
	private Integer depth;
	private Long userId;
	private String userName;
}
