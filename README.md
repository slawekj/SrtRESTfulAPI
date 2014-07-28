SrtRESTfulAPI
=============

###Getting Started

In order to compile and run this project you need Java, Git, Tomcat, and Maven installed. Jersey and Spring will be automatically resolved by Maven.

First: download, compile, and build the project:
```
$ cd /tmp/
$ git clone https://github.com/slawekj/SrtRESTfulAPI
$ cd /tmp/SrtRESTfulAPI/
$ mvn clean package
```

Great! Now you should have /tmp/SrtRESTfulAPI/target/srt.war file created and ready to be deployed on Tomcat. I assume you run Tomcat on localhost, port 8080. Open your favorite browser, go to http://localhost:8080/manager, go to “WAR file to deploy”, select /tmp/SrtRESTfulAPI/target/srt.war, deploy!
Now go to http://localhost:8080/srt. You should be able to see that “System is working”!

The API is organized as follows:
```
http://localhost:8080/srt/api/{s|l}?url=<url>
s – shorten given url
l – lookup given url
url – url to shorten or lookup
```

Now you can test the api, type in the browser:
```
http://localhost:8080/srt/api/s?url=http://test/a
http://localhost:8080/srt/api/s?url=http://test/b
http://localhost:8080/srt/api/s?url=http://test/c
http://localhost:8080/srt/api/s?url=http://test/d
```

You just created four mappings:
```
http://test/a <-> http://srt.com/D
http://test/b <-> http://srt.com/E
http://test/c <-> http://srt.com/F
http://test/d <-> http://srt.com/G
```

To reverse lookup the mapping type:
```
http://localhost:8080/srt/api/l?url=http://D
http://localhost:8080/srt/api/l?url=http://E
http://localhost:8080/srt/api/l?url=http://F
http://localhost:8080/srt/api/l?url=http://G
```
