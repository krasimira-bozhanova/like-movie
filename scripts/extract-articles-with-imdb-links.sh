#/bin/sh


# Expects $1 to be a path to a LANGwiki-REVISION-externallinks.sql.gz file. Outputs a file named
# "articles_with_imdb_movie_links" which contain lines of the following form: PAGEID:'IMDB link'.

zcat $1 | grep -Eo "[0-9]+,0,'http://www.imdb.com/title/tt[0-9]*/'" | awk -F',' '{print $1 ":" $3}' > articles_with_imdb_movie_links.txt
