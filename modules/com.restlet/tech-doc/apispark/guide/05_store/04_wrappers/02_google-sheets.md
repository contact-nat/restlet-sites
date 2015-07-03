# Introduction

APISpark features a Wrapper Entity Store for Google Sheets spreadsheets. The Google Sheets Wrapper lets you expose data stored in a spreadsheet through a web API.

![GSheet to wrapper](images/gsheet-to-wrapper.jpg "GSheet to wrapper")

# Create a Google Sheets Wrapper

To know how to create a Google Sheets wrapper step by step, jump to our [Google Sheets tutorial](/technical-resources/apispark/tutorials/turn-spreadsheet-to-api "Google Sheets tutorial").

You can import one spreadsheet per wrapper. If you need data from several spreadsheets, create one wrapper per spreadsheet and import them into one single API.

# Perform queries

You can apply simple [filters](/technical-resources/apispark/guide/publish/publish/invocation#filter "filters") on entity properties and [sort](/technical-resources/apispark/guide/publish/publish/invocation#sort "sort") queries on Google Sheets wrapper APIs.

>**Note:** Non alphanumeric characters must be in quotes in your queries.  
 If your spreadsheet cells are in plain text format, they must also be in quotes in your queries.
