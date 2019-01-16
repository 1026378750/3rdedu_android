package com.ldf.calendar.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ldf on 17/7/5.
 */

public class DayUtil implements Parcelable {
    //状态
    private DateState state;
    //日期
    private CalendarDate date;
    //行数
    private int posRow;
    //列数
    private int posCol;

    public DayUtil(DateState state , CalendarDate date , int posRow , int posCol) {
        this.state = state;
        this.date = date;
        this.posRow = posRow;
        this.posCol = posCol;
    }

    public DateState getDateState() {
        return state;
    }

    public void setDateState(DateState state) {
        this.state = state;
    }

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }

    public int getPosRow() {
        return posRow;
    }

    public void setPosRow(int posRow) {
        this.posRow = posRow;
    }

    public int getPosCol() {
        return posCol;
    }

    public void setPosCol(int posCol) {
        this.posCol = posCol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeSerializable(this.date);
        dest.writeInt(this.posRow);
        dest.writeInt(this.posCol);
    }

    protected DayUtil(Parcel in) {
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : DateState.values()[tmpState];
        this.date = (CalendarDate) in.readSerializable();
        this.posRow = in.readInt();
        this.posCol = in.readInt();
    }

    public static final Parcelable.Creator<DayUtil> CREATOR = new Parcelable.Creator<DayUtil>() {
        @Override
        public DayUtil createFromParcel(Parcel source) {
            return new DayUtil(source);
        }

        @Override
        public DayUtil[] newArray(int size) {
            return new DayUtil[size];
        }
    };

    public boolean isTagMark(List<String> loadMarkData){
        return loadMarkData!=null
                && !loadMarkData.isEmpty()
                && loadMarkData.contains(getDate().toString());
    }

}
