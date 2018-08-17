build:
	docker build -t eco-frontend .
.PHONY: build

dev:
	docker run -p 8080:8080 -p 3000:3000 -it --rm -v $(shell pwd):/app eco-frontend bash
.PHONY: dev

fe-build:
    npm --prefix eco-webpack i
.PHONY: fe-build

fe-dev:
    npm --prefix eco-webpack run start
.PHONY: fe-dev
