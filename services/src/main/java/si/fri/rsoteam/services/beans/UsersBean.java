package si.fri.rsoteam.services.beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.lib.dtos.UserDto;
import si.fri.rsoteam.models.entities.UserEntity;
import si.fri.rsoteam.services.config.RestConfig;
import si.fri.rsoteam.services.mappers.UserMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class UsersBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private RestConfig restConfig;

    @Inject
    @DiscoverService(value = "basketball-activity-tracking")
    private URL userServiceUrl;

    @Inject
    @DiscoverService(value = "basketball-stats")
    private URL statsServiceUrl;

    private Client httpClient;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    /**
     * Return all users
     */
    @Timed
    public List<UserDto> getUsers() {
        TypedQuery<UserEntity> query = em.createNamedQuery("User.getAllUsers", UserEntity.class);
        return query.getResultList().stream().map(UserMapper::entityToDto).collect(Collectors.toList());
    }

    /**
     * <p> Queries the database and returns a specific user based on given id. </p>
     *
     * @param id The id of the wanted user.
     * @return Response object containing the requested user, or empty with the NOT_FOUND status.
     */
    public UserDto getUser(Integer id) {
        UserEntity userEntity = em.find(UserEntity.class, id);
        return UserMapper.entityToDto(userEntity);
    }

    /**
     * <p> Insert the provided book into the database.</p>
     *
     * @param user The user object that will be created.
     * @return Response object containing created user object.
     */
    public UserDto createUser(UserDto user) {
        this.beginTx();
        UserEntity userEntity = UserMapper.dtoToEntity(user);
        em.persist(userEntity);
        this.commitTx();
        return UserMapper.entityToDto(userEntity);
    }

    /**
     * <p> Update user with given id. </p>
     * @param id Id of object we want to update.
     * @param user User with new properties.
     * @return Response object containing updated user object.
     * */
    public UserDto updateUser(UserDto user, Integer id){
        this.beginTx();

        UserEntity userEntity = em.find(UserEntity.class, id);
        userEntity.setName(user.name);
        userEntity.setSurname(user.surname);
        userEntity.setBirthDay(user.birthDay);
        userEntity.setEmail(user.email);
        em.persist(userEntity);

        this.commitTx();

        return UserMapper.entityToDto(userEntity);
    }

    /**
     * <p> Remove given object from database if it exists. </p>
     *
     * @return Response object with status gone if deletion was successful, else returns not found.
     */
    public void deleteUser(Integer id) {
        UserEntity userEntity = em.find(UserEntity.class, id);
        if (userEntity != null) {
            removeActivities(id);
            removeStats(id);
            this.beginTx();
            em.remove(userEntity);
            this.commitTx();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    private void removeActivities(Integer userId) {
        String host = String.format("%s://%s:%s/v1/activities/user/%d",
                userServiceUrl.getProtocol(),
                userServiceUrl.getHost(),
                userServiceUrl.getPort(),
                userId);
        httpClient
                .target(host)
                .request()
                .header("apiToken", restConfig.getApiToken())
                .delete();
    }

    private void removeStats(Integer userId) {
        String host = String.format("%s://%s:%s/v1/stats/user/%d",
                statsServiceUrl.getProtocol(),
                statsServiceUrl.getHost(),
                statsServiceUrl.getPort(),
                userId);
        httpClient
                .target(host)
                .request()
                .header("apiToken", restConfig.getApiToken())
                .delete();
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
