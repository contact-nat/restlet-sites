#! /usr/bin/env python
# -*- coding: utf-8 -*-

import argparse
import os
import yaml
import re
import sys


def main():
    parser = argparse.ArgumentParser(description='Create ToC (toc.yml) from a given path. '
                                                 'All files & folders must begin with xx_name.')
    parser.add_argument('--path', '-p',  type=str, help='Path to scan. If not provided takes current path.')
    parser.add_argument('--markdown', '-m',  action='store_true', help='If provided, return a markdown file.')
    parser.add_argument('--gwtsources', '-gs',  action='store_true', help='If provided, return a list of GWT sources.')
    parser.add_argument('--gwtpanel', '-gp',  action='store_true', help='If provided, return a list of GWT sources.')
    parser.add_argument('--gwtjsonindex', '-gj',  action='store_true', help='If provided, return a list of GWT sources.')

    args = parser.parse_args()
    path = args.path
    markdown = args.markdown
    gwtsources = args.gwtsources
    gwtpanel = args.gwtpanel
    gwtjsonindex = args.gwtjsonindex

    if path is None:
        path = os.getcwd()

    document = []
    scan_dir(path, document, '', '')

    obj = {'toc': document}

    if markdown:
        convert_to_markdown(document)
    elif gwtsources:
        convert_to_gwt_sources(document)
    elif gwtpanel:
        convert_to_gwt_panel(document)
    elif gwtjsonindex:
        convert_to_gwt_json_index(document)
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

def convert_to_gwt_sources(document):
    indentation = '    '
    for line in document:
        print_line_as_gwt_sources(line, indentation)

def print_line_as_gwt_sources(line, indentation):
    if 'dir' in line.keys():
        for item in line['items']:
            print_line_as_gwt_sources(item, indentation)
    elif 'file' in line.keys():
        # Is file
        print '%s@Source("%s")' % (indentation, line['link_url'][1:])
        print '%sTextResource %s();' % (indentation, line['url'].replace("/", "_").replace("-", "_"))

def convert_to_gwt_panel(document):
    indentation = '    '
    for line in document:
        print_line_as_gwt_panel(line, indentation, "/")
def print_line_as_gwt_panel(line, indentation, dir):
    if 'dir' in line.keys():
        # Is directory
        print '%s%s.addItem("%s", "%s", null)' % (indentation, indentation, line['title'], dir + line['id'])
        for item in line['items']:
            print_line_as_gwt_panel(item, indentation, dir + line['id'] + "/")
    elif 'file' in line.keys():
        # Is file
        print '%s%s.addItem("%s", "%s", toHTML(DocsResources.INSTANCE.%s()))' % (indentation, indentation, line['title'], line['url'], line['url'].replace("/", "_").replace("-", "_"))

def convert_to_gwt_json_index(document):
    indentation = '    '
    listHelpPages = []
    for line in document:
        print_line_as_gwt_json_index(listHelpPages, line, "/")
    print '[\n%s\n]' % (",\n".join(listHelpPages))

def print_line_as_gwt_json_index(listHelpPages, line, dir):
    if 'dir' in line.keys():
        # Is directory
        listHelpPages.append('{"dir": true, "title":"%s", "path": "%s"}' % (line['title'], dir + line['id']))
        for item in line['items']:
            print_line_as_gwt_json_index(listHelpPages, item, dir + line['id'] + "/")
    elif 'file' in line.keys():
        # Is file
        listHelpPages.append('{"dir": false, "title":"%s", "path": "%s"}' % (line['title'], line['url']))
 
if __name__ == "__main__":
    main()