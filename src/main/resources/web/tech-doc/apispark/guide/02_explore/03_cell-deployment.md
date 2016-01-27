
Every APISpark cell is present in two different environments.

The *APISpark web console* is the development environment in which you design and maintain your cells.

When you deploy a cell, APISpark generates a program that will be deployed in the cloud and executed in the *APISpark runtime environment*.

![cell deployment](images/cell-deployment.jpg "cell deployment")


Thus, your Data Stores and web APIs are only available to store data or receive HTTP requests after having been deployed.

When you create a cell, a message reminds you that you need to deploy it before being able to call it. This message displays from the cell **Overview** page on top of the screen.

![cell deployment](images/overview-cell-needs-deployment.jpg "cell deployment")

It also displays from the Dashboard at the bottom of the cell card.

![cell deployment](images/dashboard-cell-needs-deployment.jpg "cell deployment")

Cells can be deployed from the draft, published and deprecated states, and can be re-deployed at any time.

The deployment process is composed of several steps, which are displayed in the deployment information window.

![cell deployment process](images/deploymentmessages.png "cell deployment process")

Each step of the deployment process is also documented by a message in the **Messages** tab. The events are listed in the **Traces** section.

![traces](images/traces.jpg "traces")
