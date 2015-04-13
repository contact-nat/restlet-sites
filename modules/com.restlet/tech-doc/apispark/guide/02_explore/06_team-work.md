
# Introduction

APISpark is built to support team-based collaboration on API projects.

Collaboration in APISpark is based on the capacity to share a cell with other APISpark users, and to attribute specific roles to collaborators based on the functions they carry out.

# Cell Roles

A member can have one of five roles on a cell.

For team-based collaboration, members should be given either of the roles *Owner*, *Manager*, *Operator*, or *Developer*.

*User* is a special role designed for web API members who are consumers of your API. For more information about managing your API consumption, please visit the [User Groups](/technical-resources/apispark/guide/publish/secure/user-groups "User Groups") page.

Depending on the role given to a member, s/he will have different permissions in the APISpark console.

![team work](images/provider-team.jpg "team work")


## Team roles

 * **Owner:** a cell's Owner has full rights on it. Each cell has a unique Owner, who is also the Owner of every version of the cell. The Owner's role cannot be modified.

 * **Manager:** a Manager has all but a few rights on a cell. For instance, a manager cannot delete a cell, as this operation is reserved to the Owner and to manage the community of cell members.

 * **Operator:** an Operator has the rights to manage runtime aspects of a cell and to manage a cell's lifecycle.

 * **Developer:** a Developer has rights to the design-time and development aspects of a cell.


## Consumer roles

 * **User:** a User can consult basic information about a web API and be given runtime access rights by being added to a member group.


# Managing members

## Members with standard accounts

To manage the members of a cell, navigate to the cell's **Members** tab.

From here, you can add, delete members, and attribute roles to members.

>**Note:** When you create a new cell [revision](/technical-resources/apispark/guide/explore/revisions "revision"), it is created with the same members as the former revision. But if you restore an old revision, then you restore it with the members that were associated to this old revision (you loose the members changes you made).

To add a member to a cell:

From the web API **Overview** page, click on the **Members** tab.  
In the **Members** section, click on the **Add** button.  
In the window that displays, enter the name or email address of the member you want to add in the **Search** field. From the information entered, APISpark will suggest the different accounts created by the member (GitHub, Google Mail, etc.).

![Add member](images/add-member-to-cell.jpg "Add member")

Select the appropriate account and click on the **Add** button.  
Select a role for your member in the drop-down menu (Manager, Operator, Developer or User).

>**Note:** To add a member, s/he must have signed in to APISpark before.


## Members with App accounts

App accounts make it easy for end-users without an APISpark account to connect to protected APISpark APIs.

As the manager of an APISpark Full Stack API (or Connector), you can manually create App accounts and share their credentials directly with your end-users who can then authenticate themselves.


To create a new App member on a Full Stack API (or Connector):

From the web API **Overview** page, click on the **Members** tab.  
In the **Members** section, click on the **Add** button.  
In the window that displays, select **New local member** from the **Member** drop-down menu.

![Add a new local member](images/add-new-local-member.jpg "Add a new local member")

Enter the App account details: **First name**, **Last name**, **Email**, **Username** and **Secret key** (all fields are required except **Email**).

![App account details](images/app-account-details.jpg "App account details")

The new account will automatically be added as a member of the API. You can now also add this account as a member of other APIs by searching for an existing member.

>**Note:** The **Username** must be a unique identifier across the APISpark platform (a message will inform you in case the **Username** you chose is already taken). This **Username** may only contain alphanumeric characters, hyphens and underscores, and cannot begin with an hyphen or underscore.  
The **Secret key** entered must contain at least 8 characters.
App members are automatically assigned the *User* role.
