package util.stopwatch;

import java.util.GregorianCalendar;

public class StopWatchImpl implements StopWatch {
  private final String START_STOP_NOT_CALLED = "Start and stop were not called on the stopwatch.";
  private final String START_NOT_CALLED = "Start was not called on the stopwatch.";
  private final String STOP_NOT_CALLED = "Stop was not called on the stopwatch.";
  private final String STOPPED_BEFORE_STARTED = "The stopwatch was stopped before it was started.";

  GregorianCalendar start;
  GregorianCalendar end;
  GregorianCalendarFactory factory;

  public StopWatchImpl(GregorianCalendarFactory incomingFactory) {
    factory = incomingFactory;
  }

  public StopWatchImpl() {
    factory = new GregorianCalendarFactoryImpl();
  }

  @Override
  public void start() {
    start = factory.getCalendar();
  }

  @Override
  public void stop() {
    end = factory.getCalendar();
  }

  @Override
  public String getDifference() {
    if (start == null && end == null) {
      return START_STOP_NOT_CALLED;
    }
    if (start == null) {
      return START_NOT_CALLED;
    }
    if (end == null) {
      return STOP_NOT_CALLED;
    }
    if (start.getTimeInMillis() > end.getTimeInMillis()) {
      return STOPPED_BEFORE_STARTED;
    }
    return calcDifference();
  }

  private String calcDifference() {
    long span = end.getTimeInMillis() - start.getTimeInMillis();
    long mins = 1000 * 60;
    long numMins = span / mins;
    span -= mins * numMins;

    long secs = 1000;
    long numSecs = span / secs;

    String ret = "Executed in " + numMins + " minute(s) and " + numSecs + " seconds.";

    return ret;
  }

  public GregorianCalendar getStart() {
    return start;
  }

  public GregorianCalendar getEnd() {
    return end;
  }
}
