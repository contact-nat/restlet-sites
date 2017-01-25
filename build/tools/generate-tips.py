#! /usr/bin/env python
# -*- coding: utf-8 -*-

import argparse
import os
import json
import re
import sys
import shutil

def main():
  parser = argparse.ArgumentParser(description='Aggregate HTML tips for Restlet Client\'s did you know dialog in a JSON file.')
  parser.add_argument('--path', '-p',  type=str, help='Path to scan. If not provided takes current path.')

  args = parser.parse_args()
  root_path = args.path

  if root_path is None:
    root_path = os.getcwd()

  document = {}
  scan_dir(root_path, document, list())
  print json.dumps(document)

def scan_dir(root_path, document, dir_path):

  elements = os.listdir(root_path)
  elements.sort()

  for element in elements:

    absolute_element = os.path.join(root_path, element)
    relative_path = [fragment for fragment in dir_path]
    relative_path.append(element)

    if os.path.isdir(absolute_element):
      scan_dir(absolute_element, document, relative_path)

    elif os.path.isfile(absolute_element):
      attribute_name = remove_extension('_'.join(relative_path))
      document[attribute_name] = open(absolute_element, 'r').read()

def remove_extension(file_name):
  return  '.'.join(file_name.split('.')[:-1])

if __name__ == "__main__":
  main()
