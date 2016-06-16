# 1. Language Specifics

## a. Follow a homogenous speech style

### No contractions
We do not use contractions such as “we don’t”, “we’ll” or “we haven’t” in the User Guide as we would do in a blog post.  
We write the whole sentence instead: “we do not”, “we will” or “we have not”.

### Friendly language
We use friendly, casual language (but not to casual!) such as:  
“Jump to the next section!”  
“Congrats, you have completed your first tutorial!”  
“Let’s get right to it!”

### Level of accuracy
#### in our User guide
We do not use numbered list inside the user guide.  
We do not detail all the steps such as “In the Create a web API window, enter a name in the Name field and click on the Ok button”. In this example, this would be enough: “Enter a Name for your API”.

#### in our Tutorials
We do use numbered list inside our tutorials which are step-by-step user guides.

### Follow style conventions
(bold characters for words written in the UI)
Italics for words we want to highlight.

## b.  Use a specific terminology

### Name APISpark cells
web API  
Entity Store  
File Store  
Descriptors  
Connectors  

### Name screen sections/buttons
<add screenshot>
action button  
Entities section  
Settings tab  
the console (instead of web IDE)  
the Dashboard  


# 2. Technical Aspects

## a. Write content in Markdown language
Documentation is written in Markdown language. You can use tools such as MarkdownPad or Atom to edit it easily.  
Use # for H1 title, ## for H2 title.  
For **bold** characters.  
For *italics*.  
For more information:   https://github.com/restlet/restlet-sites/blob/proxy-mode/modules/com.restlet/tech-doc/guidelines/syntax.md

## b. Follow the existing Structure

The documentation is a set of .md and .yml files classified in folders.  
Folders, .yml and .md files are prefixed by a number in order to display in a defined order e.g. 02_explore.

### Folders
Folders display as menus in the Table of Contents (ToC).
Each folder has an associated .yml file (e.g. 01_get-started folder has its get-started.yml file).

### .yml files
Each .yml file contains a title and a long title for the associated .md file.
e.g.
“title: Subscribe
long-title: Subscribe to a plan”
This .yml file generates the associated menu in the ToC.

### .md files
.md files contain the documentation content.
Each .md file has an associated .yml file (e.g. 01_sign-in.md file has its 01_sign-in.yml file) in order to display as an item in the ToC.


## c. Generate Table of Contents
Launch local scripts to generate the appropriate ToC.

## d. Add resources

### Insert screenshots
Screenshots are left-aligned.  
They must be 700px wide.  
Screenshots are named following the syntax below:  
“add-entity.jpg”  
Screenshots are saved in jpg format.  
Highlight the part of interest in a square and darken the rest of the screenshot.   
If the area we want to highlight is hardly visible, add a red arrow (Skitch funky arrow medium size).

### Insert images
To insert images in the documentation pages, create a folder called “images” at the same level than the .md file and follow the syntax below to call your image:

![Add an entity](images/add-an-entity.png "Add an Entity")

The text in square brackets will display on the page as a clickable link.  
The text in quotes displays in the tooltip.

#### Insert clickable images

[ ![Restlet in action (Manning)][1]][2]
[1]: images/restlet-in-action-120.png
[2]: http://www.amazon.com/gp/product/193518234X/ref=as_li_tf_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=193518234X&linkCode=as2&tag=restlet-20

>**Note**: Each reference number e.g. [1] must be unique in the page

#### Insert animated GIFs

Insert animated GIFs in tutorials only.  
Tool: LICEcap (available on mac as well).  
GIF size: 1065x510

### Insert links
#### External links
To insert external links, we follow the syntax below:
<a href="http://support.restlet.com/" target="_blank">Help Desk</a>
 target="_blank" is added to open in a new window.

#### Internal links
To insert internal links (links to another section of the documentation), we follow the syntax below:  
[Firewall settings](/technical-resources/apispark/guide/publish/secure/firewall-settings "Firewall settings")  
The text in square brackets will display on the page as a clickable link.  
The text in quotes displays in the tooltip.  

##### Insert links with customizable parts
Use {customizable part} rather than <customizable part> which will not display:
https://apispark.restlet.com/apis/\{cell number\}/versions/\{version number\}/overview

##### Internal links in DHC doc
As DHC doc is also integrated into the product, we need internal links to be relative links (rather than absolute links).  
So we will not write "/technical-resources/dhc/guide/drive/search-item"  
but "./drive/search-item" (if in current folder)  
>**Note:**
./ means current folder  
../ means previous folder

### Insert manual anchors

#### Inside the same page
To insert manual anchors, follow the syntax below:
go to [Sparky](#1)

<a class="anchor" name="1"></a>Sparky  

The text in square brackets will display on the page as a clickable link.
"1" will not display.

#### Outside the page

You will need to [synchronize](/technical-resources/apispark/guide/create/overview#synchronize "synchronize") each store imported.

--> Add <a class="anchor" name="synchronize"></a> where you want to redirect the user

## e. Tutorial titles

Insert specific icons in tutorials according to the titles

### Introduction
<h1 class="iconed" id="toc_0"><i class="fa fa-hand-o-right"></i>Introduction</h1>

### Requirements
<h1 class="iconed" id="toc_0"><i class="fa fa-flag-checkered"></i>Requirements</h1>

### Part 1
<h1 class="numbered" id="toc_1"><i>1</i>Create an Entity Store</h1>

### Part 2
<h1 class="numbered" id="toc_2"><i>2</i>Create a Web API</h1>

### Part 3
<h1 class="numbered" id="toc_3"><i>3</i>Invoke the web API</h1>

### Part 4
<h1 class="numbered" id="toc_4"><i>4</i>Invoke the Web API</h1>

# 3. Documentation management

## Create a new tag before pushing doc to production

Create a tag from SourceTree.  
The tag name should follow this structure (and is inspired from the product version):
v1.32.2

## Fill in the changes file
Each tag content is specified in the changes file located in tech-doc folder.

## Former features

Some parts of the user guide / tutorials were removed as some features were removed. You will find some of the removed pages in the tag called "removed_unused_files"
