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
public class GuestbookVo {
	private Long id;
	private String name;
	private String password;
	private String regDate;
	private String contents;
}
