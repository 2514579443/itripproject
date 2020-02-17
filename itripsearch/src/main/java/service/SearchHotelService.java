package service;

import beans.vo.hotel.ItripHotelVO;
import beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;

import java.util.List;

public interface SearchHotelService {
    /**
     * 搜索宾馆
     *
     * @param vo
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo, Integer pageNo, Integer pageSize);

    /***
     * 根据热门城市查询酒店
     * @param pageSize
     * @return
     */
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId, Integer pageSize) throws Exception;

}
