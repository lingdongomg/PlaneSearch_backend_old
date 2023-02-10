package cn.whpu.webserver.service;


import cn.whpu.webserver.vo.SearchInfoVo;

import java.sql.SQLException;

public interface SearchService {
    /**
     * 搜索机票，用户输入多少段那就返回多少段
     * @param searchInfoVo
     * @return
     */
    String searchTickets(SearchInfoVo searchInfoVo) throws Exception;
}
