
Routine Support Resolver
==================

Routine ships with a default resolver for caller. This child module contained herein is additional resovler for android support `Fragment`.


Usage
====
Supply an instance of support resolver when building your Routine instance:
```java
Routine routine = new Routine.Builder()
  .addResolverFactory(SupportFragmentResolverFactory.create())
  .build();
```


Download
=======
	compile 'com.anbillon.routine:support-resolver:1.0.1.2-SNAPSHOT'