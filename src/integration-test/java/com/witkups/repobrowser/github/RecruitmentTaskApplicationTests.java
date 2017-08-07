package com.witkups.repobrowser.github;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//to override default testProperties
@TestPropertySource(locations = {"classpath:application.properties", "classpath:test.properties"})
@Import(TestProperties.class)
public class RecruitmentTaskApplicationTests {

    private static final String REST_TEMPLATE_FIELD_NAME = "restTemplate";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GitHubRepoConnector gitHubRepoConnector;

    @Autowired
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    @Rule
    public TestName testName = new TestName();

    @Autowired
    private TestProperties testProperties;

    @Before
    public void setup() {
        final RestTemplate configuredRestTemplate = createProperRestTemplate();
        ReflectionTestUtils.setField(gitHubRepoConnector, REST_TEMPLATE_FIELD_NAME, configuredRestTemplate);
        mockMvc = prepareMockMvc();
    }

    private RestTemplate createProperRestTemplate() {
        return RestTemplateConfig.configure(restTemplate, testProperties.GIT_HUB_API_REPO_URL);
    }

    private MockMvc prepareMockMvc() {
        return MockMvcBuilders.webAppContextSetup(wac)
                .addDispatcherServletCustomizer(
                        ds -> ds.setThrowExceptionIfNoHandlerFound(testProperties.isThrowingNoHandlerFound)
                ).build();
    }

    @Test
    public void checkWrongRequestResponse() throws Exception {
        final MockHttpServletRequestBuilder jsonRequest = get("/repositories/Azbesciak/HitTheGround")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(jsonRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        final MockHttpServletRequestBuilder xmlRequest = get("/repositories/Azbesciak/HitTheGround")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final MvcResult xmlResult = mockMvc.perform(xmlRequest)
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
        final String content = xmlResult.getResponse().getContentAsString();
        assertTrue(content.matches(".*allowed: \\[application/json;charset=UTF-8].*"));
        assertTrue(content.matches(".*application/xml.*"));

        final MockHttpServletRequestBuilder notRecognizedRequest = get("/someNonExistentPath")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final ResultActions onNoHandlerFound = mockMvc.perform(notRecognizedRequest);

        if (testProperties.isThrowingNoHandlerFound) {
            onNoHandlerFound
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Not recognized request!")
                    );
        } else {
            onNoHandlerFound.andExpect(status().isNotFound());
        }
    }

    @Test
    public void requestRepo() throws Exception {
        final MockHttpServletRequestBuilder requestFirst = get("/repositories/Azbesciak/HitTheGround")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestFirst)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Azbesciak/HitTheGround"));


        final MockHttpServletRequestBuilder requestSecond = get("/repositories/Azbesciak/toDelete")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
        mockMvc.perform(requestSecond)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Azbesciak/toDelete"));

    }

    @Test
    public void checkAllData() throws Exception {
        final MockHttpServletRequestBuilder requestFirst = get("/repositories/Azbesciak/HitTheGround")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .locale(new Locale("pl"));

        mockMvc.perform(requestFirst)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Azbesciak/HitTheGround"))
                .andExpect(jsonPath("$.description").value("Try NOT to hit the ground piloting the plane"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/Azbesciak/HitTheGround.git"))
                .andExpect(jsonPath("$.stars").value(999))
                .andExpect(jsonPath("$.createdAt").value("czwartek, 1 czerwca 2017 16:26:23 CEST"));
    }

    @Test
    public void checkCreatedAtLocalizedDate() throws Exception {
        checkWithLocale("pl", "czwartek, 1 czerwca 2017 16:26:23 CEST");
        checkWithLocale("en", "Thursday, June 1, 2017 4:26:23 PM CEST");
        checkWithLocale("fr", "jeudi 1 juin 2017 16 h 26 CEST");
    }

    private void checkWithLocale(String language, String expected) throws Exception {
        final MockHttpServletRequestBuilder request = get("/repositories/Azbesciak/HitTheGround")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .locale(new Locale(language));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt").value(expected));
    }

    @Test
    public void repoExistenceTest() throws Exception {
        final MockHttpServletRequestBuilder requestOk = get("/repositories/Azbesciak/HitTheGround")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .locale(new Locale("pl"));
        mockMvc.perform(requestOk)
                .andExpect(status().isOk());

        final MockHttpServletRequestBuilder requestNotFound = get("/repositories/Azbesciak/qwasddasdasdas")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .locale(new Locale("pl"));
        mockMvc.perform(requestNotFound)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find requested repository"));
    }

}