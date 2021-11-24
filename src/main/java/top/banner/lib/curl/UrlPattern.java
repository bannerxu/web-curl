package top.banner.lib.curl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * URL 匹配
 *
 * <p>
 * url-pattern: /status/*
 *
 * http://example.com/examples/status/synopsis  匹配
 *
 * http://example.com/examples/status/complete?date=today  匹配
 *
 * http://example.com/examples/status   匹配
 *
 * http://example.com/examples/server/status    未匹配
 *
 * <p>
 * url-pattern: *.map
 *
 * http://example.com/examples/US/Oregon/Portland.map   匹配
 *
 * http://example.com/examples/US/Washington/Seattle.map    匹配
 *
 * http://example.com/examples/Paris.France.map    匹配
 *
 * http://example.com/examples/US/Oregon/Portland.MAP   未匹配
 *
 * http://example.com/examples/interface/description/mail.mapi   未匹配
 *
 */
public class UrlPattern {
    private static final String EXTENSION_MAPPING_PATTERN = "*.";

    private static final String PATH_MAPPING_PATTERN = "/*";


    /**
     * 需要精确匹配的模式，例如“/test”
     */
    private static final Set<String> exactMatches = new HashSet<>();

    /**
     * 需要 URL 具有特定前缀的模式，例如“/test/*”
     */
    private static final Set<String> startsWithMatches = new HashSet<>();

    /**
     * 要求请求 URL 具有特定后缀的模式，例如“*.html”
     */
    private static final Set<String> endsWithMatches = new HashSet<>();


    public static void addUrlPattern(String urlPattern) {
        if (null == urlPattern) {
            return;
        }
        if (urlPattern.startsWith(EXTENSION_MAPPING_PATTERN)) {
            endsWithMatches.add(urlPattern.substring(1));
        } else if (urlPattern.equals(PATH_MAPPING_PATTERN)) {
            startsWithMatches.add("");
        } else if (urlPattern.endsWith(PATH_MAPPING_PATTERN)) {
            startsWithMatches.add(urlPattern.substring(0, urlPattern.length() - 1));
            exactMatches.add(urlPattern.substring(0, urlPattern.length() - 2));
        } else {
            if ("".equals(urlPattern)) {
                urlPattern = "/";
            }
            exactMatches.add(urlPattern);
        }
    }


    public static boolean matches(String requestPath) {
        for (String pattern : exactMatches) {
            if (pattern.equals(requestPath)) {
                return true;
            }
        }
        if (!requestPath.startsWith("/")) {
            return false;
        }
        for (String pattern : endsWithMatches) {
            if (requestPath.endsWith(pattern)) {
                return true;
            }
        }
        for (String pattern : startsWithMatches) {
            if (requestPath.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

}
