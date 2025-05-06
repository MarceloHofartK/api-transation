FROM ubuntu:latest
LABEL authors="Marcelo Hofart"

ENTRYPOINT ["top", "-b"]