package top.banner.lib.curl;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class CurlBuilder {
    protected static final String FORMAT_HEADER = "-H '%1$s:%2$s'";
    protected static final String FORMAT_METHOD = "-X %1$s";
    protected static final String FORMAT_BODY = "-d '%1$s'";
    protected static final String FORMAT_URL = "'%1$s'";
    protected static final String CONTENT_TYPE = "Content-Type";
    protected final String method;
    protected final String contentType;
    protected final String body;
    protected final List<String> options;
    protected final List<Header> headers;
    protected final List<Query> queries;
    private final List<String> ignoreHeaders = Arrays.asList("content-length",
            "sec-ch-ua", "sec-ch-ua-mobile", "sec-fetch-site", "sec-fetch-mode", "sec-fetch-dest");
    protected StringBuffer url;

    public CurlBuilder(ServletRequest servletRequest, String body, Options options) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        this.url = new StringBuffer(request.getRequestURL().toString());
        this.method = request.getMethod();
        this.options = Collections.unmodifiableList(options.list());
        List<Header> headers = headers(request);
        this.headers = Collections.unmodifiableList(headers);
        this.contentType = request.getContentType();
        this.body = body;
        this.queries = queries(request);
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens an array objects to be joined. Strings will be formed from
     *               the objects by calling object.toString().
     */
    private static String join(CharSequence delimiter, Iterable<?> tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    private List<Header> headers(HttpServletRequest request) {
        List<Header> list = new ArrayList<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (!ignoreHeaders.contains(name.toLowerCase())) {
                String value = request.getHeader(name);//根据请求头的名字获取对应的请求头的值
//                log.info("【 {} 】: {}", name, value);
                list.add(new Header(name, value));
            }
        }
        return list;
    }

    private List<Query> queries(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<Query> list = new ArrayList<>();
        parameterMap.forEach((k, v) -> {
            String value = Arrays.stream(v).findFirst().orElse(null);
            list.add(new Query(k, value));
        });
        return list;
    }

    public String toString() {
        List<String> parts = new ArrayList<>();
        parts.add("curl");
        parts.addAll(options);
        parts.add(String.format(FORMAT_METHOD, method.toUpperCase()));

        for (Header header : headers) {
            final String headerPart = String.format(FORMAT_HEADER, header.name(), header.value());
            parts.add(headerPart);
        }

        if (contentType != null && !containsName(headers)) {
            parts.add(String.format(FORMAT_HEADER, CONTENT_TYPE, contentType));
        }

        if (body != null) {
            parts.add(String.format(FORMAT_BODY, body));
        }
        if (!queries.isEmpty()) {
            boolean firstTime = true;
            for (Query query : queries) {
                if (firstTime) {
                    firstTime = false;
                    url.append("?");
                } else {
                    url.append("&");
                }
                url.append(query.name()).append("=").append(query.value());
            }
        }
        parts.add(String.format(FORMAT_URL, url.toString()));

        return join(" ", parts);
    }

    protected boolean containsName(List<Header> headers) {
        for (Header header : headers) {
            if (Objects.equals(header.name().toLowerCase(), CurlBuilder.CONTENT_TYPE.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}