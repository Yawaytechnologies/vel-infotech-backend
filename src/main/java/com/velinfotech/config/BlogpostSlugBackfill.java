package com.velinfotech.config;

import com.velinfotech.model.Blogpost;
import com.velinfotech.repository.BlogpostRepository;
import com.velinfotech.service.BlogImageService;
import com.velinfotech.service.BlogpostService;
import com.velinfotech.util.SlugGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Gives every pre-existing blog post a slug and a category.
 *
 * Posts created before /blog/{slug} shipped have neither, and both columns had to
 * be added as nullable so ddl-auto=update could apply them to a populated table.
 * This runs once on boot and fills the gaps; afterwards it finds nothing and costs
 * a single query.
 */
@Component
public class BlogpostSlugBackfill implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(BlogpostSlugBackfill.class);

    /** The newest backfilled post is dated this many days ago. */
    private static final int BACKDATE_START_DAYS = 60;

    /** Gap between consecutive backfilled posts. */
    private static final int BACKDATE_INTERVAL_DAYS = 45;

    private final BlogpostRepository blogpostRepository;
    private final BlogImageService blogImageService;

    public BlogpostSlugBackfill(BlogpostRepository blogpostRepository,
                                BlogImageService blogImageService) {
        this.blogpostRepository = blogpostRepository;
        this.blogImageService = blogImageService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        backfillSlugs();
        backfillPublishDates();
        migrateInlineImages();
    }

    /**
     * Moves featured images out of the post row and behind /api/images/{id}.
     *
     * They were stored as base64 data URIs, so every byte was inlined into the HTML
     * on every request. Runs once; afterwards no post holds a data URI and this is a
     * single query.
     */
    private void migrateInlineImages() {
        List<Blogpost> inlined = blogpostRepository.findAll().stream()
                .filter(post -> post.getFeaturedImageUrl() != null
                        && post.getFeaturedImageUrl().startsWith("data:"))
                .toList();

        if (inlined.isEmpty()) return;

        long freed = 0;

        for (Blogpost post : inlined) {
            String original = post.getFeaturedImageUrl();
            String url = blogImageService.toServableUrl(original);

            if (url.startsWith("data:")) {
                log.warn("Could not migrate the image on blog post {}; leaving it inline", post.getId());
                continue;
            }

            freed += original.length();
            post.setFeaturedImageUrl(url);

            log.info("Migrated image on blog post {} -> {} ({} chars inline)",
                    post.getId(), url, original.length());
        }

        blogpostRepository.saveAll(inlined);
        log.info("Migrated {} inline image(s), removing {} characters of base64 from page HTML",
                inlined.size(), freed);
    }

    /**
     * Gives posts written before the createdAt column a publish date, so structured
     * data can carry datePublished instead of omitting it.
     *
     * The real publication dates were never recorded and cannot be recovered. These
     * are assigned oldest-id-first at a fixed interval, so the ordering between posts
     * is correct even though the individual dates are approximate. Anything already
     * carrying a date is left alone, and posts created from now on get a real one.
     */
    private void backfillPublishDates() {
        List<Blogpost> undated = blogpostRepository.findAll().stream()
                .filter(post -> post.getCreatedAt() == null)
                .sorted(Comparator.comparing(Blogpost::getId))
                .toList();

        if (undated.isEmpty()) return;

        // Walk backwards from the most recent slot so the newest post is the newest date.
        LocalDateTime slot = LocalDateTime.now()
                .minusDays(BACKDATE_START_DAYS)
                .withHour(10).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime[] dates = new LocalDateTime[undated.size()];
        for (int i = undated.size() - 1; i >= 0; i--) {
            dates[i] = slot;
            slot = slot.minusDays(BACKDATE_INTERVAL_DAYS);
        }

        for (int i = 0; i < undated.size(); i++) {
            Blogpost post = undated.get(i);
            post.setCreatedAt(dates[i]);

            log.info("Backfilled publish date for blog post {} -> {}", post.getId(), dates[i]);
        }

        blogpostRepository.saveAll(undated);
        log.info("Backfilled publish dates for {} blog post(s)", undated.size());
    }

    private void backfillSlugs() {
        List<Blogpost> pending = blogpostRepository.findBySlugIsNullOrSlugEquals("");

        if (pending.isEmpty()) return;

        // Collect slugs as we go: the rows in this batch have no slug in the database
        // yet, so existsBySlug alone cannot stop two same-titled posts colliding.
        Set<String> claimed = new HashSet<>();

        for (Blogpost post : pending) {
            String slug = SlugGenerator.uniqueSlug(
                    post.getTitle(),
                    candidate -> claimed.contains(candidate) || blogpostRepository.existsBySlug(candidate)
            );

            claimed.add(slug);
            post.setSlug(slug);

            if (post.getCategory() == null || post.getCategory().isBlank()) {
                post.setCategory(BlogpostService.DEFAULT_CATEGORY);
            }

            log.info("Backfilled blog post {} -> /blog/{}", post.getId(), slug);
        }

        blogpostRepository.saveAll(pending);
        log.info("Backfilled slugs for {} blog post(s)", pending.size());
    }
}
