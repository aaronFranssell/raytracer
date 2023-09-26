package util.stopwatch;

import java.util.GregorianCalendar;

public class GregorianCalendarFactoryImpl implements GregorianCalendarFactory {
  @Override
  public GregorianCalendar getCalendar() {
    return new GregorianCalendar();
  }
}
