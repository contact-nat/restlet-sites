# Introduction

APISpark provides a highly flexible system for controlling runtime access to your APIs.

Runtime access means controlling which of your API's methods can be invoked by which of the API's consumers. You can decide to put in place very simple rules, for example by giving all your API's consumers the same access rights, or you can fine tune access to your APIs for different groups that you can define. You can also configure your API methods to be open to anyone without authentication.

Runtime access permissions to APISpark APIs are defined by assigning method access rights to member groups.

You have the choice to build the member groups that correspond to your needs. For example, you might create a group to which you will give read-only rights on your API, and another to which you will give read-write access.

Alternatively, you might create groups for specific categories of users, depending for example on the rate limitation you wish to apply (see the [Firewall settings](/technical-resources/apispark/guide/publish/secure/firewall-settings "Firewall settings") page on how to configure rate limitations).

When you create a new web API, two default groups will be created: *Testers* and *Consumers*. As the API creator, you will automatically be added to the *Testers* group so that you can invoke and test your API. The *Consumers* group is designed for the consumers of your API.

Of course, you can freely edit your API's member groups and delete the default ones if you wish.

# Managing web API groups

From your API **Overview** page, click on the **Members** tab.
In the **Groups** section, click on the **Add** button.

![Groups section](images/groups-section.jpg "Groups section")

In the **Add group** window, enter the name and description for your group. The new group displays in the **Groups** section.

![Add group](images/add-group.jpg "Add group")

# Managing group members

You can manage a group's members by selecting the group from the left panel of the **Members** tab.

![Groups section](images/add-members.jpg "Groups section")

Add members to a group by clicking on the **+ Add a member** button of the selected group.

> Note: you can only add a member to a group if s/he has already been added as a member of your API. The process of adding member to any cell is described in the [Team Work](/technical-resources/apispark/guide/explore/team-work "Team Work") page.


## Set your consumer members' role to User

Cell members destined to consume your API should be given role *User*. This means the member will be able to consult the API's documentation, but will not be able to modify the API in any way.

By default, new members are given role *User*.
