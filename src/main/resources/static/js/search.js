var Search = function () {
    // 页大小
    var PAGE_SIZE = 10;
    // 日期格式显示长度
    var DATE_FORMAT_LEN = 19;
    // 博文地址
    var BLOG_SERVER = "";
    var BLOG_POSTS_URL_PREFIX = "/archives/"
    // 博客标签地址前缀
    var BLOG_TAG_URL_PREFIX = '/tags/';
    // 博文编辑地址前缀
    var BLOG_POSTS_EDIT_URL_PREFIX = "/admin/index.html#/posts/write?postId=";

    function bindEvents() {
        $("#search-input").keypress(function (e) {
            // 绑定回车事件
            if (e.which == 13) {
                search();
            }
        });
        $("#search-btn").on("click", function () {
            search();
        });
        $("#btn-store2es").on("click", function () {
            if ($(this).hasClass("js-clicked")) {
                return;
            }
            $(this).addClass("js-clicked");
            $.post("/api/halo/post", {}, function(result, status) {
                console.log(result, status);
                $("#btn-store2es").html("保存成功");
            });
        });
        $("#btn-delete-from-es").on("click", function () {
            $.ajax({
                url: "/api/halo/post",
                type: "DELETE",
                success: function(result, status) {
                    console.log(result, status);
                    $("#btn-delete-from-es").html("删除成功");
                }
            });
        });
    }

    function clearResult() {
        var noResultTpl = document.getElementById('noResultTpl').innerHTML;
        $('#search-result-container').html(noResultTpl);
    }

    function showPagination(isShow) {
        isShow ? $('#pagination-container').show() : $('#pagination-container').hide();
    }

    function showLoading() {
        var loadingTpl = document.getElementById('loadingTpl').innerHTML;
        $('#search-result-container').html(loadingTpl);
    }

    function search() {
        var keyword = $.trim($('#search-input').val());
        if (!keyword) {
            return;
        }
        console.log("search: " + keyword);
        $.getJSON("/api/halo/post/count", {
            keyword: keyword
        }, function(data) {
            if (!data || !data.success) {
                console.error("查询数量出错！");
                console.error(data);
                clearResult();
                return;
            }
            var dataCount = data.count;
            console.log("共查询到 " + dataCount + " 个结果");
            if (!dataCount) {
                clearResult();
                showPagination(false);
                return;
            }
            showPagination(true);
            // 设置分页
            new Pagination('#pagination-container', {
                pageSize: PAGE_SIZE,
                autoLoad: true,
                unit: '条',
                toPage: function(index, _pageSize) {
                    // 设置记录总数，用于生成分页HTML内容
                    if (index === 0 || _pageSize) {
                        this.updateCount(dataCount, _pageSize);
                    }
                    // 根据页码以及分页大小生成html内容
                    getSearch(keyword, index);
                }
            });
        });
    }

    function getSearch(keyword, index) {
        showLoading();
        console.log("第 " + index + " 页");
        $.getJSON("/api/halo/post", {
            keyword: keyword,
            pageNo: index,
            pageSize: PAGE_SIZE
        }, function(data) {
            if (!data || !data.success) {
                console.error("查询结果出错！");
                console.error(data);
                return;
            }
            // 有bug，点击页码不更新
            console.log("success");
            renderPosts(data.result);
        });
    }

     function renderPosts(posts) {
        if (!posts || posts.length == 0) {
            clearResult();
            return;
        }
        // 处理标签名字和连接
        for (var i = 0; i < posts.length; i++) {
            posts[i].tagArr = [];
            if (posts[i].tags) {
                continue;
                var _tagNames = posts[i].tags.split(","),
                    _tagSlugs = posts[i].tagSlugs.split(",");
                for (var j = 0; j < _tagNames.length; j++) {
                    posts[i].tagArr[posts[i].tagArr.length] = {
                        tagName: _tagNames[j],
                        tagSlug: _tagSlugs[j]
                    };
                }
            }
        }
        juicer.register("formatDate", formatDate);
        var searchResultTpl = document.getElementById('searchResultTpl').innerHTML;
        // console.log(searchResultTpl);
        $('#search-result-container').html(juicer(searchResultTpl, {
            posts: posts,
            blogServer: BLOG_SERVER,
            postUrlPrefix: BLOG_POSTS_URL_PREFIX,
            blogTagPrefix: BLOG_TAG_URL_PREFIX,
            postEditPrefix: BLOG_POSTS_EDIT_URL_PREFIX
        }));
    }

    function formatDate(dateStr) {
        if (!dateStr || dateStr.length < DATE_FORMAT_LEN) {
            return '';
        }
        return dateStr.substring(0, DATE_FORMAT_LEN);
    }

    // 初始化
    function init(blogBaseUrl) {
        BLOG_SERVER = blogBaseUrl;
        clearResult();
        bindEvents();
    }

    return {
        init : init
    };
}();