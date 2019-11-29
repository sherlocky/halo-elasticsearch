package com.sherlocky.halo.dao;

import com.sherlocky.halo.model.PostModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>使用注解形式配置Mapper</p>
 * 注解的一些解释:
 * <li>@Select 是查询类的注解，所有的查询均使用这个</li>
 * <li>@Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类* 属性和数据库属性名保持一致，就不需要这个属性来修饰。</li>
 * <li>@Insert 插入数据库使用，直接传入实体类会自动解析属性到对应的值</li>
 * <li>@Update 负责修改，也可以直接传入对象</li>
 * <li>@delete 负责删除</li>
 * <br>
 * <p>注意：启用注解配置，需要将 application 配置文件中的 【mybatis.mapper-locations】属性注释掉掉再启动项目，否则会报已存在的错。</p>
 *
 * @author: zhangcx
 * @date: 2018/12/2 16:11
 */
@Mapper
public interface PostDAO {
    /**
     * 只查询出已经发布的有效的字段，且按照更新时间倒序排列
     * <p>如果实例对象中的属性名和数据表中字段名不一致，可以用 @Result注解进行说明映射关系</p>
     * @return
     */
    @Select("SELECT posts.id, posts.title, posts.slug, posts.markdown, posts.published_at, posts.updated_at, GROUP_CONCAT(tags.name) AS tags " +
            "FROM posts " +
            "LEFT JOIN posts_tags ON posts.id=posts_tags.post_id " +
            "LEFT JOIN tags ON posts_tags.tag_id=tags.id " +
            // "where posts.status='published' " +
            "GROUP BY posts.id " +
            "order by posts.updated_at desc")
    @Results(id = "postMap", value = {
            @Result(property = "title", column = "title"),
            @Result(property = "url", column = "slug"),
            @Result(property = "content", column = "markdown"),
            @Result(property = "publishedAt", column = "published_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "tags", column = "tags")
    })
    List<PostModel> list();

    /**
     * 根据ids查询博文
     * <p>此处要foreach，使用了<script>,传参数时需要加上 @Param("ids")，且for循环中要使用${} </p>
     * @param ids
     * @return
     */
    @Select("<script>" +
            "SELECT posts.id, posts.title, posts.slug, posts.markdown, posts.published_at, posts.updated_at, GROUP_CONCAT(tags.name) AS tags " +
            "FROM posts " +
            "LEFT JOIN posts_tags ON posts.id=posts_tags.post_id " +
            "LEFT JOIN tags ON posts_tags.tag_id=tags.id " +
            "where posts.id in " +
            "<foreach collection='ids' open='(' close=')' separator=',' item='id'>" +
            "'${id}'" +
            "</foreach>"+
            " GROUP BY posts.id" +
            "</script>")
    @ResultMap(value = "postMap")
    List<PostModel> listByIds(@Param("ids") List<String> ids);

    @Select("SELECT posts.id, posts.title, posts.slug, posts.markdown, posts.published_at, posts.updated_at, GROUP_CONCAT(tags.name) AS tags " +
            "FROM posts " +
            "LEFT JOIN posts_tags ON posts.id=posts_tags.post_id " +
            "LEFT JOIN tags ON posts_tags.tag_id=tags.id " +
            "WHERE posts.id = #{id} GROUP BY posts.id")
    @ResultMap(value = "postMap")
    PostModel get(String id);
}
