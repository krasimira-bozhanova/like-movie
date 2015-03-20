#!/usr/bin/perl
use warnings;
use strict;

# Use on wikimedia databases.
#
# Put the category entries (2nd field in the table) on the line
# corresponding to their page_id (first field in the table)
# Requires each entry to be ordered by page_id. 
#
# Call as e.g.
# > thisScript.pl commonswiki-20130216-categorylinks.sql > Outfile.unicodetext

my $started = 0;
my $apostrophes = 0;
my $fullline="";
my $prev_id=100000; # hacky. This is a number greater than the first page_id
$/ = '(';
while (my $line = <>) {
        unless ($started) {
                $started = 1 if ($line =~ /INSERT INTO .categorylinks. VALUES\s+\($/);
        } else {
                #count the number of unescaped apostrophes
                $apostrophes += () = ($line =~ m/(?<!\\)(?:\\\\)*'/g); #see http://stackoverflow.com/questions/56554/what-is-the-proper-regular-expression-for-an-unescaped-backslash-before-a-charac
                $fullline.=$line;
                unless ($apostrophes % 2) {
                        $apostrophes=0;
                        if ($fullline =~ /^(\d+),\s*('.+?(?<!\\)(?:\\\\)*')/) {
                                my $diff = $1 - $prev_id;
                                $2 =~ s/\n\r//g;
                                if ($diff==0) {
                                        print ','.$2;
                                } elsif ($diff>0) {
                                        print "\n" x $diff;
                                        print $1.':'.$2;
                                } else { #either this is the first one, or we have problems
                                        die "Page_ids not in order for ($fullline)" if ($started==2);
                                        $started=2;
                                        print "\n" x ($1-1);
                                        print $1.':'.$2;
                                }
                                $fullline="";
                                $prev_id=$1;                    
                        }
                }
        }
}
