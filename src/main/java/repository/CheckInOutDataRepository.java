package repository;

import model.CheckInOutData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CheckInOutDataRepository extends JpaRepository<CheckInOutData,UUID> {

  @Query(nativeQuery=true, value ="select * FROM  inout.check_in_out_data a where a.user_id= ?1 and a.type=?2 and date(a.createdOn)=?3 " )
  public List<CheckInOutData> findByUserIdAndTypeAndCreatedOnTime(String uid, String t, String d);

}
