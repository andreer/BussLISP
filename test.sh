#!/bin/bash
(cd target/classes; /usr/local/opt/openjdk/bin/java -Xss1G BussLISP < ../../test.lisp)
