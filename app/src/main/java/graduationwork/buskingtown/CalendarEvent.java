package graduationwork.buskingtown;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

public class CalendarEvent extends BaseCalendarEvent {

    private int mDrawableId;

    public CalendarEvent(CalendarEvent calendarEvent) {
        super(calendarEvent);
        this.mDrawableId = calendarEvent.getDrawableId();
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    @Override
    public CalendarEvent copy() {
        return new CalendarEvent(this);
    }

}
