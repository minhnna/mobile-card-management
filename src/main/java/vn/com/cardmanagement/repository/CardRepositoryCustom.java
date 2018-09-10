package vn.com.cardmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepositoryCustom {
    List<Card> findNewCard(CardQueryCondition cardQueryCondition);

    Page<Card> findOldCard(Pageable pageable, CardQueryCondition cardQueryCondition);

    Page<Card> findExpiredCard(Pageable pageable);

    Card findOne(String id);
}
