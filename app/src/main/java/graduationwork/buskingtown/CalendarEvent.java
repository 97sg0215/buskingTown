package graduationwork.buskingtown;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

import java.util.Calendar;

public class CalendarEvent extends BaseCalendarEvent {

    private int mDrawableId;

    // region Constructors

    public CalendarEvent(long id, int color, String title, String description, String location, long dateStart, long dateEnd, int allDay, String duration, int drawableId) {
        super(id, color, title, description, location, dateStart, dateEnd, allDay, duration);
        this.mDrawableId = drawableId;
    }

    public CalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay, int drawableId) {
        super(title, description, location, color, startTime, endTime, allDay);
        this.mDrawableId = drawableId;
    }

    public CalendarEvent(CalendarEvent calendarEvent) {
        super(calendarEvent);
        this.mDrawableId = calendarEvent.getDrawableId();
    }

    // endregion

    // region Public methods

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int drawableId) {
        this.mDrawableId = drawableId;
    }

    // endregion

    // region Class - BaseCalendarEvent

    @Override
    public CalendarEvent copy() {
        return new CalendarEvent(this);
    }

}
