package mysite.exception;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mysite.dto.JsonResult;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public void handler(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Exception e) throws Exception {

		// 1. 로깅(logging)
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		log.error(errors.toString());

		// 2. 요청 구분
		// json 요청: request header의 accept: application/json
		// html 요청: request header의 accept: application/json이 없는지 확인
		// -> json 요청에도 text/html이 있을 수도 있기 때문
		String accept = request.getHeader("accept");

		if (accept.matches(".*application/json.*")) {
			// 3. json 응답
			JsonResult jsonResult = JsonResult.fail((e instanceof NoHandlerFoundException) ? "Unknown API URL" : errors.toString());
	         String jsonString = new ObjectMapper().writeValueAsString(jsonResult);
	         
	         response.setStatus(HttpServletResponse.SC_OK);
	         response.setContentType("application/json; charset=utf-8");
	         OutputStream os = response.getOutputStream();
	         os.write(jsonString.getBytes("utf-8"));
	         os.close();
	         
	         return;
		}
		
		 //4. HTML 응답: 사과 페이지(종료)
	      if(e instanceof NoHandlerFoundException || e instanceof NoResourceFoundException) {
	         request
	         .getRequestDispatcher("/error/404")
	         .forward(request, response);   
	      } else {
	         request.setAttribute("errors", errors.toString());
	         request
	            .getRequestDispatcher("/error/500")
	            .forward(request, response);   
	      }   

	}
}
