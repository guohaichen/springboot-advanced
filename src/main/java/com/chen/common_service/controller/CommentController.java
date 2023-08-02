package com.chen.common_service.controller;

import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.Comment;
import com.chen.common_service.entity.CommentCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cgh
 * @create 2023-08-01
 * 评论模块测试
 */
@RestController
@RequestMapping("/article")
@Slf4j
public class CommentController {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据id查所属评论
     *
     * @param postId 评论拥有者
     * @return
     */
    @GetMapping("/comment")
    public List<Comment> queryComment(@RequestParam("id") String postId) {
        log.info("postId:{}", postId);
        //查询所有comment
        //List<Comment> commentList = mongoTemplate.query(Comment.class).all();
        Query query = new Query().addCriteria(Criteria.where("postId").is(postId));
        List<Comment> commentList = mongoTemplate.find(query, Comment.class);

        //聚合操作demo，例如根据postId统计 评论总数
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation
                        .group("postId").count().as("commentCount"));
        AggregationResults<CommentCount> results = mongoTemplate.aggregate(aggregation, "comment", CommentCount.class);
        List<CommentCount> mappedResults = results.getMappedResults();
        //根据postId,汇总评论数
        for (CommentCount mappedResult : mappedResults) {
            log.info("文章:{},评论数:{}", mappedResult.getId(), mappedResult.getCommentCount());
        }


//        for (Comment comment : commentList) {
//            log.info("comment: {}", comment);
//        }
        return commentList;
    }

    //新增
    @PostMapping("/comment")
    public Result<?> addComment(@RequestBody Comment comment) {
        Comment save = mongoTemplate.save(comment);
        return Result.OK("新增成功", save);
    }

    //修改
    @PutMapping("/comment")
    public Result<?> updateComment(@RequestBody Comment comment) {
        //有点恶心，暂时没有看到合适的api 去 灵活更新，这里只能写死
        Update newComment = new Update().set("commentMsg", comment.getCommentMsg())
                .set("postId", comment.getPostId());

        mongoTemplate.upsert(
                (Query.query(Criteria.where("id").is(comment.getId()))),
                newComment, Comment.class);

        return Result.OK("修改成功");
    }

    //根据评论id删除
    @DeleteMapping("/comment/{id}")
    public Result<?> deleteComment(@PathVariable("id") String commentId) {
        log.info("commentId:{}", commentId);
        mongoTemplate.remove(Query.query(Criteria.where("id").is(commentId)), Comment.class);
        return Result.OK("删除成功");
    }

    //根据postId，删除全部
    @DeleteMapping("/comment/all/{postId}")
    public Result<?> deleteAllCommentByPostId(@PathVariable("postId") String postId) {
        mongoTemplate.findAllAndRemove(
                Query.query(Criteria.where("postId").is(postId)), Comment.class);
        return Result.OK("删除成功");
    }

}
