FROM adzerk/boot-clj:latest

ENV BOOT_VERSION=2.3.0
ADD . /code
WORKDIR /code
# RUN /usr/bin/boot foo

ENTRYPOINT ["/usr/bin/boot", "foo"]
