#!/bin/bash

if [ $# -gt 1 ]; then
  1>&2 echo "zero or one argument expected"
  exit 1
fi

input_filename="${1-.env}"
output_filename=".env.example"

if [ -f "$output_filename" ]; then
  1>&2 echo "$output_filename already exists! remove it and try again, script aborted"
  exit 1
fi

touch "$output_filename"

echo reading "$input_filename"

while read -r line; do
  if [[ "$line" =~ ^.*SECRET.*= ]]; then
    echo -n "secret detected and removed, "
    parsed_line="${BASH_REMATCH[0]}"
  else
    parsed_line="$line"
  fi
  echo "copying as $parsed_line"
  echo "$parsed_line" >> "$output_filename"
done < "$input_filename"

echo "script finished successfully"
