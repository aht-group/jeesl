FROM debian:bullseye

RUN apt-get update && apt-get install -y wget maven git tmux procps build-essential libz-dev

RUN cd /usr/lib/jvm \
	&& wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.0/graalvm-ce-java11-linux-aarch64-22.3.0.tar.gz \
	&& tar -xvzf graalvm-ce-java11-linux-aarch64-22.3.0.tar.gz \
	&& rm graalvm-ce-java11-linux-aarch64-22.3.0.tar.gz \
	&& update-alternatives --install /usr/bin/java java /usr/lib/jvm/graalvm-ce-java11-22.3.0/bin/java 1 \
	&& cd /usr/lib/jvm \
	&& wget https://cdn.azul.com/zulu-embedded/bin/zulu8.66.0.15-ca-jdk8.0.352-linux_arm64.deb \
	&& apt install -y /usr/lib/jvm/zulu8.66.0.15-ca-jdk8.0.352-linux_arm64.deb \
	&& rm /usr/lib/jvm/zulu8.66.0.15-ca-jdk8.0.352-linux_arm64.deb
	
#ENV PATH="${PATH}:/usr/lib/jvm/graalvm-ce-java11-22.3.0/bin"
#ENV JAVA_HOME="/usr/lib/jvm/graalvm-ce-java11-22.3.0"
#RUN update-alternatives --set java /usr/lib/jvm/graalvm-ce-java11-22.3.0/bin/java

# RUN echo gu install native-image

RUN update-alternatives --set java /usr/lib/jvm/zulu-8-arm64/jre/bin/java
ENV JAVA_HOME="/usr/lib/jvm/zulu-8-arm64"

RUN cd /root \
	&& mkdir native \
	&& git clone https://github.com/aht-group/jeesl.git && cd jeesl && mvn clean install -DskipTests && cd /root
	
RUN update-alternatives --set java /usr/lib/jvm/graalvm-ce-java11-22.3.0/bin/java
ENV JAVA_HOME="/usr/lib/jvm/graalvm-ce-java11-22.3.0"

RUN cd /root \
	&& cd jeesl/client && mvn -Pnative -DskipTests package && cd /root
