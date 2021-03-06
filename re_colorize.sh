#!/bin/bash
#
# Simple colorize for bash by means of sed
#
# Copyright 2008 by Andreas Schamanek <andreas@schamanek.net>
# GPL licensed (see end of file) * Use at your own risk!
#
# Usage examples:
#   tail -f somemaillog | mycolorize white '^From: .*' bell
#   tail -f somemaillog | mycolorize white '^From: \/.*' green 'Folder: .*'
#
# Notes:
#   Regular expressions need to be suitable for _sed_
#   Slashes / need no escaping (we use ^A as delimiter)
#   \/ splits the coloring (similar to procmailrc. Matches behind get color.
#   Even "white 'for \/\(her\|him\).*$'" works :) Surprisingly ;)

# For the colors see tput(1) and terminfo(5)
red=$(tput bold;tput setaf 1)            # bright red text
green=$(tput setaf 2)                    # dim green text
yellow=$(tput bold;tput setaf 3)         # bright yellow text
fawn=$(tput setaf 3) ; beige=$fawn       # dark yellow text
blue=$(tput bold;tput setaf 4)           # bright blue text
purple=$(tput setaf 5) ; magenta=$purple # magenta text
pink=$(tput bold;tput setaf 5)           # bright magenta text
cyan=$(tput bold;tput setaf 6)           # bright cyan text
gray=$(tput setaf 7)                     # dim white text
white=$(tput bold;tput setaf 7)          # bright white text
normal=$(tput sgr0)                      # normal text

bell=$(tput bel)                         # bell/beep

# produce separator character ^A (for _sed_)
A=$(echo | tr '\012' '\001')

# compile all rules given at command line to 1 set of rules for SED
while [ "/$1/" != '//' ] ; do
  c1=''; re='';  beep=''
  c1=$1 ; re="$2" ; shift 2 || break
  # if a beep is requested in the optional 3rd parameter set $beep
  [ "/$1/" != '//' ] && [[ ( "$1" = 'bell' || "$1" = 'beep' ) ]] \
    && beep=$bell && shift
  # if the regular expression includes \/ we split the substitution
  if [ "/${re/*\\\/*/}/" = '//' ] ; then
     re="${re/\\\//\)\(}"
     sedrules="$sedrules;s$A\($re\)$A\1${!c1}\2$beep$normal${A}g"
  else
     sedrules="$sedrules;s$A\($re\)$A${!c1}\1$beep$normal${A}g"
  fi

done

# call sed to do the main job
sed -e "$sedrules"

exit

# License
#
# This script is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This script is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this script; if not, write to the
# Free Software Foundation, Inc., 59 Temple Place, Suite 330,
# Boston, MA  02111-1307  USA

