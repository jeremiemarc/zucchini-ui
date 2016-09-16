package io.zucchiniui.backend.reportconverter.converter;

import io.zucchiniui.backend.feature.domain.Feature;
import io.zucchiniui.backend.feature.domain.FeatureRepository;
import io.zucchiniui.backend.reportconverter.report.ReportFeature;
import io.zucchiniui.backend.reportconverter.report.Tag;
import io.zucchiniui.backend.shared.domain.BasicInfo;
import io.zucchiniui.backend.shared.domain.Location;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
class ReportFeatureConverter {

    private final FeatureRepository featureRepository;

    public ReportFeatureConverter(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    public Feature convert(final String testRunId, final ReportFeature reportFeature) {
        final String featureKey = ConversionUtils.stringToSha1Sum(reportFeature.getId());

        final BasicInfo info = new BasicInfo(
            ConversionUtils.trimString(reportFeature.getKeyword()),
            ConversionUtils.trimString(reportFeature.getName())
        );

        final Location location = new Location(
            ConversionUtils.trimString(reportFeature.getFilename()),
            reportFeature.getLine()
        );

        final Set<String> tags = reportFeature.getTags().stream()
            .map(Tag::getName)
            .map(ConversionUtils::stripAtSign)
            .collect(Collectors.toSet());

        final Feature feature = findOrCreateFeature(testRunId, featureKey);
        feature.setInfo(info);
        feature.setLocation(location);
        feature.setTags(tags);
        feature.setDescription(reportFeature.getDescription());

        return feature;
    }

    private Feature findOrCreateFeature(String testRunId, String featureKey) {
        return featureRepository.query(q -> q.withTestRunId(testRunId).withFeatureKey(featureKey))
            .tryToFindOne()
            .orElseGet(() -> new Feature(featureKey, testRunId));
    }

}
