#!/usr/bin/python

import sys
from collections import defaultdict
from bs4 import BeautifulSoup as Soup
from urllib.request import urlopen
from soupselect import select
import re

movies_file = sys.argv[1];
imdb_links_file = sys.argv[2];
imdb_links = defaultdict(list)

with open(movies_file, 'r') as movies_resource, open(imdb_links_file, 'r') as imdb_links_resource, open("movies_with_imdb_ids.txt","a") as out:
    for line in imdb_links_resource:
        page_id, imdb_link = line.split(':', 1)
        imdb_id = imdb_link.split('/')[-2]
        imdb_links[page_id].append(imdb_id)

    for line in movies_resource:
        page_id, page_categories = line.split(':', 1)
        imdb_ids = imdb_links[page_id]
        imdb_ids_size = len(imdb_ids)

        if imdb_ids_size == 0:
            continue;

        if imdb_ids_size > 1:
            # print('http://en.wikipedia.org/?curid={0}'.format(page_id))
            soup = Soup(urlopen('http://en.wikipedia.org/?curid={0}'.format(page_id)))
            try:
                links = soup.find(id='External_links').parent.find_next('ul').find_all(href=re.compile("imdb\.com/title/tt\d+"))
            except AttributeError:
                continue

            if not links:
                continue;

            link = links[0]['href']
            if link[-1] != '/':
                link += '/'
            imdb_ids = [link.split('/')[-2]]

        # if imdb_ids_size != 1:
        #     imdb_link = input('Manually enter an IMDB link for article http://en.wikipedia.org/?curid={0} (or blank to skip, processed {1}): '.format(page_id, processed))
        #     if not imdb_link:
        #         continue;
        #     imdb_ids = [imdb_link.split('/')[-2]]

        imdb_id = 'tt' + imdb_ids[0][2:].zfill(7)
        out.write("{0}:{1}:{2}".format(page_id, imdb_id, page_categories))
