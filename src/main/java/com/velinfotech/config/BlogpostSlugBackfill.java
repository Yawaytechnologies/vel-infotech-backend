package com.velinfotech.config;

import com.velinfotech.model.Blogpost;
import com.velinfotech.repository.BlogpostRepository;
import com.velinfotech.service.BlogpostService;
import com.velinfotech.util.SlugGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    private final BlogpostRepository blogpostRepository;

    public BlogpostSlugBackfill(BlogpostRepository blogpostRepository) {
        this.blogpostRepository = blogpostRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
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
