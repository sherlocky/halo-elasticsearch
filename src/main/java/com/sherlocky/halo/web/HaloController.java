package com.sherlocky.halo.web;

import com.sherlocky.halo.es.PostEsService;
import com.sherlocky.halo.model.PostModel;
import com.sherlocky.halo.service.HaloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sherlocky.halo.constant.HaloConstants.PAGE_NO;
import static com.sherlocky.halo.constant.HaloConstants.PAGE_SIZE;

/**
 *
 * @author: zhangcx
 * @date: 2019/1/29 10:34
 */
@RestController
@RequestMapping("/api/halo")
public class HaloController {
    private static final Logger logger = LoggerFactory.getLogger(HaloController.class);
    @Autowired
    private PostEsService postEsService;
    @Autowired
    private HaloService haloService;

    /**
     * 全文搜索 结果数
     * @param keyword
     * @return
     */
    @GetMapping("/post/count")
    public Map searchCount(@RequestParam String keyword) {
        Map data = new HashMap();
        long count = 0;
        try {
            count = postEsService.countHighlightSearch(keyword);
            if (logger.isInfoEnabled()) {
                logger.info("### 本次共查询到：{} 个可能的结果~", count);
            }
        } catch (Exception exception) {
            logger.error("$$$ 查询出错！", exception);
            data.put("success", "false");
            data.put("message", "查询出错！");
            return data;
        }
        count = Math.max(count, 0);
        data.put("success", "true");
        data.put("count", count);
        return data;
    }

    /**
     * 全文搜索
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping("/post")
    public Map search(@RequestParam(defaultValue = PAGE_NO) int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, @RequestParam String keyword) {
        Map data = new HashMap();
        List<PostModel> pms = Collections.EMPTY_LIST;
        try {
            pms = postEsService.highlightSearch(keyword, pageNo, pageSize).stream().collect(Collectors.toList());
        } catch (Exception exception) {
            logger.error("$$$ 查询出错！", exception);
            data.put("success", "false");
            data.put("message", "查询出错！");
            return data;
        }
        data.put("success", "true");
        data.put("result", pms);
        return data;
    }

    /**
     * 触发一次 保存博文到ES
     */
    @PostMapping("/post")
    public Map storePost2Es() {
        haloService.storePost2Es();

        return new HashMap() {{
            put("success", "true");
            put("message", "同步保存博文到ES成功！");
        }};
    }

    /**
     * 触发一次 从ES删除博文
     */
    @DeleteMapping("/post")
    public Map removePostFromEs() {
        haloService.removePostFromEs();

        return new HashMap() {{
            put("success", "true");
            put("message", "同步从ES删除博文成功！");
        }};
    }
}
