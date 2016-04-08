package com.eventspipe;

import org.joda.time.LocalDateTime;

public interface DateAndTime {

  LocalDateTime at(Double aLatitude, Double aLongitude);
}
