package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Service
public class FilterService {

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;


    // считает объекты, у которых ревизии зеленые
    public int countObjectsWithFilteredRevisions(MyObject root, LocalDate startDate, LocalDate endDate, Double amount, String color) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);
        int count = 0;

        while (!stack.isEmpty()) {
            MyObject current = stack.pop();

            if (current.getRevisions() != null) {
                List<Revision> filteredRevisions = current.getRevisions().stream()
                        .filter(revision -> (startDate == null || !revision.getStartDate().isBefore(startDate)))
                        .filter(revision -> (endDate == null || !revision.getEndDate().isAfter(endDate)))
                        .filter(revision -> (amount == null || revision.getAmount() == amount))
                        .filter(revision -> (color == null || revision.getColor().equals(color)))
                        .toList();

                if (!filteredRevisions.isEmpty()) {
                    count++;

                    // Добавляем дочерние объекты только если есть подходящие ревизии
                    for (Revision revision : filteredRevisions) {
                        if (revision.getChildObjects() != null) {
                            stack.addAll(revision.getChildObjects());
                        }
                    }
                }
            }


        }

        return count;
    }



    // считает все объекты от зеленых ревизий
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<MyObject> findObjectsWithGreenRevisions() {
        List<Document> pipeline = List.of(
                new Document("$match", new Document("parentRevision", null)),
                new Document("$lookup", new Document()
                        .append("from", "revision")
                        .append("let", new Document("revisionIds", "$revisions.$id"))
                        .append("pipeline", List.of(
                                new Document("$match", new Document("$expr", new Document("$in", List.of("$_id", "$$revisionIds")))),
                                new Document("$match", new Document("color", "green")),
                                new Document("$lookup", new Document()
                                        .append("from", "myObject")
                                        .append("let", new Document("childObjectIds", "$childObjects.$id"))
                                        .append("pipeline", List.of(
                                                new Document("$match", new Document("$expr", new Document("$in", List.of("$_id", "$$childObjectIds")))),
                                                new Document("$lookup", new Document()
                                                        .append("from", "revision")
                                                        .append("let", new Document("revisionIds", "$revisions.$id"))
                                                        .append("pipeline", List.of(
                                                                new Document("$match", new Document("$expr", new Document("$in", List.of("$_id", "$$revisionIds")))),
                                                                new Document("$match", new Document("color", "green")),
                                                                new Document("$lookup", new Document()
                                                                        .append("from", "myObject")
                                                                        .append("let", new Document("childObjectIds", "$childObjects.$id"))
                                                                        .append("pipeline", List.of(
                                                                                new Document("$match", new Document("$expr", new Document("$in", List.of("$_id", "$$childObjectIds")))),
                                                                                new Document("$lookup", new Document()
                                                                                        .append("from", "revision")
                                                                                        .append("let", new Document("revisionIds", "$revisions.$id"))
                                                                                        .append("pipeline", List.of(
                                                                                                new Document("$match", new Document("$expr", new Document("$in", List.of("$_id", "$$revisionIds")))),
                                                                                                new Document("$match", new Document("color", "green"))
                                                                                        ))
                                                                                        .append("as", "revisions")
                                                                                )
                                                                        ))
                                                                        .append("as", "childObjects")
                                                                )
                                                        ))
                                                        .append("as", "revisions")
                                                )
                                        ))
                                        .append("as", "childObjects")
                                )
                        ))
                        .append("as", "revisions")
                )
        );

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection("myObject").withCodecRegistry(pojoCodecRegistry);

        return collection.aggregate(pipeline, MyObject.class).into(new ArrayList<>());
    }

    //запрос

//    [
//    {
//        $match: {
//            parentRevision: null
//        }
//    },
//    {
//        $lookup: {
//            from: "revision",
//                    let: {
//                revisionIds: "$revisions.$id"
//            },
//            pipeline: [
//            {
//                $match: {
//                    $expr: {
//                        $in: ["$_id", "$$revisionIds"]
//                    }
//                }
//            },
//            {
//                $match: {
//                    color: "green"
//                }
//            },
//            {
//                $lookup: {
//                    from: "myObject",
//                            let: {
//                        childObjectIds: "$childObjects.$id"
//                    },
//                    pipeline: [
//                    {
//                        $match: {
//                            $expr: {
//                                $in: [
//                                "$_id",
//                                        "$$childObjectIds"
//                    ]
//                            }
//                        }
//                    },
//                    {
//                        $lookup: {
//                            from: "revision",
//                                    let: {
//                                revisionIds: "$revisions.$id"
//                            },
//                            pipeline: [
//                            {
//                                $match: {
//                                    $expr: {
//                                        $in: [
//                                        "$_id",
//                                                "$$revisionIds"
//                          ]
//                                    }
//                                }
//                            },
//                            {
//                                $match: {
//                                    color: "green"
//                                }
//                            },
//                            {
//                                $lookup: {
//                                    from: "myObject",
//                                            let: {
//                                        childObjectIds:
//                                        "$childObjects.$id"
//                                    },
//                                    pipeline: [
//                                    {
//                                        $match: {
//                                            $expr: {
//                                                $in: [
//                                                "$_id",
//                                                        "$$childObjectIds"
//                                ]
//                                            }
//                                        }
//                                    },
//                                    {
//                                        $lookup: {
//                                            from: "revision",
//                                                    let: {
//                                                revisionIds:
//                                                "$revisions.$id"
//                                            },
//                                            pipeline: [
//                                            {
//                                                $match: {
//                                                    $expr: {
//                                                        $in: [
//                                                        "$_id",
//                                                                "$$revisionIds"
//                                      ]
//                                                    }
//                                                }
//                                            },
//                                            {
//                                                $match: {
//                                                    color: "green"
//                                                }
//                                            }
//                              ],
//                                            as: "revisions"
//                                        }
//                                    }
//                        ],
//                                    as: "childObjects"
//                                }
//                            }
//                  ],
//                            as: "revisions"
//                        }
//                    }
//            ],
//                    as: "childObjects"
//                }
//            }
//      ],
//            as: "revisions"
//        }
//    }
//]
}
