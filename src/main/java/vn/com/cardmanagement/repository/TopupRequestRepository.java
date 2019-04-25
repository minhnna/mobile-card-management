package vn.com.cardmanagement.repository;

import vn.com.cardmanagement.domain.TopupRequest;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the TopupRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopupRequestRepository extends MongoRepository<TopupRequest, String> {

}
