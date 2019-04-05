if ! git diff-index --quiet HEAD --; then
    echo "Repo has open changes";
    exit;
fi

echo "Repo has no open changes"