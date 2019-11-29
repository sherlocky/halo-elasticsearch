package com.sherlocky.halo.dao;

import com.sherlocky.halo.model.TrglogModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author: zhangcx
 * @date: 2019/1/26 17:56
 */
@Mapper
public interface TrglogDAO {
    /**
     * 获取博文相关的触发器日志
     * <p>此处要foreach，使用了<script>,传参数时需要加上 @Param("ids")，且for循环中要使用${} </p>
     * @return
     */
    @Select("<script>" +
            "SELECT * FROM trglogs where tablename=#{tablename} and operating_type in " +
            "<foreach collection='operatingTypes' open='(' close=')' separator=',' item='operatingType'>" +
            "'${operatingType}'" +
            "</foreach>"+
            " group by guid" +
            " order by operating_datetime desc" +
            "</script>")
    @Results(id = "trglogMap", value = {
            @Result(property = "operatingType", column = "operating_type")
    })
    List<TrglogModel> listTrglogs(@Param("tablename") String tablename, @Param("operatingTypes") String[] operatingTypes);

    /**
     * 根据id删除触发器日志
     * @return
     */
    @Delete("DELETE FROM trglogs WHERE id=#{id}")
    Long deleteTrglog(Integer id);

    /**
     * 根据id删除触发器日志
     * @return
     */
    @Delete("<script>" +
            "DELETE FROM trglogs WHERE id in  " +
            "<foreach collection='ids' open='(' close=')' separator=',' item='id'>" +
            "'${id}'" +
            "</foreach>"+
            "</script>")
    Long deleteTrglogs(@Param("ids") Integer[] ids);

    /**
     * 根据某一操作历史时间的触发器日志
     * <p>SQL脚本中的<要用&lt;代替</p>
     * @return
     */
    @Delete("<script>" +
            "DELETE FROM trglogs WHERE tablename=#{tablename}" +
            " and guid in  " +
            "<foreach collection='guids' open='(' close=')' separator=',' item='guid'>" +
            "'${guid}'" +
            "</foreach>"+
            " and operating_type in " +
            "<foreach collection='operatingTypes' open='(' close=')' separator=',' item='operatingType'>" +
            "'${operatingType}'" +
            "</foreach>" +
            " and operating_datetime&lt;=#{datetime}" +
            "</script>")
    Long deleteTrglogsBefore(@Param("tablename") String tablename, @Param("guids") String[] guids, @Param("operatingTypes") String[] operatingTypes, @Param("datetime") String datetime);

    @Select("<script>" +
            "SELECT count(DISTINCT guid) FROM trglogs where tablename=#{tablename} and operating_type in " +
            "<foreach collection='operatingTypes' open='(' close=')' separator=',' item='operatingType'>" +
            "'${operatingType}'" +
            "</foreach>"+
            "</script>")
    Long countTrglog(@Param("tablename") String tablename, @Param("operatingTypes") String[] operatingTypes);
}
