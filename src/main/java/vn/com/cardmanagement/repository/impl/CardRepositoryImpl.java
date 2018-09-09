package vn.com.cardmanagement.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import vn.com.cardmanagement.config.Constants;
import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.repository.CardRepositoryCustom;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;

import java.time.Instant;
import java.util.List;

@Repository
public class CardRepositoryImpl implements CardRepositoryCustom {
    MongoTemplate mongoTemplate;
    private final Logger log = LoggerFactory.getLogger(CardRepositoryImpl.class);

    public CardRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        mongoTemplate.indexOps(Card.class).ensureIndex(new Index().named("created_date_desc").on("created_date", Sort.Direction.DESC).background());
        mongoTemplate.indexOps(Card.class).ensureIndex(new Index().named("exported_date_desc").on("exported_date", Sort.Direction.DESC).background());
        mongoTemplate.indexOps(Card.class).ensureIndex(new Index().named("updated_date_desc").on("updated_date", Sort.Direction.DESC).background());
        mongoTemplate.indexOps(Card.class).ensureIndex(new Index().named("status_desc").on("status", Sort.Direction.DESC).background());
        mongoTemplate.indexOps(Card.class).ensureIndex(new Index().named("price_desc").on("price", Sort.Direction.DESC).background());
        mongoTemplate.indexOps(Card.class).ensureIndex(new Index().named("user_id_desc").on("user_id", Sort.Direction.DESC).background());
//        DBObject indexOptions = new BasicDBObject();
//        indexOptions.put("service_id", 1);
//        indexOptions.put("status", 1);
//        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition((Document) indexOptions);
//        mongoTemplate.indexOps(Card.class).ensureIndex(indexDefinition.background());
//        DBObject countByFeedbackIndexOptions = new BasicDBObject();
//        countByFeedbackIndexOptions.put("service_id", 1);
//        countByFeedbackIndexOptions.put("human_status.status", 1);
//        CompoundIndexDefinition indexDef = new CompoundIndexDefinition((Document) countByFeedbackIndexOptions);
//        mongoTemplate.indexOps(Card.class).ensureIndex(indexDef.background());
    }

    @Override
    public List<Card> findNewCard(CardQueryCondition cardQueryCondition) {
        CustomFieldQuery query = new CustomFieldQuery();
        query.with(new Sort(Sort.Direction.ASC, "created_date"));
        query.excludeKeys("created_date", "tenantId", "serviceId", "video_split_frame", "createdDate", "updatedDate");
        log.info("Find new card:" + cardQueryCondition.toString());
        if (cardQueryCondition.getMobileService()!=null) {
            query.addCriteria(Criteria.where("mobile_service").is(cardQueryCondition.getMobileService().toString()));
        }
        if (cardQueryCondition.getPrice() != 0) {
            query.addCriteria(Criteria.where("price").is(cardQueryCondition.getPrice()));
        }
        query.addCriteria(Criteria.where("user_id").is(null));
        query.addCriteria(Criteria.where("status").is("NEW"));
        List<Card> cards = mongoTemplate.find(query.limit(cardQueryCondition.getQuantity()), Card.class);
        for (Card card : cards) {
            card.setExportedDate(Instant.now());
            card.setUserId(cardQueryCondition.getUserId());
            card.setStatus(Constants.Status.PENDING.toString());
            mongoTemplate.save(card);
        }

        return cards;
    }

    @Override
    public Page<Card> findOldCard(Pageable pageable, CardQueryCondition cardQueryCondition) {
        CustomFieldQuery query = new CustomFieldQuery();
        query.with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "updated_date"));
        query.excludeKeys("created_date", "tenantId", "serviceId", "video_split_frame", "createdDate", "updatedDate");
        log.info("Find new card:" + cardQueryCondition.toString());
        if (cardQueryCondition.getMobileService()!=null) {
            query.addCriteria(Criteria.where("mobile_service").is(cardQueryCondition.getMobileService().toString()));
        }
        if (cardQueryCondition.getPrice() != 0) {
            query.addCriteria(Criteria.where("price").is(cardQueryCondition.getPrice()));
        }
        if(!Strings.isNullOrEmpty(cardQueryCondition.getFromDate())){
            Instant fromDate = Instant.ofEpochMilli(Long.parseLong(cardQueryCondition.getFromDate()));
            query.addCriteria(Criteria.where("created_date").gte(fromDate));
        }
        if(!Strings.isNullOrEmpty(cardQueryCondition.getToDate())){
            Instant toDate = Instant.ofEpochMilli(Long.parseLong(cardQueryCondition.getToDate()));
            query.addCriteria(Criteria.where("created_date").lte(toDate));
        }
        query.addCriteria(Criteria.where("user_id").is(cardQueryCondition.getUserId()));
        query.addCriteria(Criteria.where("status").ne("NEW"));
        List<Card> cardList = mongoTemplate.find(query, Card.class);
        Long total = mongoTemplate.count(query, Card.class);
        return new PageImpl<Card>(cardList, pageable, total);
    }
}
