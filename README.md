# Doom Struct

Copyright (c) 2015 - 2016 Matt Tropiano

Modified in 2018 by Joseph Catrambone  

### Author's Note:

This library is a duplicate of Matt Tropiano's Doom Struct, but with the Black Rook library requirements removed and replaced with the Java Standard Library. 

### NOTICE

This library is an offshoot/rewrite of [Black Rook Software's Doom Struct Project](https://github.com/BlackRookSoftware/Doom), 
of which I have been granted rights to rewrite (the rights were granted to myself BY myself).

All deprecated classes from the origin project will not be in this one.
All end users are encouraged to switch to this one, as this project will be 
actively maintained.

### Source

The MASTER branch contains stable code (I hope). The DEVELOP branch's contents may always
shift.

### Introduction

The purpose of the Doom Struct project is to provide a means to read/write
data structures for the Doom Engine and similar derivatives.

### Implemented Features (so far)

- Reads WAD files.
- Can read PK3 package type.
- Reads all Doom map and data structures in Doom, Hexen/ZDoom, or Strife 
  formats. This includes textures, patches, lines, vertices, things, sectors,
  nodes, palettes, colormaps, text, PNG data, MUS data, flats, blockmaps,
  reject, and even ENDOOM-type VGA lumps.
- Contains a utility class for converting Doom graphics to standard Java
  graphics structures.
- Can read/edit Boom-engine data lumps like ANIMATED and SWITCHES. 
- Full UDMF parsing/writing support.

### Library

Contained in this release is a series of libraries that allow reading, writing,
and extracting data in Doom Engine structures, found in the **net.mtrop.doom** 
packages. 

### Compiling with Ant

To download the dependencies for this project (if you didn't set that up yourself already), type:

	ant dependencies

A *build.properties* file will be created/appended to with the *dev.base* property set.
	
To compile this library with Apache Ant, type:

	ant compile

To make a JAR of this library, type:

	ant jar

And it will be placed in the *build/jar* directory.

### Other

This program/library and the accompanying materials
are made available under the terms of the GNU Lesser Public License v2.1
which accompanies this distribution, and is available at
http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html

A copy of the LGPL should have been included in this release (LICENSE.txt).
If it was not, please contact me for a copy, or to notify me of a distribution
that has not included it. 
