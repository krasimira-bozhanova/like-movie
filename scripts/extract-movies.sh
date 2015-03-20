#/bin/sh

# Expects $1 to be a path to a LANGwiki-REVISION-categorylinks.sql.gz file. Outputs
# a file named movies.txt containing only articles about movies (using a the heuristic
# that all movies have the NUM{4}_films category).

zcat $1 | perl categorylinks-extractor.pl | grep '[0-9]\{4\}_films' > movies.txt
