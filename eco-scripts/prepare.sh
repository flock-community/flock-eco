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
  if ! [[ "$1" != "" ]]; then
    read -p 'Version: ' version
  else
    local version=$1
  fi
  echo ${version}
}

set_prerelease() {
  branch_name=$(git symbolic-ref -q HEAD)
  if ! [[ "$branch_name" != "refs/heads/master" ]]; then
    local prerelease=""
  else
    local prerelease=$(git rev-parse --short HEAD)
  fi
  echo ${prerelease}
}

set_mvn_version() {
  if ! [[ "$2" == "" ]]; then
    echo "$1-$2"
  else
    echo "$1"
  fi

}

set_npm_version() {
  if ! [[ "$2" == "" ]]; then
    echo "$1-next.$2"
  else
    echo "$1"
  fi

}

check_changes
version=$(set_version $@)
prerelease=$(set_prerelease $@)

mvn_version=$(set_mvn_version $version $prerelease)
npm_version=$(set_npm_version $version $prerelease)

echo "---"
echo "Version: ${version}"
echo "Prerelease: ${prerelease}"

echo "Mvn version: ${mvn_version}"
echo "Npm version: ${npm_version}"

echo "Tag name: $(tag_name ${mvn_version})"

mvn versions:set -DnewVersion=${mvn_version} -DgenerateBackupPoms=false
npx lerna version --yes --no-git-tag-version ${npm_version}

find . -name 'pom.xml' | xargs git add
find . -name 'package.json' | xargs git add
find . -name 'package-lock.json' | xargs git add
find . -name 'lerna.json' | xargs git add

git commit -m "$(commit_message ${mvn_version})"
git tag "$(tag_name ${mvn_version})"

git push origin
git push origin --tags