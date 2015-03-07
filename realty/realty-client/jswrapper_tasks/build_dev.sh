#command 'node -v' should work

CURR_DIR=$(dirname $0)

NODE=$($CURR_DIR/which_node.sh)

echo "Using node: ${NODE}"

export PATH="${NODE}/bin:${PATH}"

$CURR_DIR/../node_modules/gulp/bin/gulp.js build
$CURR_DIR/../node_modules/gulp/bin/gulp.js compressDev
