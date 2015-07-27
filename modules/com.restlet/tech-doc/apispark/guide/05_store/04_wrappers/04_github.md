# Introduction

APISpark features a Wrapper File Store for GitHub repositories. The GitHub Wrapper lets you expose files stored in a GitHub repository through a web API.

![GitHub to wrapper](images/github-to-wrapper.jpg "GitHub to wrapper")

# Create a GitHub Wrapper

To know how to create a GitHub wrapper step by step, jump to our [GitHub tutorial](/technical-resources/apispark/tutorials/github "GitHub tutorial").

# <a name="personal-access-tokens"></a>Use GitHub Personal Access Tokens

To avoid using your personal GitHub credentials, you can create <a href="http://restlet.com/technical-resources/apispark/tutorials/github" target="_blank">Personal access tokens</a> that will give access to a restricted part of your repositories and that can be revoked at any time.

You can create *Personal access tokens* from your GitHub *Personal settings*, in the <a href="https://github.com/settings/tokens" target="_blank">Personal access tokens section</a>.  
We advise you to select *repo* or *public_repo* from the scopes list suggested by GitHub which means this token will only grant access to the public or private repository but not to others giving access to i.e. account management, etc.

![Personal access tokens](images/github-perso-access-tokens.jpg "Personal access tokens")
