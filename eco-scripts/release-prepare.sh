if ! git diff-index --quiet HEAD --; then
    echo "Uncommitted changes found"; exit 0;
fi

if ! [ "$1" != "" ]; then
    echo "No version set"; exit 0;
fi

echo "Version: $1"
npx lerna version $1
mvn release