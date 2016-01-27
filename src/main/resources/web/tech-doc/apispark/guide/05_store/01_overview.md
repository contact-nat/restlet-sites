# Introduction to the APISpark Data Stores

APISpark features two types of Data Stores: the Entity Store and the File Store.

The Entity Store is used to store structured data while the File Store provides storage for static files.

Both of these Data Store types have a set different implementations to chose from. The main distinction is that there are *local* implementations that are hosted on the APISpark platform, and *wrapper* implementations that use or wrap third party storage components such as Google Spreadsheets.

## Entity Stores

All the Entity Stores share common concepts and a common user interface (with a few minor differences).

An Entity Store is made up of *entities*, which are types of objects, and their associated *properties*, which store values.

## File Stores

All the APISpark File Stores also share common concepts and a common user interface (with a few minor differences).

A file store is made up of *folders*, similar to those you would find in a filesystem.
