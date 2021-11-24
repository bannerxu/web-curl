package top.banner.lib.curl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * curl过滤器：
 * xss
 *
 * @author lgh
 */
public class CurlFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CurlFilter.class);


    private final Set<String> urlPatterns = new LinkedHashSet<>();


    @Override
    public void init(FilterConfig filterConfig) {

    }


    public void addIgnoreUrlPatterns(String... urlPatterns) {
        if (null != urlPatterns) {
            Collections.addAll(this.urlPatterns, urlPatterns);
            for (String urlPattern : urlPatterns) {
                UrlPattern.addUrlPattern(urlPattern);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (ignore(req)) {
            chain.doFilter(req, resp);
        } else {
            String contentType = req.getContentType();
            if ((contentType != null) && (contentType.toLowerCase().startsWith("multipart/"))) {
                log.info("curl 暂不支持 表单中进行文件上传");
                chain.doFilter(req, resp);
            } else {
                CurlRequestWrapper wrapper = new CurlRequestWrapper(req);
                CurlBuilder curl = new CurlBuilder(wrapper, wrapper.getBodyStr(), Options.EMPTY);
                log.info(curl.toString());
                chain.doFilter(wrapper, resp);
            }
        }

    }


    /**
     * 判断是否需要忽略
     *
     * @return 如果要忽略返回 true
     */
    private Boolean ignore(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return UrlPattern.matches(requestURI);
    }

    @Override
    public void destroy() {

    }
}
