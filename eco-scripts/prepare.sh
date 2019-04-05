git_reset () {
  git reset --hard
}

tag_name () {
  echo "v$1"
}

commit_message () {
  echo "release version: $1"
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
version=$(set_version $@)

echo "Set version: ${version}"
echo "Tag name: $(tag_name ${version})"

npm run lerna -- version --yes --no-git-tag-version ${version}
mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false

find . -name 'pom.xml' | xargs git add
find . -name 'package.json' | xargs git add

git commit -m "$(commit_message ${version})"
git tag "$(tag_name ${version})"

git push origin --tags