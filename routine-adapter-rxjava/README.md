
Routine RxJava Adapter
==================

Routine ships with a default adapter for `router`. This child module contained herein is additional adapter for RxJava.


Usage
====
Supply an instance of rxjava adapter when building your Routine instance:
```java
Routine routine = new Routine.Builder()
  .addAdapter(RxJavaAdapterFactory.create())
  .build();
```


Download
=======
	compile 'com.anbillon.routine:adapter-rxjava:1.1.0'