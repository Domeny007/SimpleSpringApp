package database.hibernate_repository;

import database.entity.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country,Integer> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM " +
                    "spring_database.public.getselectcountrybyname(:countryName)")
    List<Country> findByName(@Param("countryName") String countryName);

}
