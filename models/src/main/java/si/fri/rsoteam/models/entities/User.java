package si.fri.rsoteam.models.entities;
import si.fri.rsoteam.models.converters.EventsAtributeConverter;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "user")
@NamedQuery(name = "User.getAllUsers", query = "SELECT u from user u")
public class User implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String surname;

    private Instant birthDay;

    private String email;

    @Convert(converter = EventsAtributeConverter.class)
    @ElementCollection
    private List<Integer> events;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getEvents() {
        return events;
    }

    public void setEvents(List<Integer> events) {
        this.events = events;
    }

}
