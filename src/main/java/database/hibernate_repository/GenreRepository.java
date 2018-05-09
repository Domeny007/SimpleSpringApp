package database.hibernate_repository;

import database.entity.Country;
import database.entity.Genre;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends CrudRepository<Genre,Integer> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM " +
                    "spring_database.public.getselectgenrebyname(:genreName)")
    List<Genre> findByName(@Param("genreName") String genreName);

}
