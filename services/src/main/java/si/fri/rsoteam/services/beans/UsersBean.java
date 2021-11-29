package si.fri.rsoteam.services.beans;

import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.lib.User;
import si.fri.rsoteam.models.entities.UserEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class UsersBean {
    private Logger log = Logger.getLogger(UsersBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Return all users
     */
    @Timed
    public List<UserEntity> getUsers() {
        TypedQuery<UserEntity> query = em.createNamedQuery("User.getAllUsers", UserEntity.class);
        return query.getResultList();
    }

    /**
     * <p> Queries the database and returns a specific user based on given id. </p>
     *
     * @param id THe id of the wanted user.
     * @return Response object containing the requested user, or empty with the NOT_FOUND status.
     */
    public UserEntity getUser(Integer id) {
        return em.find(UserEntity.class, id);
    }

    /**
     * <p> Insert the provided book into the database.</p>
     *
     * @param user The user object that will be created.
     * @return Response object containing created user object.
     */
    public User createUser(User user) {
        this.beginTx();
        em.persist(user);
        this.commitTx();
        return user;
    }

    /**
     * <p> Update user with given id. </p>
     * @param id Id of object we want to update.
     * @param user User with new properties.
     * @return Response object containing updated user object.
     * */
    public UserEntity updateUser(User user, Integer id){
        this.beginTx();
        UserEntity oldUserEntity = em.find(UserEntity.class, id);
        oldUserEntity.setName(user.getName());
        oldUserEntity.setSurname(user.getSurname());
        oldUserEntity.setBirthDay(user.getBirthDay());
        oldUserEntity.setEmail(user.getEmail());
        em.persist(oldUserEntity);
        this.commitTx();
        return oldUserEntity;
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
