
Routine RxJava2 Adapter
==================

Routine ships with a default adapter for `router`. This child module contained herein is additional adapter for RxJava2.


Usage
====
Supply an instance of rxjava2 adapter when building your Routine instance:
```java
Routine routine = new Routine.Builder()
  .addAdapter(RxJava2AdapterFactory.create())
  .build();
```


Download
=======
	compile 'com.anbillon.routine:adapter-rxjava2:1.1.0'