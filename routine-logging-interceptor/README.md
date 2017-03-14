
Routine Logging Interceptor
==================

A Routine interceptorA Routine interceptor which logs router call information.


Usage
====
```java
Routine routine = new Routine.Builder()
  .addInterceptor(new RoutineLoggingInterceptor())
  .build();
```

You can change the log level at any time by calling `setLevel`.
To log to a custom location, pass a Logger instance to the constructor.
```java
RoutineLoggingInterceptor logging = new RoutineLoggingInterceptor(new Logger() {
  @Override public void log(String message) {
    Timber.tag("Routine").d(message);
  }
});
```


Download
=======
	compile 'com.anbillon.routine:logging-interceptor:1.1.0'