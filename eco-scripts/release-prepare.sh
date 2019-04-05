git_reset () {
  git reset --hard
}

if ! git diff-index --quiet HEAD --; then
    echo "Uncommitted changes found";
    exit 0;
fi

if ! [ "$1" != "" ]; then
    read -p 'Version: ' version
else
    version = $1
fi

echo "Version: $1"
npx lerna version $1
mvn release