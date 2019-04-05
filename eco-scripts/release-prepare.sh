if ! git diff-index --quiet HEAD --; then
    echo "Repo has open changes"; exit;
fi

if ! [ "$1" != "" ]; then
    echo "No version set"; exit;
fi

echo "Version: $1"