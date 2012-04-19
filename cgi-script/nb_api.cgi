#!/usr/bin/python

from datetime import datetime
import fileinput
import glob
import locale
import json
import re
import os
import string
import sys

locale.setlocale(locale.LC_TIME, 'de_DE.UTF-8')

# definitions
blog_home = '../../public_html/blog'
blog_link = 'http://public.beuth-hochschule.de/~sleuthold/blog'
version = 14

# debugging
if not 'QUERY_STRING' in os.environ:
    os.environ['QUERY_STRING'] = 'q=archive'

def getargdict():
    args = {}
    if 'QUERY_STRING' in os.environ:
        arglist = os.environ['QUERY_STRING'].split('&')
        for pair in arglist:
            elem = pair.split('=')
            args[elem[0]] = elem[1]
    return args

def getcategories(root):
    cats = []
    for path in glob.glob(root + '/data/cat_*.db'):
        i = 0
        cat = ''
        for line in fileinput.input(path):
            if i == 0:
                cat = line[0:-1]
            i += 1
        category = {}
        category['name'] = cat
        category['count'] = i - 1
        cats.append(category)
    return sorted(cats, key=lambda k: k['name']);

def getcategoryentries(root, category):
    category_entries = []
    entryfiles = []
    for path in glob.glob(root + '/data/cat_*.db'):
        i = 0
        cat = ''
        files = []
        for line in fileinput.input(path):
            if i == 0:
                cat = line[0:-1]
            if i > 0:
                files.append(line[0: line.find('>') ])
            i += 1
        if cat == category:
            entryfiles = files
    for entryfile in entryfiles:
        path = root + '/data/' + entryfile
        entry = {}
        entry['tag'] = entryfile
        entry['data'] = getentrymetadata(path)
        category_entries.append(entry)

    sortedList = sorted(
        category_entries, 
        key=lambda k: datetime.strptime(k['data']['DATE'], '%A, %B %d, %Y'), 
        reverse=True)
    
    nextE = ''
    pos = 0
    for entry in sortedList:
        entry['data']['NEXT'] = nextE
        nextE = entry['tag']
        if pos == len(sortedList)-1:
            entry['data']['BACK'] = ''
        else:
            pos += 1
            entry['data']['BACK'] = sortedList[pos]['tag']

    return sortedList;

def getarchive(root):
    archive = []
    old = {}
    old['year'] = '2009-2011'
    old['count'] = 191
    archive.append(old)

    gather = {}
    for path in glob.glob(root + '/data/*.txt'):
        entry = os.path.split(path)[1]
        year = entry[0:4]
        if year in gather:
            gather[year] = gather[year] + 1
        else:
            gather[year] = 1
    
    for year in gather.keys():
        item = {}
        item['year'] = year
        item['count'] = gather[year]
        archive.append(item)

    archive = sorted(archive,
    key=lambda k: k['year'], reverse=True)
    return archive

def getall(root):
    return getarchiveentries(root,'')

def getarchiveentries(root, year):
    year_entries = []
    for path in glob.glob(root + '/data/' + year + '*.txt'):
        entry = {}
        entry['tag'] = os.path.split(path)[1]
        entry['data'] = getentrymetadata(path)
        year_entries.append(entry)
    
    sortedList = sorted(
        year_entries, 
        key=lambda k: datetime.strptime(k['data']['DATE'], '%A, %B %d, %Y'), 
        reverse=True)
    
    nextE = ''
    pos = 0
    for entry in sortedList:
        entry['data']['NEXT'] = nextE
        nextE = entry['tag']
        if pos == len(sortedList)-1:
            entry['data']['BACK'] = ''
        else:
            pos += 1
            entry['data']['BACK'] = sortedList[pos]['tag']

    return sortedList;

def getentry(root, entry):
    f = open(root + '/data/' + entry)
    content = f.read()
    f.close()
    result = gethtmlheader()
    result += '<!DOCTYPE html>\n'
    result += '<html>\n'
    result += '<head>\n'
    result += '<link rel="stylesheet" href="http://public.beuth-hochschule.de/~sleuthold/blog/styles/nb_android.css" type="text/css" media="all" />\n'
    result += '<script type="text/javascript">  var _gaq = _gaq || [];  _gaq.push([\'_setAccount\', \'UA-23923935-2\']);  _gaq.push([\'_trackPageview\']);(function() {var ga = document.createElement(\'script\'); ga.type = \'text/javascript\'; ga.async = true; ga.src = (\'https:\' == document.location.protocol ? \'https://ssl\' : \'http://www\') + \'.google-analytics.com/ga.js\';    var s = document.getElementsByTagName(\'script\')[0]; s.parentNode.insertBefore(ga, s);  })();</script>\n'
    result += '</head>\n'
    result += '<body>\n'
    result += content[content.find('BODY:')+6 : content.find('END-----')-1]
    result += '</body>\n</html>\n'
    return result

def getentrynav(root, entry):
    entries = []
    for path in glob.glob(root + '/data/*.txt'):
        entries.append(os.path.split(path)[1])
    entries.sort()
    index = entries.index(entry)
    nav = {}
    if index > 0:
        nav['previous'] = entries[index - 1]
    else:
        nav['previous'] = None
    if index == len(entries) - 1:
        nav['next'] = None
    else:
        nav['next'] = entries[index + 1]
    nav['current'] = entry
    return nav

def getlatest(root):
    entries = []
    for path in glob.glob(root + '/data/*.txt'):
        entries.append(path)
    entries.sort()
    latest = {}
    latest['tag'] = os.path.split(entries[-1])[1]
    latest['data'] = getentrymetadata(entries[-1])

    latest['data']['FORWARD'] = ''

    latest['data']['BACK'] = os.path.split(entries[-2])[1]

    return latest

def getentrymetadata(entry_path):
    entry = {}
    stop = False
    for line in fileinput.input(entry_path):
        if line[0:5] == '-----':
            stop = True
        if stop == False:
            entry_meta = line.split(':', 1)
            entry[entry_meta[0]] = entry_meta[1].rstrip('\n').strip(' ')
    entry['PERMALINK'] = getlink(entry_path, entry['TITLE'])
    return entry

def getlink(entry_path, title):
    link = re.sub('[\.]', '_', title)
    link = re.sub('[^ a-zA-Z0-9_-]', '', link)
    link = re.sub('[ ]','_', link.lower())[0:150]
    link += '/index.html'

    filename = os.path.split(entry_path)[1]
    year = filename[0:4]
    month = filename[5:7]
    day = filename[8:10]
    link = blog_link + '/archives/' + year + '/' + month + '/' + day + '/' + link
    return link

def getjsonheader():
    res  = 'Content-Type: application/json\n'
    res += 'Connection: close\n\n'
    return res

def gethtmlheader(pre=False):
    res   = 'Content-Type: text/html\n'
    res += 'Connection: close\n\n'
    if pre:
        res += '<html><pre>'
    return res

def gethtmlfooter(pre=False):
    if pre:
        return ''
    else:
        return '</pre></html>'

def getemptyheader():
   return 'HTTP/1.1 204 No Content\r\n\r\n'

def getenv():
    res = ''
    for env in os.environ:
        res += "env( " + env + " ) = ( " + os.environ[env]  + " )\n"
    return res



# get the query
query = getargdict()

output = ''

# no arguments, exit
if len(query) == 0:
   output += getemptyheader()

response = ''

if 'q' in query:
    if query['q'] == 'version':
        output += getjsonheader()
        currentversion = []
        v = {}
        v['version'] = version
        currentversion.append(v)
        response = json.dumps(currentversion, indent=4)
    elif query['q'] == 'categories':
        output += getjsonheader()
        if 'category' in query:
            response = getcategoryentries(blog_home, query['category'])
        else:
            response = getcategories(blog_home)
        response = json.dumps(response, indent=4)
    elif query['q'] == 'archive':
        output += getjsonheader()
        if 'year' in query:
            response = getarchiveentries(blog_home, query['year'])
        else:
            response = getarchive(blog_home)
        response = json.dumps(response, indent=4)
    elif query['q'] == 'details':
        output += getjsonheader()
        entryList = []
        if 'year' in query:
            entryList= getarchiveentries(blog_home, query['year'])
        elif 'category' in query:
            entryList = getcategoryentries(blog_home, query['category'])
        else:
            entryList = getall(blog_home)

        for entry in entryList:
            if entry['tag'] == query['tag']:
                response = entry
                break
    
        response = json.dumps(response, indent=4)
    elif query['q'] == 'entry':
        if 'tag' in query:
            response = getentry(blog_home, query['tag'])
    elif query['q'] == 'nav':
        if 'tag' in query:
            output += getjsonheader()
            response = getentrynav(blog_home, query['tag'])
            response = json.dumps(response, sort_keys=True, indent=4)
    elif query['q'] == 'latest':
        output += getjsonheader()
        response = getlatest(blog_home)
        response = json.dumps(response, sort_keys=True, indent=4)

    output += response

if 'format' in query:
    if query['format'] == 'html':
        output  = gethtmlheader(True) + output
        # output += getenv()
        output += gethtmlfooter(True)


sys.stdout.write(output)

