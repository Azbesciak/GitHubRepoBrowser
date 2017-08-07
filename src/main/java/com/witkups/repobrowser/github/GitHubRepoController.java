package com.witkups.repobrowser.github;

import com.witkups.repobrowser.utils.TimeZoneUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static com.witkups.repobrowser.github.RepoConstants.GET_REPO_PATH;

@RestController
@RequestMapping("/repositories")
@Slf4j
final class GitHubRepoController {

    private GitHubRepoConnector gitHubRepoConnector;


    public GitHubRepoController(GitHubRepoConnector gitHubRepoConnector) {
        this.gitHubRepoConnector = gitHubRepoConnector;
    }

    @GetMapping(
            value = GET_REPO_PATH,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public RepoModel getRepositoryData(
            HttpServletRequest request,
            @PathVariable final String owner,
            @PathVariable final String repoName
    ) {
        final TimeZone timeZone = RequestContextUtils.getTimeZone(request);
        final ZoneId zoneId = TimeZoneUtils.getTimeZoneIdOrDefault(timeZone);
        final Locale locale = RequestContextUtils.getLocale(request);
        return gitHubRepoConnector.getRepoData(owner, repoName, zoneId, locale);
    }
}
