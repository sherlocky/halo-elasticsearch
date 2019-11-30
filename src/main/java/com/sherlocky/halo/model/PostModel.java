package com.sherlocky.halo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * Halo博文 DO 类
 * 同时还是 ES 博文 DO 类
 *
 * @author: zhangcx
 * @date: 2019/1/26 16:30
 */
@Document(indexName = "halo_posts", type = "post", shards = 1, replicas = 0, refreshInterval = "-1")
public class PostModel implements Serializable {
    private static final long serialVersionUID = 433731869110457614L;
    /** elasticsearch 注解 */
    @Id
    @Field(type = FieldType.Text)
    private String id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String url;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Text)
    private String publishedAt;
    @Field(type = FieldType.Text)
    private String updatedAt;
    @Field(type = FieldType.Text)
    private String tags;
    @Field(type = FieldType.Text)
    private String tagSlugs;
    /** 访问量 */
    @Field(type = FieldType.Integer)
    private Integer visits;

    public PostModel() {
    }

    public PostModel(String id) {
        this.id = id;
    }

    public PostModel(String id, String title, String url, String content) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTagSlugs() {
        return tagSlugs;
    }

    public void setTagSlugs(String tagSlugs) {
        this.tagSlugs = tagSlugs;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }
}
