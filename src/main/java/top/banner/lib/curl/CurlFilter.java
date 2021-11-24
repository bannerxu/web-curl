package top.banner.lib.curl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * curl过滤器：
 * xss
 *
 * @author lgh
 */
public class CurlFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CurlFilter.class);

    public CurlFilter() {
        this.ignoreList = Collections.singletonList("/actuator");
    }

    public CurlFilter(List<String> ignoreList) {
        this.ignoreList = ignoreList;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * 要忽略的URI
     */
    private final List<String> ignoreList;

    /**
     * 判断是否需要忽略
     *
     * @return 如果要忽略返回 true
     */
    public Boolean ignore(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return ignoreList.stream().anyMatch(requestURI::contains);
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

    @Override
    public void destroy() {

    }
}
