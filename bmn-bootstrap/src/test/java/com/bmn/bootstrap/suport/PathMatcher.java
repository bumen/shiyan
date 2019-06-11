package com.bmn.bootstrap.suport;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/18
 */
public interface PathMatcher {

    /**
     * Does the given {@code path} represent a pattern that can be matched
     * by an implementation of this interface?
     * <p>If the return value is {@code false}, then the {@link #match}
     * method does not have to be used because direct equality comparisons
     * on the static path Strings will lead to the same result.
     * @param path the path String to check
     * @return {@code true} if the given {@code path} represents a pattern
     */
    boolean isPattern(String path);

    /**
     * Match the given {@code path} against the given {@code pattern},
     * according to this PathMatcher's matching strategy.
     * @param pattern the pattern to match against
     * @param path the path String to test
     * @return {@code true} if the supplied {@code path} matched,
     * {@code false} if it didn't
     */
    boolean match(String pattern, String path);

    /**
     * Match the given {@code path} against the corresponding part of the given
     * {@code pattern}, according to this PathMatcher's matching strategy.
     * <p>Determines whether the pattern at least matches as far as the given base
     * path goes, assuming that a full path may then match as well.
     * @param pattern the pattern to match against
     * @param path the path String to test
     * @return {@code true} if the supplied {@code path} matched,
     * {@code false} if it didn't
     */
    boolean matchStart(String pattern, String path);
}
