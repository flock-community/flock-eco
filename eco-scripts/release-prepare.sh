git_reset () {
  git reset --hard
}

check_changes () {
  if ! git diff-index --quiet HEAD --; then
    echo "Uncommitted changes found";
    exit 0;
  fi
}

set_version() {
  echo "$1: $1"
  if ! [ "$1" != "" ]; then
    read -p 'Version: ' version
  else
    version=$1
    echo "Version: $version"
  fi
}

check_changes
set_version



# npm run lerna version $version
# mvn release