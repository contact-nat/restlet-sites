Sections
========

This is an H1
=============

This is an H2
-------------

# This is an H1

## This is an H2

###### This is an H6

## Markdown plus h2 with a custom ID ##         {#id-goes-here}
[Link back to H2](#id-goes-here)


<a name="Text formatting"></a>Text formatting
======

### Style

_italics_
*italics*

__bold__
**bold**

inline `code`.


### blockquote

> This is a **blockquote** with two paragraphs. Lorem ipsum dolor sit amet,
consectetuer adipiscing elit. Aliquam hendrerit mi posuere lectus.
Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.

> Donec sit amet nisl. Aliquam semper ipsum sit amet velit. Suspendisse
id sem consectetuer libero luctus adipiscing.

### nested blockquote

> This is a blockquote with two paragraphs. Lorem ipsum dolor sit amet,
consectetuer adipiscing elit. Aliquam hendrerit mi posuere lectus.
Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.

> > **Donec** sit amet nisl. Aliquam semper ipsum sit amet velit. Suspendisse
id sem consectetuer libero luctus adipiscing.

### Lists

*   Red
*   Green
*   Blue


1. Bird
1. McHale
1. Parish
1. Test

### Nested list

* A
 * A1
 * A2
* B
 1. B1
 2. B2

### Advanced list

With paragraphs and images (use tabulation).

1. first
2. second

  still in second item

  ![picture alt](images/300x50.jpg)

  images works too

3. third

## Special characters

Inside some block, you need to escape specials characters with a backslash **'\\'**:
- Symboles : \\ \` \*  \_
- Parentheses : \{ \} \[ \] \( \)
- Ponctuations : \#  \. \!
- Signes : \+ \-


Postproduction
===========

<!-- .note -->

> my note. *(respect empty line before and after!)*

Links
=====

Http
- Simple : http://restlet.com
- With text : [Restlet website](http://restlet.com)
- With text and tooltip : [Restlet website](http://zenika.com "the tooltip")

Internal link:

Prefix with `technical-resources/apispark/guide/` and use file system structure (without the number prefix or file extension) to generate the URL.

Example: [Export an API](/technical-resources/apispark/guide/store/entity-stores/overview "Export an API")

Email
- Email : <info@reslet.com>


Codes
=====

### Inline

Use backquotes `this is code` to delimite code.

Use double backquotes ``code with ` symbol`` for specials characters.

### code block

java
```java
private String name = "value";
```

sql
```sql
select * from table;
```

json
```json
{
  "name": "me",
  "age": 12
}
```

xml
```xml
<person name="me" age="12" />
```


Tables
======

**note**: very basic support

### Simple

| Header 1 | Header 2 |
| ------------- | ------------- |
| A | yes
| B | yes
| C | no


### Alignment

Configure alignment with `;`

| Justified | Normal | Number
| :-------------: | ------------- | ------:
| A | Javascript | 1000
| B | Angular | 1500
| Long text | Java | 1

Images
======

### Full size

Relative path:

![picture alt](images/400x200.jpg "Title is optional")

URL:

![](http://lorempixel.com/400/200/technics/)

Le Lorem Ipsum est simplement du faux texte employé dans la composition
et la mise en page avant impression.
Inline image ![alt](images/30x30.jpg) is supported to!
Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500,
quand un peintre anonyme assembla ensemble des morceaux de texte pour réaliser un
livre spécimen de polices de texte. Il n'a pas fait que survivre cinq siècles, mais
s'est aussi adapté à la bureautique informatique, sans que son contenu n'en soit
modifié. Il a été popularisé dans les années 1960 grâce à la vente de feuilles
Letraset contenant des passages du Lorem Ipsum, et, plus récemment, par son
inclusion dans des applications de mise en page de texte, comme Aldus PageMaker.

### Custom position

<!-- TODO center -->
Code for images that need to be centered (rather than left-aligned)

Others
======

### Horizontal lines

### Add anchors
Go to [Text formatting](#Text formatting)



### Insert comments that will not display to end-users

<!---This is my comment
-->

****

Needs
======

colors ??
