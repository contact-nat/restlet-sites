# Introduction

APISpark features a Wrapper Entity Store for Google Sheets spreadsheets. The Google Sheets Wrapper lets you expose data stored in a spreadsheet through a web API.

![GSheet to wrapper](images/gsheet-to-wrapper.jpg "GSheet to wrapper")

# Create a Google Sheets Wrapper

To know how to create a Google Sheets wrapper step by step, jump to our [Google Sheets tutorial](/technical-resources/apispark/tutorials/turn-spreadsheet-to-api "Google Sheets tutorial").

You can import one spreadsheet per wrapper. If you need data from several spreadsheets, create one wrapper per spreadsheet and import them into one single API.

# Configure your spreadsheet

Your spreadsheet must follow a few rules in order to be stored properly inside an Entity Store.

## Name your worksheets (Entities)

The name of your worksheets will be the name of your Entities.
An Entity name must contain at least one letter.  
It can contain any alphanumeric character and also underscores "_".  

## Name your columns (Properties)

The name of your columns will be the name of your properties.
A Property name must contain at least one letter.  
It can contain any alphanumeric character and also underscores "_" and hyphens "-".

> **Note:** we recommend using lowercase characters from the roman alphabet for column names.

# Connect to another Google account

You can connect to another Google account from the creation wizard by clicking the **Change Google account** button. After you have created your wrapper, you can still choose another Google account or another spreadsheet from your wrapper's **Settings** tab.

# Perform queries

You can apply simple [filters](/technical-resources/apispark/guide/publish/publish/invocation#filter "filters") on entity properties and [sort](/technical-resources/apispark/guide/publish/publish/invocation#sort "sort") queries on Google Sheets wrapper APIs.

>**Note:** Non alphanumeric characters must be in quotes in your queries.  
 If your spreadsheet cells are in plain text format, they must also be in quotes in your queries.
