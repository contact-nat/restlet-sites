<h1 class="iconed" id="toc_0"><i class="fa fa-hand-o-right"></i>Introduction</h1>

GitHub is a social coding platform that hosts Git repositories.

The APISpark GitHub Wrapper is a type of File Store that wraps the content of a GitHub repository. This content can then be manipulated directly via the wrapper, or imported into a web API and exposed for consumption.

<h1 class="iconed" id="toc_0"><i class="fa fa-flag-checkered"></i>Requirements</h1>

To follow this tutorial, you will need:

*   a web browser,
*   15 minutes of your time,
*   your GitHub login details,
*   access to a GitHub repository.

<h1 class="numbered" id="toc_1"><i>1</i>Create a GitHub Wrapper</h1>

From the **Dashboard**, click on the **+ File Store** button, and select "GitHub wrapper" from the **Type** drop-down menu.

Give your wrapper a **Name** and a **Description** (optional).

![Create a GitHub Wrapper](images/create-github-wrapper.jpg "Create a GitHub Wrapper")

You will be taken to the new Wrapper's **Settings** tab.

<h1 class="numbered" id="toc_2"><i>2</i>Configure your GitHub account</h1>

From the wrapper's **Settings** tab, select **Github Account** from the **Security** section in the left panel.

Enter your GitHub account credentials. Press the **Test** button to check the connection.

![Setup GitHub account](images/github-account.jpg "Setup your GitHub account")

>**Note:** We advise you to [use personal access tokens](/documentation/cloud/guide/store/wrappers/github#personal-access-tokens "use personal access tokens") to import your GitHub repository.

<h1 class="numbered" id="toc_3"><i>3</i>Import a GitHub repository</h1>

To select a GitHub repository to import, click the **Add** button next to the **Imports** section in the left panel.

You will be prompted to enter the username of the **Owner** of the repository, as well as the **Repository** name.

![Add a GitHub repo](images/import-github-repo.jpg "Add a GitHub repository")

<h1 class="numbered" id="toc_4"><i>4</i>Generate the Wrapper's folders</h1>

Once the repository has been imported, select it from the **Imports** section in the left panel, and open the **Folders** tab in the central panel.

Click on the **Add folders** button to import the repository's folders. Imported folders are listed in the **Dependant folder** section in the central panel.

![Add GitHub folders](images/add-github-folders.jpg "Add GitHub folders")

Open the Wrapper's **Overview** tab to view the imported folders.

![GitHub folders overview](images/github-folders-overview.jpg "GitHub folders overview")

<h1 class="numbered" id="toc_5"><i>5</i>Deploy the Wrapper and browse the folders</h1>

In order to browser the contents of the folders, you need to start by deploying the Wrapper. Click on the **Deploy** button at the top-right of the screen.

Once the Wrapper is deployed, you can select a folder from the **Folders** section in the left panel, and then open the **Browser** tab in the central panel.

![GitHub folder browser](images/github-folder-browser.jpg "GitHub folder browser")

Congratulations on completing this tutorial! If you have questions or suggestions, feel free to contact the <a href="http://support.restlet.com/" target="_blank">Help Desk</a>.
