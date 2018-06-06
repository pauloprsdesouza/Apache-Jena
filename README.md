# SWRL rules
Basic tutorial to generate inference in OWL using SWRL rules

# Introduction
<p>
  This tutorial was developed using the language JAVA and the Framework <img src="https://jena.apache.org/images/jena-logo/jena-logo-jumbotron.png" width="30"/> <a href="https://jena.apache.org/">[Apache Jena]</a>. Aims to auxiliate who users  
<p>
  
# Dependencies
<pre>Apache Jena version 3.0</pre>
### Maven
  ```xml
<dependency>
    <groupId>org.apache.jena</groupId>
    <artifactId>apache-jena-libs</artifactId>
    <type>pom</type>
    <version>3.0</version>
</dependency>
```
# Examples
* First case
  * [<b>S1</b>: (?x <b>eg:hasSon</b> ?y), (?y <b>eg:hasSon</b> ?z), (?w <b>eg:hasSpouse</b> ?y) -> (?z <b>eg:hasGrandFather</b> ?x), (?w <b>eg:hasSon</b> ?z)]
