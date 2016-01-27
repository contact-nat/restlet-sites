/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.filter;

import org.restlet.engine.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DirectoryInstruction {

    private String index = "index";
    private boolean listingAllowed = false;
    private boolean negotiatingContent = true;
    private List<String> tryFiles;

    public boolean isListingAllowed() {
        return listingAllowed;
    }

    public DirectoryInstruction withListingAllowed(boolean listingAllowed) {
        DirectoryInstruction directoryInstruction = new DirectoryInstruction();
        directoryInstruction.index = this.index;
        directoryInstruction.listingAllowed = listingAllowed;
        directoryInstruction.negotiatingContent = this.negotiatingContent;
        directoryInstruction.tryFiles = this.tryFiles;

        return directoryInstruction;
    }

    public boolean isNegotiatingContent() {
        return negotiatingContent;
    }

    public DirectoryInstruction withNegotiatingContent(boolean negotiatingContent) {
        DirectoryInstruction directoryInstruction = new DirectoryInstruction();
        directoryInstruction.index = this.index;
        directoryInstruction.listingAllowed = this.listingAllowed;
        directoryInstruction.negotiatingContent = negotiatingContent;
        directoryInstruction.tryFiles = this.tryFiles;

        return directoryInstruction;
    }

    public String getIndex() {
        return index;
    }

    public DirectoryInstruction withIndex(String index) {
        DirectoryInstruction directoryInstruction = new DirectoryInstruction();
        directoryInstruction.index = index;
        directoryInstruction.listingAllowed = this.listingAllowed;
        directoryInstruction.negotiatingContent = this.negotiatingContent;
        directoryInstruction.tryFiles = this.tryFiles;

        return directoryInstruction;
    }

    public List<String> getTryFiles() {
        return tryFiles;
    }

    public DirectoryInstruction withTryFiles(String tryFilesAsString) {
        DirectoryInstruction directoryInstruction = new DirectoryInstruction();
        directoryInstruction.index = this.index;
        directoryInstruction.listingAllowed = this.listingAllowed;
        directoryInstruction.negotiatingContent = this.negotiatingContent;
        if (!StringUtils.isNullOrEmpty(tryFilesAsString)) {
            directoryInstruction.tryFiles = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(tryFilesAsString);
            while (tokenizer.hasMoreTokens()) {
                directoryInstruction.tryFiles.add(tokenizer.nextToken());
            }
        }

        return directoryInstruction;
    }
}
