package com.witkups.repobrowser.github;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.util.Locale;

import static com.witkups.repobrowser.github.RepoConstants.*;
import static com.witkups.repobrowser.github.TestUtils.parseDateWithLocale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonToRepoModelMapperTest {

    private static final String HTTPS_GIT_HUB_COM = "https://github.com/";
    private static final Locale LOCALE_PL = new Locale("pl");
    private static final ZoneId defaultZoneId = ZoneId.systemDefault();
    private final static String REPO_NAME_VALUE_1 = "XYZ";
    private final static String USER_VALUE_1 = "AlaMaKota";
    private static final String FULL_REPO_NAME_1 = USER_VALUE_1 + "/" + REPO_NAME_VALUE_1;
    private static final String CLONE_URL_VALUE_1 = HTTPS_GIT_HUB_COM + FULL_REPO_NAME_1 + ".git";
    private static final Integer STARS_1 = 785;
    private static final String DESCRIPTION_VALUE_1 = "some descripTIon";
    private static final String DATE_STRING_1 = "2017-07-19T03:52:41Z";

    private final static String REPO_NAME_VALUE_2 = "QWE";
    private final static String USER_VALUE_2 = "LOSADWE";
    private static final String FULL_REPO_NAME_2 = USER_VALUE_2 + "/" + REPO_NAME_VALUE_2;
    private static final String CLONE_URL_VALUE_2 = HTTPS_GIT_HUB_COM + FULL_REPO_NAME_2 + ".git";
    private static final Integer STARS_2 = 666;
    private static final String DESCRIPTION_VALUE_2 = "YYyyy";
    private static final String DATE_STRING_2 = "2012-09-13T01:12:24Z";

    @Autowired
    private JsonToRepoMapper jsonToRepoMapper;

    @Test
    public void mapGeneralTest()  {
        final ObjectNode input1 = JsonNodeFactory.instance.objectNode();
        input1.put("name", REPO_NAME_VALUE_1);
        input1.put(FULL_NAME_JSON_KEY, FULL_REPO_NAME_1);
        input1.put("private", false);
        input1.put(CREATED_AT_JSON_KEY, DATE_STRING_1);
        input1.put(CLONE_URL_JSON_KEY, CLONE_URL_VALUE_1);
        input1.put(STARGAZERS_COUNT_JSON_KEY, STARS_1);
        input1.put(DESCRIPTION_JSON_KEY, DESCRIPTION_VALUE_1);


        final RepoModel repoModel1 = jsonToRepoMapper.map(input1, defaultZoneId, LOCALE_PL);
        final String createdAt1 = parseDateWithLocale(DATE_STRING_1, LOCALE_PL);

        final ObjectNode input2 = JsonNodeFactory.instance.objectNode();
        input2.put("name", REPO_NAME_VALUE_2);
        input2.put(FULL_NAME_JSON_KEY, FULL_REPO_NAME_2);
        input2.put("private", false);
        input2.put(CREATED_AT_JSON_KEY, DATE_STRING_2);
        input2.put(CLONE_URL_JSON_KEY, CLONE_URL_VALUE_2);
        input2.put(STARGAZERS_COUNT_JSON_KEY, STARS_2);
        input2.put(DESCRIPTION_JSON_KEY, DESCRIPTION_VALUE_2);

        final RepoModel repoModel2 = jsonToRepoMapper.map(input2, defaultZoneId, Locale.US);
        final String createdAt2 = parseDateWithLocale(DATE_STRING_2, Locale.US);

        assertEquals(CLONE_URL_VALUE_1, repoModel1.getCloneUrl());
        assertEquals(DESCRIPTION_VALUE_1, repoModel1.getDescription());
        assertEquals(STARS_1, repoModel1.getStars());
        assertEquals(FULL_REPO_NAME_1, repoModel1.getFullName());
        assertEquals(createdAt1, repoModel1.getCreatedAt());

        assertEquals(CLONE_URL_VALUE_2, repoModel2.getCloneUrl());
        assertEquals(DESCRIPTION_VALUE_2, repoModel2.getDescription());
        assertEquals(STARS_2, repoModel2.getStars());
        assertEquals(FULL_REPO_NAME_2, repoModel2.getFullName());
        assertEquals(createdAt2, repoModel2.getCreatedAt());
    }



    @Test(expected = NullPointerException.class)
    public void filterFieldsTest() {
        ObjectNode input = JsonNodeFactory.instance.objectNode();
        input.put("qweqweax", "wqxczdas");
        input.put("qwczx", "wqxczffsddas");
        input.put("qwezczx", "qwevxvx");
        input.put("adsasd", "qweqez");
        final RepoModel repoModel = jsonToRepoMapper.map(input);
        fail();
    }

}