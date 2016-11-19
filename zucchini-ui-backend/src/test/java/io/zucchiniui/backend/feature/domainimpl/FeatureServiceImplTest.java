package io.zucchiniui.backend.feature.domainimpl;

import io.zucchiniui.backend.feature.dao.FeatureDAO;
import io.zucchiniui.backend.feature.domain.Feature;
import io.zucchiniui.backend.feature.domain.FeatureRepository;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mongodb.morphia.query.Query;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class FeatureServiceImplTest {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FeatureRepository featureRepository;

    @Mock
    private FeatureDAO featureDAO;

    @InjectMocks
    private FeatureServiceImpl featureService;

    @Test
    public void should_delete_feature_by_test_run_id() throws Exception {
        // given
        final String testRunId = "testRunId";

        final Query<Feature> featureQuery = mock(Query.class, "featureQuery");
        given(featureDAO.prepareTypedQuery(any())).willReturn(featureQuery);

        // when
        featureService.deleteByTestRunId(testRunId);

        // then
        then(featureDAO).should().prepareTypedQuery(any());
        then(featureDAO).should().deleteByQuery(featureQuery);
    }

    @Test
    public void should_delete_feature_by_id() throws Exception {
        // given
        final String featureId = "featureId";

        final Feature feature = mock(Feature.class);
        given(featureRepository.getById(featureId)).willReturn(feature);

        // when
        featureService.deleteById(featureId);

        // then
        then(featureRepository).should().getById(featureId);
        then(featureRepository).should().delete(feature);
    }

}
