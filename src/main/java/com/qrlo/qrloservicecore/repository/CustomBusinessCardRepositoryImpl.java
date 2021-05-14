package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.UserBusinessCard;
import com.qrlo.qrloservicecore.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Component
public class CustomBusinessCardRepositoryImpl implements BusinessCardRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public CustomBusinessCardRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<UserBusinessCard> findUnwoundBusinessCardForUserById(String userId, String businessCardId) {
        MatchOperation userMatch = Aggregation.match(Criteria.where("_id").is(userId));
        ProjectionOperation businessCardProjection = Aggregation.project()
                .andInclude("firstName", "lastName", "version")
                .and(context -> {
            Document document = new Document();
            document.put("input", "$myBusinessCards");
            document.put("as", "myBusinessCard");
            document.put("cond", new Document("$eq", Arrays.asList("$$myBusinessCard._id", new ObjectId(businessCardId))));
            return new Document("$filter", document);
        }).as("myBusinessCard");
        UnwindOperation unwindOperation = Aggregation.unwind("myBusinessCard");
        ProjectionOperation finalProjection = Aggregation.project()
                .andInclude("firstName", "lastName", "version")
                .and("_id").as("userId")
                .and("myBusinessCard._id").as("businessCardId")
                .and("myBusinessCard.company").as("company")
                .and("myBusinessCard.email").as("email")
                .and("myBusinessCard.phone").as("phone");
        Aggregation aggregation = Aggregation.newAggregation(userMatch, businessCardProjection, unwindOperation, finalProjection);
        return mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(User.class), UserBusinessCard.class).elementAt(0);
    }
}
