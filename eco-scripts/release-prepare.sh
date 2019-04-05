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
  if ! [ "$1" != "" ]; then
    read -p 'Version: ' version
  else
    local version=$1
  fi
  echo ${version}

}

check_changes
version $(set_version $@)

echo "Set version: $version"



# npm run lerna version $version
# mvn release