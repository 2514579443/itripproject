package dao;

import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.List;

public class BaseQuery<T> {
    private HttpSolrClient httpSolrClient;
    static Logger logger = Logger.getLogger(BaseQuery.class);

    public BaseQuery(String url) {
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser());// 设置响应解析器
        httpSolrClient.setConnectionTimeout(500);//连接时长
    }

    public Page queryPage(SolrQuery query, Integer pageNo, Integer pageSize, Class cla) {
        Integer rows = EmptyUtils.isEmpty(pageSize) ? 10 : pageSize;//页大小
        Integer currPage = (EmptyUtils.isEmpty(pageNo)) ? 1 - 1 : pageNo - 1;
        Integer start = currPage * rows;
        query.setStart(start);
        query.setRows(rows);
        Page page = null;
        try {
            QueryResponse queryResponse = httpSolrClient.query(query);
            SolrDocumentList docs = queryResponse.getResults();
            page = new Page(currPage + 1, query.getRows(), new Long(docs.getNumFound()).intValue());
            List list = queryResponse.getBeans(cla);
            page.setRows(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return page;
    }

    public List queryList(SolrQuery query, Integer pageSize, Class cla) {
        List list = null;
        query.setStart(0);
        query.setRows(EmptyUtils.isEmpty(pageSize) ? 10 : pageSize);

        try {
            QueryResponse queryResponse = httpSolrClient.query(query);
            list = queryResponse.getBeans(cla);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
