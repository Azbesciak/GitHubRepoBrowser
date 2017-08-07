package com.witkups.repobrowser.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;

import static com.witkups.repobrowser.github.RepoConstants.GIT_HUB_API_REPO_URL_PROPERTY;

@TestComponent
class TestProperties {

    @Value("${test.throw-exception-if-no-handler-found}")
    boolean isThrowingNoHandlerFound;

    @Value(GIT_HUB_API_REPO_URL_PROPERTY)
    String GIT_HUB_API_REPO_URL;

}