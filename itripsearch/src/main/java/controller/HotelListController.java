package controller;

import beans.dto.Dto;
import beans.vo.hotel.ItripHotelVO;
import beans.vo.hotel.SearchHotCityVO;
import beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.SearchHotelService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/hotellist")
public class HotelListController {
    @Resource
    private SearchHotelService searchHotelService;

    @RequestMapping(value = "/searchItripHotelPage", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Dto<Page<ItripHotelVO>> searchInRipHoteLPage(@RequestBody SearchHotelVO vo) {
        Page page = null;
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getDestination())) {
            return DtoUtil.returnFail("目的地不能为空", "20002");
        }
        try {
            page = searchHotelService.searchItripHotelPage(vo, vo.getPageNo(), vo.getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "20001");
        }
        return DtoUtil.returnDataSuccess(page);
    }

    /**
     * 查询海外酒店
     * @param vo
     * @return
     */
    @RequestMapping(value = "/searchItripHotelListByHotCity", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Dto<Page<ItripHotelVO>> searchItripHotelListByHotCity(@RequestBody SearchHotCityVO vo) {
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getCityId())) {
            return DtoUtil.returnFail("城市id不能为空", "20004");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("cityId", vo.getCityId());
        List list = null;
        try {
            list = searchHotelService.searchItripHotelListByHotCity(vo.getCityId(), vo.getCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(list);
    }
}
