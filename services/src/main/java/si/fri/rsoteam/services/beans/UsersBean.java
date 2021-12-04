package si.fri.rsoteam.services.beans;

import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.lib.dtos.UserDto;
import si.fri.rsoteam.models.entities.UserEntity;
import si.fri.rsoteam.services.mappers.UserMapper;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class UsersBean {
    private Logger log = Logger.getLogger(UsersBean.class.getName());

    @PersistenceContext
    private EntityManager em;

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
     * @param id THe id of the wanted user.
     * @return Response object containing the requested user, or empty with the NOT_FOUND status.
     */
    public UserDto getUser(Integer id) {
        return em.find(UserDto.class, id);
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
            this.beginTx();
            em.remove(userEntity);
            this.commitTx();
        } else {
            throw new NotFoundException("User not found");
        }
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
