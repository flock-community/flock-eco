build:
	docker build -t eco-frontend .
.PHONY: build

dev:
	docker run -p 8080:8080 -it --rm -v $(shell pwd):/app eco-frontend bash
.PHONY: dev
