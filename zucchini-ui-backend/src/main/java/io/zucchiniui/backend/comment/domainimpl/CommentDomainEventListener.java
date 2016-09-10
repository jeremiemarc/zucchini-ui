package io.zucchiniui.backend.comment.domainimpl;

import com.google.common.eventbus.Subscribe;
import io.zucchiniui.backend.comment.domain.CommentRepository;
import io.zucchiniui.backend.shared.domain.ItemReference;
import io.zucchiniui.backend.shared.domain.ItemReferenceType;
import io.zucchiniui.backend.testrun.domain.TestRunDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommentDomainEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentDomainEventListener.class);

    private final CommentRepository commentRepository;

    public CommentDomainEventListener(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Subscribe
    public void onTestRunDeleted(TestRunDeletedEvent event) {
        final ItemReference testRunReference = ItemReferenceType.TEST_RUN_ID.createReference(event.getEntityId());
        LOGGER.info("Deleting reference for comments linked with reference {}", testRunReference);
        commentRepository.query(q -> q.withReference(testRunReference)).forEach(comment -> {
            comment.removeReference(testRunReference);
            commentRepository.save(comment);
        });
    }

}
