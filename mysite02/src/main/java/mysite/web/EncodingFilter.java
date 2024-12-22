package mysite.web;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;

@WebFilter("/**")
public class EncodingFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = 1L;
	private String encoding;

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// request
		System.out.println("EncodingFilter.doFilter() called: request processing");
		request.setCharacterEncoding(encoding);

		// pass the request along the filter chain
		chain.doFilter(request, response);

		// response
		System.out.println("EncodingFilter.doFilter() called: response processing");
	}

	public void init(FilterConfig fConfig) throws ServletException {
		encoding = fConfig.getInitParameter("encoding");

		if (encoding == null) {
			encoding = "utf-8";
		}

	}

}
