if git diff-index --quiet HEAD --; then
    echo "N"
else
    echo "Y"
fi