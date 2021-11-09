package si.fri.rsoteam.api.v1.filters;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import si.fri.rsoteam.services.config.RestConfig;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@WebFilter("*")
public class AuthFilter implements Filter {
    private final Logger LOG = Logger.getLogger(AuthFilter.class.getName());

    @Inject
    private RestConfig restConfig;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        String apiToken = restConfig.getApiToken();

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String userId = httpServletRequest.getHeader("userId");
        if (apiToken.equals(userId)) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
    }
}
