#! /usr/bin/env python
# -*- coding: utf-8 -*-

import argparse
import os
import yaml
import re
import sys
import shutil

def main():
    parser = argparse.ArgumentParser(description='Create ToC (toc.yml) from a given path. '
                                                 'All files & folders must begin with xx_name.')
    parser.add_argument('--path', '-p',  type=str, help='Path to scan. If not provided takes current path.')
    parser.add_argument('--markdown', '-m',  action='store_true', help='If provided, return a markdown file.')
    parser.add_argument('--dhcjsonindex', '-dhci',  action='store_true', help='If provided, return a JSON representation of the client help guide.')
    parser.add_argument('--dhcfiles', '-dhcf',  type=str, help='Path where the DHC help files are saved (including images). If not provided takes current path.')

    args = parser.parse_args()
    path = args.path
    markdown = args.markdown
    dhcjsonindex = args.dhcjsonindex
    dhcfiles = args.dhcfiles

    if path is None:
        path = os.getcwd()

    document = []
    scan_dir(path, document, '', '')

    obj = {'toc': document}

    if markdown:
        convert_to_markdown(document)
    elif dhcjsonindex:
        convert_to_dhc_json_index(document)
    elif dhcfiles:
        convert_to_dhc_files(document, path, dhcfiles)
    else:
        print yaml.dump(obj, default_flow_style=False)


def scan_dir(path, document, path_to_display, link_url):

    elements = os.listdir(path)
    elements.sort()

    for element in elements:

        new_elem = os.path.join(path, element)

        if os.path.isdir(new_elem):

            # Check that the directory has a matching .yml file
            if os.path.exists(new_elem + '.yml'):
                add_dir(new_elem, document, new_elem + '.yml', path_to_display, link_url)

        elif os.path.isfile(new_elem):

            # Find markdown files with a corresponding .yml file
            regexp = re.match(r'(.*)\.md', new_elem)
            if regexp and os.path.exists(regexp.group(1) + '.yml'):
                add_markdown_file(new_elem, document, regexp.group(1) + '.yml', path_to_display, link_url)


def add_dir(path, document, yml_path, path_to_display, link_url):
    to_add = yaml.load(open(yml_path, 'r'))
    document.append(to_add)

    basename = os.path.basename(path)
    to_add['dir'] = basename
    try:
        to_add['id'] = re.match(r'[0-9]*_(.*)', basename).group(1)
    except Exception:
        sys.stderr.write('Error with folder %s. Wrong regexp.' % path)
        sys.exit(1)

    to_add['sourcepath'] = path
    to_add['targetpath'] = "%s/%s" % (path_to_display, to_add['id'])

    items_next = []
    to_add['items'] = items_next
    scan_dir(path, items_next, path_to_display + '/' + to_add['id'], link_url + '/' + to_add['dir'])


def add_markdown_file(path, document, yml_path, path_to_display, link_url):

    to_add = yaml.load(open(yml_path, 'r'))
    if not to_add:
        sys.stderr.write('Error with file %s' % yml_path)
        sys.exit(1)

    document.append(to_add)

    basename = os.path.basename(path)
    to_add['file'] = basename
    to_add['link_url'] = link_url + '/' + basename

    try:
        to_add['id'] = re.match(r'[0-9]*_(.*)\.md', basename).group(1)
    except Exception:
        sys.stderr.write('Error with file %s. Wrong regexp.' % path)
        sys.exit(1)

    to_add['url'] = path_to_display + '/' + to_add['id']


def convert_to_markdown(document):
    indentation = ''
    for line in document:
        print_line_as_markdown(line, indentation)

def print_line_as_markdown(line, indentation):
    if 'dir' in line.keys():
        # Is directory
        print '%s* %s' % (indentation, line['title'])
        for item in line['items']:
            print_line_as_markdown(item, indentation + '  ')
    elif 'file' in line.keys():
        # Is file
        print '%s* [%s](%s)' % (indentation, line['title'], line['link_url'])

def convert_to_dhc_json_index(document):
    listHelpPages = []
    listHelpPages.append('{"dir": false, "title":"%s", "path": "%s"}' % ("User Guide", "/index"))
    for line in document:
        print_line_as_dhc_json_index(listHelpPages, line, "/")
    print '[\n%s\n]' % (",\n".join(listHelpPages))

def print_line_as_dhc_json_index(listHelpPages, line, dir):
    if 'dir' in line.keys():
        # Is directory
        listHelpPages.append('{"dir": true, "title":"%s", "path": "%s"}' % (line['title'], dir + line['id']))
        for item in line['items']:
            print_line_as_dhc_json_index(listHelpPages, item, dir + line['id'] + "/")
    elif 'file' in line.keys():
        # Is file
        listHelpPages.append('{"dir": false, "title":"%s", "path": "%s"}' % (line['title'], line['url']))

def convert_to_dhc_files(document, sourcepath, dhcfiles):
    sourceFile = os.path.join(sourcepath, "index.md")
    destFile = os.path.join(dhcfiles, 'index.md')
    ensure_dir(destFile)
    shutil.copyfile(sourceFile, destFile)
    if os.path.exists(os.path.join(sourcepath, 'images')):
        shutil.copytree(os.path.join(sourcepath, 'images'), os.path.join(dhcfiles, 'images'))
    
    for line in document:
        copy_to_dhc_files(line, sourcepath, dhcfiles)

def copy_to_dhc_files(line, sourcepath, dhcfiles):
    if 'dir' in line.keys():
        imagesDir = os.path.join(line['sourcepath'], 'images');
        if os.path.exists(imagesDir):
            shutil.copytree(imagesDir, os.path.join(dhcfiles, line['targetpath'][1:], 'images'))
        for item in line['items']:
            copy_to_dhc_files(item, sourcepath, dhcfiles)
    if 'file' in line.keys():
        sourceFile = os.path.join(sourcepath, line['link_url'][1:])
        destFile = os.path.join(dhcfiles, '%s.md' % (line['url'][1:]))
        ensure_dir(destFile)
        shutil.copyfile(sourceFile, destFile)

def ensure_dir(f):
    d = os.path.dirname(f)
    if not os.path.exists(d):
        os.makedirs(d)

if __name__ == "__main__":
    main()