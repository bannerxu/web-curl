package top.banner.lib.curl;


import org.junit.Assert;
import org.junit.Test;

public class UrlPatternTest {

    /**
     * 精准配备
     */
    @Test
    public void exactMatches() {

        UrlPattern.addUrlPattern("/test");
        Assert.assertTrue("异常", UrlPattern.matches("/test"));
        Assert.assertFalse("异常", UrlPattern.matches("/test1"));
    }

    /**
     * 特定前缀
     */
    @Test
    public void startsWithMatches() {
        //特定前缀
        UrlPattern.addUrlPattern("/test/*");
        Assert.assertTrue("异常", UrlPattern.matches("/test"));
        Assert.assertTrue("异常", UrlPattern.matches("/test/"));
        Assert.assertFalse("异常", UrlPattern.matches("/test1"));
        Assert.assertTrue("异常", UrlPattern.matches("/test/1"));
        Assert.assertTrue("异常", UrlPattern.matches("/test/1/1"));
    }

    /**
     * 特定后缀
     */
    @Test
    public void endsWithMatches() {
        UrlPattern.addUrlPattern("*.html");
        Assert.assertTrue("异常", UrlPattern.matches("/test.html"));
        Assert.assertTrue("异常", UrlPattern.matches("/ss/s/test.html"));
        Assert.assertFalse("异常", UrlPattern.matches("/test.html1"));
    }
}