package com.witkups.repobrowser.github;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;

import static com.witkups.repobrowser.github.RepoConstants.*;

@Service
class GitHubRepoConnector {

    private final String GIT_HUB_API_REPO_URL;
    private final JsonToRepoMapper repoMapper;
    private final RestTemplate restTemplate;

    public GitHubRepoConnector(
            JsonToRepoMapper repoMapper,
            RestTemplate restTemplate,
            @Value(GIT_HUB_API_REPO_URL_PROPERTY) String git_hub_api_repo_url) {
        this.repoMapper = repoMapper;
        this.restTemplate = restTemplate;
        GIT_HUB_API_REPO_URL = git_hub_api_repo_url;
    }

    RepoModel getRepoData(final String owner, final String repoName, ZoneId zoneId, Locale locale) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put(OWNER_KEY, owner);
            put(REPO_KEY, repoName);
        }};
        ResponseEntity<JsonNode> exchange = restTemplate
                .getForEntity(GIT_HUB_API_REPO_URL + GET_REPO_PATH, JsonNode.class, params);
        return repoMapper.map(exchange.getBody(), zoneId, locale);
    }

}
