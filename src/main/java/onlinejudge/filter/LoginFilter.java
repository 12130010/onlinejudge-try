package onlinejudge.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter{
	static final String ORIGIN = "Origin";
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
//		if (request.getHeader(ORIGIN) == null || request.getHeader(ORIGIN).equals("null")) {
	           response.setHeader("Access-Control-Allow-Origin", " http://localhost:4000");//* or origin as u prefer
	           response.setHeader("Access-Control-Allow-Credentials", "true");
	           response.setHeader("Access-Control-Allow-Headers",
	           request.getHeader("Access-Control-Request-Headers"));
//        }
        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
       }
		
		String path = ((HttpServletRequest) req).getRequestURI();
		if(path.startsWith("/login")
		|| path.startsWith("/arduino")
		|| path.contains("/chat")){
			chain.doFilter(req, res);
			return;
		}
		
		HttpSession session = ((HttpServletRequest) req).getSession();
		if(session.getAttribute("userName") == null || session.getAttribute("userId") == null){
			((HttpServletResponse) res).sendRedirect("/login");
		}else{
			chain.doFilter(req, res);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
