package com.witkups.repobrowser.github;

import org.springframework.stereotype.Component;

@Component
final class RepoConstants {

    static final String OWNER_KEY = "owner";
    static final String REPO_KEY = "repoName";

    static final String GIT_HUB_API_REPO_URL_PROPERTY = "${github-api-url}";
    static final String GET_REPO_PATH = "/{" + OWNER_KEY + "}/{" + REPO_KEY + "}";
    static final String FULL_NAME_JSON_KEY = "full_name";
    static final String CREATED_AT_JSON_KEY = "created_at";
    static final String CLONE_URL_JSON_KEY = "clone_url";
    static final String STARGAZERS_COUNT_JSON_KEY = "stargazers_count";
    static final String DESCRIPTION_JSON_KEY = "description";

}
