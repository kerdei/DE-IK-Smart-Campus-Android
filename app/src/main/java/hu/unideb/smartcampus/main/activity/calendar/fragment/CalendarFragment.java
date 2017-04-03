package hu.unideb.smartcampus.main.activity.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import hu.unideb.smartcampus.R;
import hu.unideb.smartcampus.activity.EventDetailsActivity;
import hu.unideb.smartcampus.activity.NewEventActivity;
import hu.unideb.smartcampus.fragment.interfaces.OnBackPressedListener;
import hu.unideb.smartcampus.main.activity.calendar.adapter.TimetableEventListAdapter;
import hu.unideb.smartcampus.main.activity.calendar.sqllite.db.TimetableEvent;
import hu.unideb.smartcampus.main.activity.calendar.sqllite.db.TimetableEventSQLiteHelper;

public class CalendarFragment extends Fragment implements OnBackPressedListener {

    private CalendarView myCalendar;
    private FloatingActionButton newEventFab;
    private ListView listView;
    private Calendar selectedDate;
    private TextView emptyText;

    public CalendarFragment() {
    }

    public TimetableEventSQLiteHelper dbb() {
        TimetableEventSQLiteHelper db = new TimetableEventSQLiteHelper(this.getContext());
        db.onUpgrade(db.getWritableDatabase(), 1, 2);
        db.createEvent(new TimetableEvent(Long.parseLong("1491084000000"), "1", "1", "1", 1487674800000L, 1487682000000L));
        db.createEvent(new TimetableEvent(Long.parseLong("1491084000000"), "2", "2", "2", 1487674800000L, 1487682000000L));
        db.createEvent(new TimetableEvent(Long.parseLong("1491084000000"), "3", "3", "3", 1487674800000L, 1487682000000L));
        db.createEvent(new TimetableEvent(Long.parseLong("1491084000000"), "4", "Dr. Papp Zoltán Lajos", "IK-INK.HÁZ 2.28", 1487674800000L, 1487682000000L));

        return db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        listView = (ListView) view.findViewById(R.id.calendar_event);

        emptyText = (TextView) view.findViewById(android.R.id.empty);

        selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimetableEvent e = (TimetableEvent) listView.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                intent.putExtra("eventName", e.getTimetableEventName());
                intent.putExtra("eventDescription", e.getTimetableEventDescription());
                intent.putExtra("eventPlace", e.getTimetableEventPlace());
                intent.putExtra("eventDate", e.getTimetableEventDate());
                intent.putExtra("eventStartTime", e.getTimetableEventStartTime());
                intent.putExtra("eventEndTime", e.getTimetableEventEndTime());
                startActivity(intent);
            }
        });

        CalendarInitialize(view);

        List<TimetableEvent> l = dbb().getDateEvents(selectedDate.getTimeInMillis());


        if (l.size() > 0) {
            TimetableEventListAdapter adap = new TimetableEventListAdapter(getContext(), l);
            listView.setAdapter(adap);
        } else if (l.size() == 0) {
            listView.setAdapter(null);
            emptyText.setText(getResources().getText(R.string.noEventThisDay));
            listView.setEmptyView(emptyText);
        }
        return view;
    }

    public void CalendarInitialize(View v) {
        newEventFab = (FloatingActionButton) v.findViewById(R.id.new_event_add);
        myCalendar = (CalendarView) v.findViewById(R.id.calendar);
        myCalendar.setFirstDayOfWeek(2);
        myCalendar.setShowWeekNumber(false);

        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);

                List<TimetableEvent> l = dbb().getDateEvents(selectedDate.getTimeInMillis());

                if (l.size() > 0) {
                    TimetableEventListAdapter adap = new TimetableEventListAdapter(getContext(), l);
                    listView.setAdapter(adap);
                } else if (l.size() == 0) {
                    listView.setAdapter(null);
                    emptyText.setText(getResources().getText(R.string.noEventThisDay));
                    listView.setEmptyView(emptyText);
                }
            }
        });

        newEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewEventActivity.class);
                intent.putExtra("selectedDate", selectedDate.getTimeInMillis());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}