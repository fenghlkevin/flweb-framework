//package com.kevin.iesutdio.kfgis.app.framework.util;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public  class NamedThreadFactory implements ThreadFactory {
//		private final String prefix;
//		private final ThreadFactory threadFactory;
//		private final AtomicInteger atomicInteger = new AtomicInteger();
//
//		public NamedThreadFactory(final String prefix) {
//			this(prefix, Executors.defaultThreadFactory());
//		}
//
//		public NamedThreadFactory(final String prefix, final ThreadFactory threadFactory) {
//			this.prefix = prefix;
//			this.threadFactory = threadFactory;
//		}
//
//		@Override
//		public Thread newThread(Runnable r) {
//			Thread t = this.threadFactory.newThread(r);
//			t.setName(this.prefix + this.atomicInteger.incrementAndGet());
//			return t;
//		}
//	}