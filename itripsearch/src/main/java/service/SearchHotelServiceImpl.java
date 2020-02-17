package service;

import beans.vo.hotel.ItripHotelVO;
import beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import dao.BaseQuery;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHotelServiceImpl implements SearchHotelService {
    private BaseQuery<ItripHotelVO> itripHotelVOBaseQuery = new BaseQuery("http://localhost:8081/solr/hotel");

    @Override
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo, Integer pageNo, Integer pageSize) {
        SolrQuery query = new SolrQuery("*:*");
        StringBuffer tempQuery = new StringBuffer();
        int tempFlag = 0;
        if (EmptyUtils.isNotEmpty(vo)) {
            if (EmptyUtils.isNotEmpty(vo.getDestination())) { //目的地是否为空
                tempQuery.append(" destination :" + vo.getDestination());
                tempFlag = 1;
            }
            if (EmptyUtils.isNotEmpty(vo.getHotelLevel())) { //关键词否为空
                tempQuery.append(" hotelLevel :" + vo.getHotelLevel() + " ");
            }
            if (EmptyUtils.isNotEmpty(vo.getKeywords())) { //关键词否为空
                if (tempFlag == 1) {
                    tempQuery.append(" AND keyword :" + vo.getKeywords() + " ");
                } else {
                    tempQuery.append(" keyword :" + vo.getKeywords() + " ");
                }
            }
            if (EmptyUtils.isNotEmpty(vo.getFeatureIds())) {
                StringBuffer buffer = new StringBuffer("(");
                int flag = 0;
                String featureIdArray[] = vo.getFeatureIds().split(",");
                for (String featureId : featureIdArray
                ) {
                    if (flag == 0) {
                        buffer.append(" featureIds:" + "*," + featureId + ",*");
                    } else {
                        buffer.append(" OR featureIds:" + "*," + featureId + ",*");

                    }
                    flag++;
                }
                buffer.append(")");
                query.addFilterQuery(buffer.toString());
            }
            if (EmptyUtils.isNotEmpty(vo.getTradeAreaIds())) {
                StringBuffer buffer = new StringBuffer("(");
                int flag = 0;
                String tradeAreaIdArray[] = vo.getTradeAreaIds().split(",");
                for (String tradeAreaId : tradeAreaIdArray) {
                    if (flag == 0) {
                        buffer.append(" tradingAreaIds:" + "*," + tradeAreaId + ",*");
                    } else {
                        buffer.append(" OR tradingAreaIds:" + "*," + tradeAreaId + ",*");
                    }
                    flag++;
                }
                buffer.append(")");
                query.addFilterQuery(buffer.toString());
            }
            if (EmptyUtils.isNotEmpty(vo.getMaxPrice())) {
                query.addFilterQuery("minPrice:" + "[* TO " + vo.getMaxPrice() + "]");
            }
            if (EmptyUtils.isNotEmpty(vo.getMinPrice())) {
                query.addFilterQuery("minPrice:" + "[" + vo.getMinPrice() + " TO *]");
            }

            if (EmptyUtils.isNotEmpty(vo.getAscSort())) {
                query.addSort(vo.getAscSort(), SolrQuery.ORDER.asc);
            }

            if (EmptyUtils.isNotEmpty(vo.getDescSort())) {
                query.addSort(vo.getDescSort(), SolrQuery.ORDER.desc);
            }
        }
        if (EmptyUtils.isNotEmpty(tempQuery.toString())) {
            query.setQuery(tempQuery.toString());
        }
        Page<ItripHotelVO> page = itripHotelVOBaseQuery.queryPage(query, pageNo, pageSize, ItripHotelVO.class);
        return page;
    }

    @Override
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId, Integer pageSize) throws Exception {
        SolrQuery query = new SolrQuery("*:*");
        if (EmptyUtils.isNotEmpty(cityId)) {
            query.addFilterQuery("cityId:" + cityId);
        } else {
            return null;
        }
        List<ItripHotelVO> hotelVOList = itripHotelVOBaseQuery.queryList(query, pageSize, ItripHotelVO.class);
        return hotelVOList;
    }
}
