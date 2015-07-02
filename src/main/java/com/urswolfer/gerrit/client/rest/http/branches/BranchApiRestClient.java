package com.urswolfer.gerrit.client.rest.http.branches;

import com.google.gerrit.extensions.api.changes.FileApi;
import com.google.gerrit.extensions.api.projects.BranchApi;
import com.google.gerrit.extensions.common.FileInfo;
import com.google.gerrit.extensions.restapi.RestApiException;
import com.google.gson.JsonElement;
import com.urswolfer.gerrit.client.rest.http.GerritRestClient;
import com.urswolfer.gerrit.client.rest.http.changes.FileInfoParser;
import com.urswolfer.gerrit.client.rest.http.projects.BranchInfoParser;
import com.urswolfer.gerrit.client.rest.http.projects.ProjectApiRestClient;

import java.util.Map;

public class BranchApiRestClient extends BranchApi.NotImplemented implements BranchApi {
    private final GerritRestClient gerritRestClient;
    private final BranchInfoParser branchInfoParser;
    private final ProjectApiRestClient projectApiRestClient;
    private final FileInfoParser fileInfoParser;
    private final String name;

    public BranchApiRestClient(GerritRestClient gerritRestClient,
                                BranchInfoParser branchInfoParser,
                                ProjectApiRestClient projectApiRestClient,
                                FileInfoParser fileInfoParser,
                                String name) {
        this.gerritRestClient = gerritRestClient;
        this.branchInfoParser = branchInfoParser;
        this.projectApiRestClient = projectApiRestClient;
        this.fileInfoParser = fileInfoParser;
        this.name = name;
    }

    public String getRequestPath() {
        return projectApiRestClient.projectsUrl() + "/branches/" + name;
    }

    @Override
    public Map<String, FileInfo> files() throws RestApiException {
        String request = getRequestPath() + "/files";
        JsonElement jsonElement = gerritRestClient.getRequest(request);
        return fileInfoParser.parseFileInfos(jsonElement);
    }

    @Override
    public FileApi file(String path) {
        return new FileApiRestClient(gerritRestClient, this, path);
    }
}
