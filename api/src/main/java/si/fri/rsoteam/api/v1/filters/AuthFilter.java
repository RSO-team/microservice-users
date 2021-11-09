package si.fri.rsoteam.api.v1.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebFilter("*")
public class AuthFilter implements Filter {
    private final Logger LOG = Logger.getLogger(AuthFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Exception for enrolment and websocket endpoints
        String path = httpServletRequest.getRequestURI();
        if (path == null) {
            LOG.severe("PathInfo is null, url:" + httpServletRequest.getRequestURI());
        } else if (path.equals("/crossbow-am")
                || path.startsWith("/v1/market-participants/certificates")
                || path.startsWith("/market-participants/certificates")) {
            chain.doFilter(request, response);
            return;
        }

        String cid = httpServletRequest.getHeader("userId");
        if (cid == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
