package com.witkups.repobrowser.github;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static com.witkups.repobrowser.github.RepoConstants.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

final class RestTemplateConfig {

    private static final String RESPONSE_BODY_1 = "{\n" +
            "\"" + FULL_NAME_JSON_KEY + "\": \"Azbesciak/HitTheGround\"," +
            "\"" + STARGAZERS_COUNT_JSON_KEY + "\": 999, " +
            "\"" + CLONE_URL_JSON_KEY + "\": \"https://github.com/Azbesciak/HitTheGround.git\"," +
            "\"" + DESCRIPTION_JSON_KEY + "\": \"Try NOT to hit the ground piloting the plane\"," +
            "\"" + CREATED_AT_JSON_KEY + "\": \"2017-06-01T16:26:23Z\"" +
            "}";
    private static final String RESPONSE_BODY_2 = "{\n" +
            "\"" + FULL_NAME_JSON_KEY + "\": \"Azbesciak/toDelete\"," +
            "\"" + STARGAZERS_COUNT_JSON_KEY + "\": 524, " +
            "\"" + CLONE_URL_JSON_KEY + "\": \"https://github.com/Azbesciak/toDelete.git\"," +
            "\"" + DESCRIPTION_JSON_KEY + "\": \"test\"," +
            "\"" + CREATED_AT_JSON_KEY + "\": \"2017-07-19T03:52:41Z\"" +
            "}";


    static RestTemplate configure(RestTemplate restTemplate, String gitHubApiRepoUrl) {

        MockRestServiceServer server = getMockServerWith(restTemplate);
        server.expect(manyTimes(), requestTo(gitHubApiRepoUrl + "/Azbesciak/HitTheGround"))
                .andExpect(method(GET))
                .andRespond(withSuccess(RESPONSE_BODY_1, MediaType.APPLICATION_JSON_UTF8));

        server.expect(manyTimes(), requestTo(gitHubApiRepoUrl + "/Azbesciak/toDelete"))
                .andExpect(method(GET))
                .andRespond(withSuccess(RESPONSE_BODY_2, MediaType.APPLICATION_JSON_UTF8));

        server.expect(manyTimes(), requestTo(gitHubApiRepoUrl + "/Azbesciak/qwasddasdasdas"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        return restTemplate;
    }


    private static MockRestServiceServer getMockServerWith(RestTemplate restTemplate) {
        return MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
    }

}