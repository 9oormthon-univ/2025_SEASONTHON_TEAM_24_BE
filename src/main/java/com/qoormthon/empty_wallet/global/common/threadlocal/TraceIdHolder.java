package com.qoormthon.empty_wallet.global.common.threadlocal;

/**
 * 스레드마다 고유한 요청 ID를 저장하고 꺼내는 역할을 합니다(로깅을 위함)
 */
public class TraceIdHolder {

  private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

  public static void set(String traceId) {
    threadLocal.set(traceId);
  }

  public static String get() {
    return threadLocal.get();
  }

  public static void clear() {
    threadLocal.remove();
  }
}
