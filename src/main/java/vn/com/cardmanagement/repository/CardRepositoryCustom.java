package vn.com.cardmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.domain.CreateDeviceRequest;
import vn.com.cardmanagement.domain.CreateDeviceResponse;
import vn.com.cardmanagement.domain.FindElementResponse;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepositoryCustom {
    ResponseEntity<CreateDeviceResponse> createDevice (CreateDeviceRequest createDeviceRequest);

    ResponseEntity<FindElementResponse> findElement(String sessionId, String elementIdInApp);

    ResponseEntity<FindElementResponse> findElement(String sessionId, String elementIdInApp, int maxRetryTimes);

    void click(String sessionId, String elementId);

    String getContentElement(String sessionId, String elementId);

    void sendKeys(String sessionId, String elementId, String keys);

    List<Card> findNewCard(CardQueryCondition cardQueryCondition);

    List<Card> findNewLessThanCard(CardQueryCondition cardQueryCondition);

    Page<Card> findOldCard(Pageable pageable, CardQueryCondition cardQueryCondition);

    Page<Card> findExpiredCard(Pageable pageable, String username);

    Card findOne(String id);

    List<Card> findOldCard(CardQueryCondition cardQueryCondition);

    List<String> getAllManagedPendingUsers();
}
