
A web API has a set of **Representations** that are used as input and output types for the API's resource methods.

A **Representation** has a **Name** and a **Description** (optional), and is composed of **Properties** which hold values.

To edit a web API's **Representations**, navigate to the web API's **Overview**. The API's representations are listed in the **Representations** section in the left panel.

Once you have select a **Representation**, you can view its properties.

Representation properties have a **Name**, **Description**, **Datatype** and  belong to a section.

![Representations](images/representations.jpg "Representations")

## Representation properties

Representations are composed of properties. A Representation property has a number of different attributes that can be set.

![Representation properties](images/representation-properties.jpg "Representation properties")

You can modify the following attributes for an existing Representation property. The remaining attributes will be modified from the Entity Store instead of the API.

### Name

The property's name. You cannot edit the property's name of an API published version. To be able do edit it, you will need to revert to a draft version.

### Hidden

Select this checkbox so that the Representation properties are not included into client kits or when the API is invoked. You may want to include only part of your properties.

### Sample value

A sample value can be provided in this field. It will be suggested to the user when s/he adds a record.


## Representation formatting

From the **Settings** tab, you can determine whether you want your API to return all values, all values except null values or all values except null and empty values.

Navigate to the **Settings** tab and select the appropriate formatting from the drop-down menu.

![Representation formatting](images/representation-formatting.jpg "Representation formatting")

## Collection formatting

By default, collections are represented as *lists of items*.
You can determine whether you want your API to return results represented as lists of items as shown below;

![Collections represented as lists](images/collections-as-lists.jpg "Collections represented as lists")

or whether you want your API to return results represented *within a wrapper* (an {entity}List entity is automatically created for each new entity added).

![Collections within a wrapper](images/collections-within-wrapper.jpg "Collections represented within a wrapper")

This setting can be changed from the **General** section of the API **Settings** tab.

![Change collections formatting](images/change-collection-formatting.jpg "Change collections formatting")

>**Note:** You will need to synchronize each store imported and then redeploy your API in order for your changes to be taken into account.

# Variants

A **Representation** has one or more **Variants** which determine how the representation is formatted, serialized, and deserialized.

By default, APISpark supports 3 different types of representation variants: XML, JSON or YAML.  

To view a representation's **Variants**, select the representation from the **Representations** section in the left panel.

![Variants](images/variants.jpg "Variants")

>**Note:** When you want to POST a new entry via [Postman](/technical-resources/apispark/guide/create/test "Postman") tool for example, you can use the **Sample** text area content to make sure you are using the appropriate syntax.
