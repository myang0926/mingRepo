package dto;

import java.util.Date;

public class CheckInOutDataDto {
    private Date checkInOut;

    private long longitude;

    private long latitude;

    public Date getCheckInOut() {
        return checkInOut;
    }

    public void setCheckInOut(Date checkInOut) {
        this.checkInOut = checkInOut;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
