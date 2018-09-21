package controller;

import dto.BaseResponse;
import dto.CheckInOutDataDto;
import model.CheckInOutData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.CheckInOutService;

import java.awt.*;
import java.util.List;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

@RestController
@CrossOrigin(origins={"http://localhost:8100","http://localhost:8080"})
public class CheckInController {

    @Autowired
    private CheckInOutService chSvc;

    @RequestMapping(value = "/checkIn/{id}", method = RequestMethod.POST)
    public BaseResponse checkIn(@PathVariable("id") String userName) {

        BaseResponse response = new BaseResponse();
        try {
            chSvc.checkIn(userName, 100, 100);
            response.setStatus("success");
            response.setDetail("success");
        }catch(Exception e){
            response.setStatus("error");
            response.setDetail(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/checkOut/{id}", method = RequestMethod.POST)
    public BaseResponse checkOut(@PathVariable("id") String userName) {

        BaseResponse response = new BaseResponse();
        try {
            chSvc.checkOut(userName, 100, 100);
            response.setStatus("success");
            response.setDetail("success");
        }catch(Exception e){
            response.setStatus("error");
            response.setDetail(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<CheckInOutDataDto>> getData(@PathVariable("id") String userName) {

        List<CheckInOutDataDto> data = chSvc.getData(userName);
        ResponseEntity<List<CheckInOutDataDto>> outData = new ResponseEntity<List<CheckInOutDataDto>>(data,HttpStatus.OK);
        return outData;
    }

}
