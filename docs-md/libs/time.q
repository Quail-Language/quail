#?toc-entry DateTime
#?toc-entry parseDateTime

#?html <h2><code>lang/time</code></h2>
#?html <hr>
#? This library is the bare bones for date and time manipulation

class DateTime {
    #? Represents date and/or time
    #? <code>timestamp</code> contains UNIX timestamp in milliseconds
    #? <code>timezone</code> contains timezone offset like this "GMT+03:00"
    #? Supports +, -, <, >, <=, =>, == and != operators and also num() conversion
    #? Notice! There is no <code>now</code> method or function, so use DateTime(millis())
    num timestamp = 0
    string timezone = "UTC"

    constructor (this, timestamp) {
        this.timestamp = timestamp
    }

    num getDay(this) {
        #? Gets day of month (1-31) part
    }

    num getDayOfWeek(this) {
        #? Gets day of week (0-6, starting from Monday) part
    }

    num getMonth(this) {
        #? Get month of year (1-12) part
    }

    num getYear(this) {
        #? Get year number part
    }

    num getHour(this) {
        #? Get hour in 24-hour format (0-23) part
    }

    num getMinute(this) {
        #? Get minute (0-59) part
    }

    num getSecond(this) {
        #? Get second (0-59) part
    }

    num getMillisecond(this) {
        #? Get millisecond (0-999) part
    }

    string format(this, string template) {
        #? Formats date according to given template
        #? Letters for using in template: (is the same as Java's SimpleDateFormat)
        #? G - Era designator
        #? yy - Year (like 18)
        #? yyyy - Year (like 2018)
        #? MMMM - Month (like December)
        #? MMM - Month (like Dec)
        #? MM - Month (like 12)
        #? w - Results in week in year
        #? W - Results in week in month
        #? D - Gives the day count in the year
        #? d - Day of the month	(like 9)
        #? dd - Day of the month (like 09)
        #? F - Day of the week in month
        #? E - Day name in the week
        #? u - Day number of week
        #? a - AM or PM marker
        #? H - Hour in the day (0-23)
        #? k - Hour in the day (1-24)
        #? K - Hour in am/pm for 12 hour format (0-11)
        #? h - Hour in am/pm for 12 hour format (1-12)
        #? m - Minute in the hour
        #? s - Second in the minute
        #? S - Millisecond in the minute
        #? z - Timezone
        #? Z - Timezone offset in hours (RFC pattern)
        #? X - Timezone offset in ISO format
    }

}

object<DateTime> parseDateTime(string template, string value) {
    #? Parse date AND time from a string value. Template is same as in DateTime::format
    #?see #DateTime::format
}
