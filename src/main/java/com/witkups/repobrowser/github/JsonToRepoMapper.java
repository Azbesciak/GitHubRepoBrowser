package com.witkups.repobrowser.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.witkups.repobrowser.utils.mapper.Mapper;
import com.witkups.repobrowser.utils.parser.DateParser;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Locale;

import static com.witkups.repobrowser.github.RepoConstants.*;

@Service
final class JsonToRepoMapper implements Mapper<RepoModel, JsonNode> {

    private final DateParser dateParser;

    JsonToRepoMapper(DateParser dateParser) {
        this.dateParser = dateParser;
    }

    @Override
    public RepoModel map(JsonNode json, ZoneId zoneId, Locale locale) {
        return RepoModel.builder()
                .fullName(json.get(FULL_NAME_JSON_KEY).asText())
                .description(json.get(DESCRIPTION_JSON_KEY).asText(null))
                .cloneUrl(json.get(CLONE_URL_JSON_KEY).asText())
                .stars(json.get(STARGAZERS_COUNT_JSON_KEY).asInt())
                .createdAt(getLocalizedCreationDate(json, zoneId, locale))
                .build();
    }

    private String getLocalizedCreationDate(JsonNode json, ZoneId timeZoneId, Locale locale) {
        String createdAt =  json.get(CREATED_AT_JSON_KEY).asText();
        return dateParser.parse(createdAt, timeZoneId, locale);
    }

}
