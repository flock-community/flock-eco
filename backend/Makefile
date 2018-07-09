build:
	docker build -t eco-backend .
.PHONY: build

dev:
	docker run -p 8080:8080 -it --rm -v $(shell pwd):/app -v ~/.m2:/root/.m2 eco-backend bash
.PHONY: dev

deploy:
	docker build -f Dockerfile-run -t eco-backend-deploy .
.PHONY: deploy

run:
	docker run -p 8080:8080 -it --rm eco-backend-deploy bash
.PHONY: run
