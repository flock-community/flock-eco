build:
	docker build -t eco-backend .
.PHONY: build

dev:
	docker run -p 8080:8080 -it --rm -v $(shell pwd):/app -v ~/.m2:/root/.m2 eco-backend bash
.PHONY: dev
