# suffixing-app

The purpose of application is renaming files, adding a suffix to their names.

The application takes a one obligatory argument - the path to a config file.
Config file format is properties (java.util.Properties class) and contains three fields:
1. mode - renaming mode. can take following values: copy - keep the source file,
move - remove the source file.
2. suffix - the suffix, that mut be added to the file name (before file extension).
3. files - a list of files for suffixing. File paths are separated with column:.
