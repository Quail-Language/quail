use "lang/time" = time

t = time.DateTime(millis())

t.timezone = "GMT+5:00"
print(t.getDay())
print(t.getMonth())
print(t.getYear())
print(t.getHour())
print(t.getMinute())
print(t.getSecond())
print(t.getMillisecond())
print(t.format("dd-MM-yyyy HH:mm:ss.SSS"))
print(millis())