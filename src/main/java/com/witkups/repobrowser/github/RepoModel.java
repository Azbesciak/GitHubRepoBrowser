package com.witkups.repobrowser.github;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
class RepoModel {

    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars;
    private String createdAt;

}
