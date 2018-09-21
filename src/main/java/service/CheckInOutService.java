package service;

import dto.CheckInOutDataDto;
import model.CheckInOutData;

import java.util.List;

public interface CheckInOutService {
    public void checkIn(String userName, long lat, long lng);
    public void checkOut(String userName, long lat, long lng);
    public List<CheckInOutDataDto> getData(String userName);
}
