package vn.com.cardmanagement.repository;

import vn.com.cardmanagement.domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends MongoRepository<Card, String>, CardRepositoryCustom {

}
