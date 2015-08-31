# Introduction

APISpark features a Wrapper Entity Store for Google Sheets spreadsheets. The Google Sheets Wrapper lets you expose data stored in a spreadsheet through a web API. The Google Sheets wrapper loads the data from your spreadsheet and changes made are replicated through automatically.

![GSheet to wrapper](images/gsheet-to-wrapper.jpg "GSheet to wrapper")

# Create a Google Sheets Wrapper

To know how to create a Google Sheets wrapper step by step, jump to our [Google Sheets tutorial](/technical-resources/apispark/tutorials/turn-spreadsheet-to-api "Google Sheets tutorial").

You can import one spreadsheet per wrapper. If you need data from several spreadsheets, create one wrapper per spreadsheet and import them into one single API.

# Update your spreadsheet structure

If you modify your spreadsheet structure (e.g. add/remove columns), you will need to synchronize your spreadsheet with your Google Sheets Wrapper.

Navigate to the **Settings** tab of your Google Sheets Wrapper.  
Click the **Synchronize** button and **Deploy** your wrapper.

![Synchronize](images/synchro-button.jpg "Synchronize")

The changes made have been propagated to the associated API(s) as you you can see in your API **Data sources** section.

![Data sources](images/gsheet-data-sources-section.jpg "Data sources")

# Perform queries

You can apply simple [filters](/technical-resources/apispark/guide/publish/publish/invocation#filter "filters") on entity properties and [sort](/technical-resources/apispark/guide/publish/publish/invocation#sort "sort") queries on Google Sheets wrapper APIs.

>**Note:** Non alphanumeric characters must be in quotes in your queries.  
>If your spreadsheet cells are in plain text format, they must also be in quotes in your queries.


# Google rate limits

The usage of the Google Sheets wrapper is subject to <a href="https://developers.google.com/apps-script/guides/services/quotas" target="_blank">Google rate limits</a>.

You can to turn on the [server cache in APISpark](/technical-resources/apispark/guide/publish/publish/invocation#configure-cache "server cache in APISpark") to mitigate the problem and improve performance.
