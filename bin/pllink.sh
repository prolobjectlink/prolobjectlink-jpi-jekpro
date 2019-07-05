#!/usr/bin/bash
java -classpath "$(dirname "$(pwd)")/lib/*" org.prolobjectlink.prolog.jekpro.JekejekePrologConsole ${1+"$@"}