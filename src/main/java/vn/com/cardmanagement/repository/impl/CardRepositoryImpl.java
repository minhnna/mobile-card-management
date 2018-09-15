package vn.com.cardmanagement.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.QueryMapper;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.com.cardmanagement.config.Constants;
import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.repository.CardRepositoryCustom;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CardRepositoryImpl implements CardRepositoryCustom {
    private static final long EXPIRED_TIME = 240000;
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
        CustomFieldQuery checkQuery = new CustomFieldQuery();
        checkQuery.addCriteria(Criteria.where("user_id").is(cardQueryCondition.getUserId()));
        checkQuery.addCriteria(Criteria.where("status").is("PENDING"));
        List<Card> pendingCards = mongoTemplate.find(checkQuery, Card.class);
        if (pendingCards != null && pendingCards.size() > 0) {
            return pendingCards;
        } else {
            CustomFieldQuery query = new CustomFieldQuery();
            query.with(new Sort(Sort.Direction.ASC, "created_date"));
            query.excludeKeys("created_date", "tenantId", "serviceId", "video_split_frame", "createdDate", "updatedDate");
            log.info("Find new card:" + cardQueryCondition.toString());
            if (cardQueryCondition.getMobileService() != null) {
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
    }

    @Override
    public Page<Card> findOldCard(Pageable pageable, CardQueryCondition cardQueryCondition) {
        CustomFieldQuery query = new CustomFieldQuery();
        query.with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "updated_date"));
        log.info("Find old card:" + cardQueryCondition.toString());
        if (cardQueryCondition.getMobileService() != null) {
            query.addCriteria(Criteria.where("mobile_service").is(cardQueryCondition.getMobileService().toString()));
        }
        if (cardQueryCondition.getPrice() != 0) {
            query.addCriteria(Criteria.where("price").is(cardQueryCondition.getPrice()));
        }
        Criteria gte = null, lte = null;
        if (!Strings.isNullOrEmpty(cardQueryCondition.getFromDate())) {
            Instant fromDate = Instant.ofEpochMilli(Long.parseLong(cardQueryCondition.getFromDate()));
            gte = Criteria.where("created_date").gte(fromDate);
        }
        if (!Strings.isNullOrEmpty(cardQueryCondition.getToDate())) {
            Instant toDate = Instant.ofEpochMilli(Long.parseLong(cardQueryCondition.getToDate()));
            lte = Criteria.where("created_date").lte(toDate);
        }
        if (gte != null && lte != null) {
            query.addCriteria(new Criteria().andOperator(gte, lte));
        } else {
            if (gte != null) {
                query.addCriteria(gte);
            }
            if (lte != null) {
                query.addCriteria(lte);
            }
        }
        if (!Strings.isNullOrEmpty(cardQueryCondition.getUserId())) {
            query.addCriteria(Criteria.where("user_id").is(cardQueryCondition.getUserId()));
        }
        query.addCriteria(Criteria.where("status").ne("NEW"));
        List<Card> cardList = mongoTemplate.find(query, Card.class);
        Long total = mongoTemplate.count(query, Card.class);
        return new PageImpl<>(cardList, pageable, total);
    }

    @Override
    public List<Card> findOldCard(CardQueryCondition cardQueryCondition) {
        CustomFieldQuery query = new CustomFieldQuery();
        query.with(new Sort(Sort.Direction.DESC, "updated_date"));
        log.info("Find old card for report:" + cardQueryCondition.toString());
        if (cardQueryCondition.getMobileService() != null) {
            query.addCriteria(Criteria.where("mobile_service").is(cardQueryCondition.getMobileService().toString()));
        }
        if (cardQueryCondition.getPrice() != 0) {
            query.addCriteria(Criteria.where("price").is(cardQueryCondition.getPrice()));
        }
        Criteria gte = null, lte = null;
        if (!Strings.isNullOrEmpty(cardQueryCondition.getFromDate())) {
            Instant fromDate = Instant.ofEpochMilli(Long.parseLong(cardQueryCondition.getFromDate()));
            gte = Criteria.where("created_date").gte(fromDate);
        }
        if (!Strings.isNullOrEmpty(cardQueryCondition.getToDate())) {
            Instant toDate = Instant.ofEpochMilli(Long.parseLong(cardQueryCondition.getToDate()));
            lte = Criteria.where("created_date").lte(toDate);
        }
        if (gte != null && lte != null) {
            query.addCriteria(new Criteria().andOperator(gte, lte));
        } else {
            if (gte != null) {
                query.addCriteria(gte);
            }
            if (lte != null) {
                query.addCriteria(lte);
            }
        }
        if (!Strings.isNullOrEmpty(cardQueryCondition.getUserId())) {
            query.addCriteria(Criteria.where("user_id").is(cardQueryCondition.getUserId()));
        }
//        query.addCriteria(Criteria.where("status").ne("NEW"));
        return mongoTemplate.find(query, Card.class);
    }

    @Override
    public Page<Card> findExpiredCard(Pageable pageable) {
        CustomFieldQuery query = new CustomFieldQuery();
        query.addCriteria(Criteria.where("status").is("NEW"));
        query.addCriteria(Criteria.where("created_date").lte(
            Instant.ofEpochMilli(System.currentTimeMillis() - EXPIRED_TIME)));
        List<Card> cardList = mongoTemplate.find(query, Card.class);
        Long total = mongoTemplate.count(query, Card.class);
        return new PageImpl<>(cardList, pageable, total);
    }

    @Override
    public Card findOne(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Card.class);
    }

    @Override
    public Page<String> getAllManagedPendingUsers(Pageable pageable) {
        QueryMapper mapper = new QueryMapper(mongoTemplate.getConverter());
        CustomFieldQuery userIdQuery = new CustomFieldQuery();
        userIdQuery.with(new Sort(Sort.Direction.ASC, "exported_date"));
        userIdQuery.fields().include("user_id");
        userIdQuery.addCriteria(Criteria.where("status").is(String.valueOf(Constants.Status.PENDING)));
        org.bson.Document mappedQuery = mapper.getMappedObject(userIdQuery.getQueryObject(), Optional.empty());
        List<String> userIds = mongoTemplate.getCollection("card")
            .distinct("user_id", mappedQuery, String.class)
            .into(new ArrayList<>());
        Long total = Long.valueOf(userIds.size());
        return new PageImpl<>(userIds, pageable, total);
    }
}
