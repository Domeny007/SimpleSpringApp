package database.hibernate_repository;

import database.entity.UserAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Integer> {

    UserAuthority findByAuthority(String role_user);
}
