dev:
	lein watch
prod:
	chromium-browser --headless --dump-dom http://localhost:8280/ > index.html
	sed '1d'  -i index.html
	sed '13d' -i index.html
	cp resources/public/style.css .
	git add index.html style.css
	git commit -m "Update prod"
	git push origin main
