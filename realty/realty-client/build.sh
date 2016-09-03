npm install
rm -r dist* > /dev/null 2>&1
mkdir dist
./webpack.minimize.sh \
	&& cp -r index.html dist/ \
	&& cp -r build dist/ \
	&& cp -r css dist/ \
	&& cp -r external_js dist/ \
	&& cp -r fonts dist/ \
	&& cp -r images dist/ \
	&& cd dist && tar -cvzf app.tar.gz *
