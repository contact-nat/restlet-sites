# The data browser

The data browser is a useful tool that lets you preview and edit data stored in an Entity Store.

In order to use the data browser, the Entity Store must be deployed first (see [Entity Store deployment](/technical-resources/apispark/guide/store/entity-stores/deploy "Entity Store deployment")).

If updates are made to the Entity Store's data model, the Entity Store will need to be redeployed in order for the data browser to update itself.

## Edit data

The data browser lets you view existing data, add new entries, update existing entries, and delete entries.

To open the data browser, select an Entity from the list in the left panel and click the **Browser** tab.

![Add](images/edit-data.jpg "Add")

If your store contains more than 25 entries, they will be displayed in seperate pages. Each page contains 25 entries. You can navigate between the different pages via the button bar at the bottom of the table.

![Data browser pages](images/data-browser-pagination.jpg "Data browser pages")

This button bar allows you respectively to go to the **first page**, go to the **previous page**, enter a **page number**, go to the **next page**, go to the **last page** and **refresh the page**.

## Delete data

You may delete your Entity entries one by one but if you need to delete all entries at once, use the **Delete all** button.  
Click the action button on the right of the **Delete** button and select **Delete all**.

![Delete all entries](images/delete-all-entries.jpg "Delete all entries")

# Backup and restore data

APISpark lets you backup the data stored in an Entity Store for safekeeping and restoration.

To use the backup feature, navigate to the **Backup** tab from an Entity Store's **Overview**.

Backups can be created at any time, as long as the Entity Store is deployed.

## Create a backup

To create a new backup, click on the **Add** button in the backups section in the left panel. You will be prompted to enter a **Name** and a **Description** for the backup.

![Create a backup](images/create-backup.jpg "Create a backup")

You can consult your existing backups in the left panel.

## Restore a backup

To restore a backup, select a backup from the **Backups** list in the left panel, and click on the **Restore** button.

> **Note**: restoring a backup will overwrite any existing data stored in your Entity Store. We recommend creating a backup of your current Entity Store before restoring a previous backup.

![Restore a backup](images/restore-a-backup.jpg "Restore a backup")

## Download a backup

To download a backup, select a backup from the **Backups** list in the left panel, and click on the **Download** button. You will download your backup in csv format.

![Download a backup](images/download-a-backup.jpg "Download a backup")
