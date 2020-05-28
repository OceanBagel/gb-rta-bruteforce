# gb-rta-bruteforce
==========

This repo is generally just a dumping ground for bots, with most of the code from stringflow, entrpntr, and Dabomstew.

Original README (with minor relevant fixes) below

#### Overview

This is base code mimicking [entrpntr's codebase](http://github.com/entrpntr/gb-rta-bruteforce) using JNA rather than JNI. 

#### Installation

Build `libgambatte.dll/.dylib/.so` (Instructions can be found [here](https://github.com/pokemon-speedrunning/gambatte-speedrun))

(You might need to change the libgambatte extension [here](https://github.com/CasualPokePlayer/gb-rta-bruteforce/blob/master/src/rta/gambatte/Libgambatte.java#L10) depending on your OS.)

Clone the repository, create a new folder called `libgambatte` and put the previously built library file in there. 

Create another folder called `roms` and put `gbc_bios.bin` as well as `poke<game name>.gbc` in there.  

After that fire up your favorite Java IDE, add the JNA library to the classpath and start using it.
