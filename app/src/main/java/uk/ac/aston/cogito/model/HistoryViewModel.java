package uk.ac.aston.cogito.model;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.aston.cogito.CogitoApp;
import uk.ac.aston.cogito.model.entities.DayRecord;
import uk.ac.aston.cogito.model.entities.Mood;

public class HistoryViewModel extends ViewModel {

    public static final DateFormat SESSION_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private final MutableLiveData<List<DayRecord>> history;
    private final DataManager dataManager;

    public HistoryViewModel() {
        super();

        history = new MutableLiveData<>(new ArrayList<>());
        dataManager = DataManager.getInstance(CogitoApp.getAppContext());

        updateHistory();
    }


    public LiveData<List<DayRecord>> getHistory() {
        return history;
    }


    public void recordTodaySession() {
        dataManager.recordSession(getTodayDate());
        updateHistory();
    }


    public void recordTodayMood(Mood mood) {
        dataManager.recordMood(getTodayDate(), mood);
        updateHistory();
    }

    public void updateMood(String date, Mood mood) {
        dataManager.recordMood(date, mood);
        updateHistory();
    }


    public boolean isDailyCheckInDone() {
        String today = getTodayDate();
        DayRecord dayRecord = dataManager.findDayRecordByDate(today);

        if (dayRecord != null) {
            return dayRecord.getMood() != null;
        }
        return false;
    }


    private String getTodayDate() {
        Date date = Calendar.getInstance().getTime();
        return SESSION_DATE_FORMAT.format(date);
    }


    private void updateHistory() {
        history.setValue(dataManager.getUpToDateHistory());
    }
}
