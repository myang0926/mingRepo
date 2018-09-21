package service;

import dto.CheckInOutDataDto;
import model.CheckInOutData;
import model.User;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CheckInOutDataRepository;
import repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckInOutServiceImpl implements CheckInOutService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CheckInOutDataRepository chRepo;

    public void checkIn(String userName, long lat, long lng){

        User user = userRepo.findByUserName(userName);

        CheckInOutData data = new CheckInOutData();
        data.setUser(user);
        data.setCheckInOut(DateTime.now().toDate());
        data.setCreatedOn(DateTime.now().toDate());
        data.setLongitude(lng);
        data.setLatitude(lat);
        data.setType("IN");
        chRepo.save(data);
    }

    public void checkOut(String userName, long lat, long lng){

        User user = userRepo.findByUserName(userName);

        CheckInOutData data = new CheckInOutData();
        data.setUser(user);
        data.setCheckInOut(DateTime.now().toDate());
        data.setCreatedOn(DateTime.now().toDate());
        data.setLongitude(lng);
        data.setLatitude(lat);
        data.setType("OUT");
        chRepo.save(data);
    }


    public List<CheckInOutDataDto> getData(String userName){
        User user = userRepo.findByUserName(userName);
        List<CheckInOutDataDto> data = new ArrayList<CheckInOutDataDto>();
        if(user != null){
           data.add(getDto(user,"IN"));
           data.add(getDto(user, "OUT"));
        }

        return data;
    }

    private CheckInOutDataDto getDto(User user, String type){
        DateTime now = DateTime.now();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String d = f.format(now.toDate());
        CheckInOutDataDto dto = new CheckInOutDataDto();
        List<CheckInOutData> results = chRepo.findByUserIdAndTypeAndCreatedOnTime(user.getId().toString(), type, d);
        if(results.size() > 0){
            CheckInOutData data = results.get(0);
            dto.setCheckInOut(data.getCheckInOut());
            dto.setLatitude(data.getLatitude());
            dto.setLongitude(data.getLongitude());
        }

        return dto;
    }
}
