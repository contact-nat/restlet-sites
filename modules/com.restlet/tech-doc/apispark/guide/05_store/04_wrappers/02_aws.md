# Introduction

APISpark features a Wrapper File Store for Amazon <a href="
http://aws.amazon.com/s3/" target="_blank">AWS S3</a>
 buckets. The S3 Wrapper  lets you serve and store files from existing S3 buckets through a web API.

This guide does not explain how to configure AWS S3, but simply lists the information you will need to collect from your AWS administrator in order to configure the APISpark S3 Wrapper.

>**Note:** You will need your AWS access key ID and secret access key to be able to use your AWS S3 account.

# Configure your S3 File Store Wrapper

Click on the **+File Store** button, select "AWS S3" from the **Type** drop-down menu, and give your store a name. In this example we named our store "AWS S3 File Store".

![Create AWS Store](images/create-aws-store.jpg "Create AWS Store")

Click on the **Settings** tab. From the **Security** section, select **AWS Account**.

![Settings tab](images/aws-settings-tab.jpg "Settings tab")

Enter your **Access Key ID** and **Secret Access Key** and click on the **Test** button.

In the **Imports** section, click on the **Add** button. From the **Bucket** drop-down menu, select the bucket you want to import and click on the **Import bucket** button.

![Import bucket](images/aws-import-bucket.jpg "Import bucket")

Click on the **Folders** tab and on the **Add folders** button to create the associated folders.

![Add folders](images/aws-add-folders.jpg "Add folders")

If your bucket contains several root folders, associated folders are created for each of them. You can then explore the folders individually by clicking on them.

# Use your File Store Wrapper

Click on the **Deploy** button.

Once your File Store has been deployed, you can simply export it as a new web API via the actions button or import it from an existing web API. For more details on how to do this, please check out our [tutorials](/technical-resources/apispark/tutorials "tutorials").
