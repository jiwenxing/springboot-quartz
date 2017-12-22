
![](http://7xry05.com1.z0.glb.clouddn.com/201712221131_63.png)

[![build](https://img.shields.io/badge/build-passing-green.svg)]()
[![dependencies](https://img.shields.io/badge/springboot-v1.4.5-blue.svg)]()
[![dependencies](https://img.shields.io/badge/quartz-v2.2.1-blue.svg)]()
[![license](https://img.shields.io/badge/license-MIT-yellowgreen.svg)]()

---

quartz + springboot + RabbitMQ + H2(in-memory database) + dynamic task management web console.

## features

- dynamic task management web console provided
- zero xml configuration with springboot
- using in-memory database H2
- using RabbitMQ to asynchronously notify the job execution

## preview

*visit http://localhost:8080/index after starting application*


![](http://ochyazsr6.bkt.clouddn.com/206751c9ac95c7860f087a02e5f2fd9f.jpg)



## notice

RabbitMQ is not in use by default. steps blow should be followed to test it.  

1. install rabbitmq-server and start it
2. update the rabbitmq configuration in `application.properties`
3. uncomment code in file `Receiver.java` & `QuartzJobFactory.java` & `RabbitConfig.java`

## references

- [https://github.com/davidkiss/spring-boot-quartz-demo](https://github.com/davidkiss/spring-boot-quartz-demo)
- [https://gist.github.com/jelies/5085593](https://gist.github.com/jelies/5085593)
