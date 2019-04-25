package vn.com.cardmanagement.repository;

import vn.com.cardmanagement.domain.Card;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends MongoRepository<Card, String> {

}
