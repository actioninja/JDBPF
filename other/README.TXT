README - DBPF4J
===============

INTRODUCTION
------------
Working with DBPF format is not easy as it seems. This package try to
provide full read and write of DBPF format, manipulating exemplars,
adding new entries and much more. The package is written in JAVA, so
it is platform-independent.


INSTALLATION
------------
Simply extract all contents of the ZIP-Archive to your wished folder
with full pathnames to prevent structure of the ZIP-file.

Then you see the file DBPF4J.jar, which you can use in your program. 
The changelog.html shows the last changelogs.

This package support the binary (EQZB1) and text (EQZT1) format in 
version 1.
For correct T-Format a property file has to loaded and set with: 
DBPFProperties.propertyList=...(LinkedHashMap<Long, String>).
The long is the property id and the string is the property string.
Otherwise only the binary format will be handled correctly!


BUGS & IDEAS
------------
If you found any bugs or have ideas to improve the package, feel free to
contact me (see CONTACT at bottom of this document).
It would also be great, if you tell me about your using, e.g. about your 
own program.


SOURCECODE
----------
The newest sourcecoude can obtain from the author or Sourceforge.net (CVS):
http://sourceforge.net/projects/sc4dbpf4j/


TRANSLATION
-----------
Any outputs (toString) of the package is written in english for convenience. 
Translation would not make sense in this package, so do not ask for this.


LICENCES
--------
See LICENSE_GPL.TXT for information about license.


CONTACT
-------
You can reach the author at: mapsonswen@web.de


Document version: 5, 2010-04-02  