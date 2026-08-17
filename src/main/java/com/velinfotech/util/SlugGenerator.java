package com.velinfotech.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Turns a blog title into a URL segment for /blog/{slug}.
 *
 * The 60-character cap keeps the finished URL inside the 75-100 character budget
 * the content team asked for: "https://www.vellinfotech.com/blog/" is already 34
 * characters, so 34 + 60 = 94.
 */
public final class SlugGenerator {

    public static final int MAX_LENGTH = 60;

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");
    private static final Pattern EDGE_HYPHENS = Pattern.compile("(^-+)|(-+$)");
    private static final Pattern COMBINING_MARKS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private SlugGenerator() {
    }

    /**
     * "DevOps Course Explained: Tools, Use Cases & Career Opportunities"
     * -> "devops-course-explained-tools-use-cases-career-opportunities"
     *
     * Returns "post" for titles that contain nothing sluggable (e.g. only symbols),
     * so callers always get a usable segment.
     */
    public static String slugify(String input) {
        if (input == null || input.isBlank()) return "post";

        // Split accented characters so the marks can be stripped: "é" -> "e".
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String ascii = COMBINING_MARKS.matcher(normalized).replaceAll("");

        String slug = NON_ALPHANUMERIC
                .matcher(ascii.toLowerCase(Locale.ENGLISH))
                .replaceAll("-");

        slug = EDGE_HYPHENS.matcher(slug).replaceAll("");

        if (slug.isEmpty()) return "post";

        return truncateOnWordBoundary(slug);
    }

    /**
     * Trims to MAX_LENGTH without cutting a word in half — better to drop the last
     * word than to end a URL on "opportu".
     */
    private static String truncateOnWordBoundary(String slug) {
        if (slug.length() <= MAX_LENGTH) return slug;

        String clipped = slug.substring(0, MAX_LENGTH);

        // If the very next character is a hyphen, the cut already fell on a word
        // boundary and the clip is a whole run of words — keep every one of them.
        // Backing off here would needlessly drop a word that fits exactly.
        if (slug.charAt(MAX_LENGTH) != '-') {
            int lastHyphen = clipped.lastIndexOf('-');

            // Only keep the hard cut if the first word alone exceeds the limit.
            if (lastHyphen > 0) clipped = clipped.substring(0, lastHyphen);
        }

        return EDGE_HYPHENS.matcher(clipped).replaceAll("");
    }

    /**
     * Slugifies {@code title}, then appends -2, -3, ... until {@code isTaken} says
     * the result is free. The suffix is trimmed out of the base when needed so the
     * final string still respects MAX_LENGTH.
     */
    public static String uniqueSlug(String title, Predicate<String> isTaken) {
        String base = slugify(title);

        if (!isTaken.test(base)) return base;

        for (int suffix = 2; suffix < 1000; suffix++) {
            String tail = "-" + suffix;
            String trimmedBase = base;

            if (trimmedBase.length() + tail.length() > MAX_LENGTH) {
                trimmedBase = EDGE_HYPHENS
                        .matcher(base.substring(0, MAX_LENGTH - tail.length()))
                        .replaceAll("");
            }

            String candidate = trimmedBase + tail;
            if (!isTaken.test(candidate)) return candidate;
        }

        // Practically unreachable; keeps the method total rather than looping forever.
        return base + "-" + System.currentTimeMillis();
    }
}
