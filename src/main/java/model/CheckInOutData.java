package model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="check_in_out_data",schema = "inout")
@DynamicUpdate
public class CheckInOutData {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Type(type="uuid-char")
    @Column(name="id",unique = true,nullable = false,length=36)
    private UUID id;


    @ManyToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInOut;

    private long longitude;

    private long latitude;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    private String type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCheckInOut() {
        return checkInOut;
    }

    public void setCheckInOut(Date checkInOut) {
        this.checkInOut = checkInOut;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
