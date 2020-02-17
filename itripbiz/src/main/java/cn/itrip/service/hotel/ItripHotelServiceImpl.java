package cn.itrip.service.hotel;

import beans.pojo.ItripAreaDic;
import beans.pojo.ItripHotel;
import beans.pojo.ItripLabelDic;
import beans.vo.hotel.HotelVideoDescVO;
import beans.vo.hotel.ItripSearchDetailsHotelVO;
import beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import beans.vo.hotel.ItripSearchPolicyHotelVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import dao.hotel.ItripHotelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelServiceImpl implements ItripHotelService {
    @Resource
    private ItripHotelMapper itripHotelMapper;


    @Override
    public HotelVideoDescVO getVideoDescByHotelId(Long id) throws Exception {
        HotelVideoDescVO hotelVideoDescVO = new HotelVideoDescVO();
        List<ItripAreaDic> itripAreaDicList = new ArrayList<>();
        itripAreaDicList = itripHotelMapper.getHotelAreaByHotelId(id);
        List<String> tempList1 = new ArrayList<>();
        for (ItripAreaDic itripAreaDic : itripAreaDicList) {
            tempList1.add(itripAreaDic.getName());
        }
        hotelVideoDescVO.setTradingAreaNameList(tempList1);

        List<ItripLabelDic> itripLabelDicList = new ArrayList<>();
        itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
        List<String> tempList2 = new ArrayList<>();
        for (ItripLabelDic itripLabelDic : itripLabelDicList) {
            tempList2.add(itripLabelDic.getName());
        }
        hotelVideoDescVO.setHotelFeatureList(tempList2);

        hotelVideoDescVO.setHotelName(itripHotelMapper.getItripHotelById(id).getHotelName());
        return hotelVideoDescVO;
    }

    @Override
    public ItripHotel getItripHotelById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelById(id);
    }

    @Override
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }

    @Override
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id) throws Exception {
        return itripHotelMapper.queryHotelPolicy(id);
    }

    @Override
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
        List<ItripLabelDic> itripLabelDicList = new ArrayList<>();
        itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
        ItripSearchDetailsHotelVO vo = new ItripSearchDetailsHotelVO();
        List<ItripSearchDetailsHotelVO> list = new ArrayList<ItripSearchDetailsHotelVO>();
        vo.setName("酒店介绍");
        vo.setDescription(itripHotelMapper.getItripHotelById(id).getDetails());
        list.add(vo);
        for (ItripLabelDic itripLabelDic:itripLabelDicList) {
            ItripSearchDetailsHotelVO vo2 = new ItripSearchDetailsHotelVO();
            vo2.setName(itripLabelDic.getName());
            vo2.setDescription(itripLabelDic.getDescription());
            list.add(vo2);
        }
        return list;
    }

    @Override
    public List<ItripHotel> getItripHotelListByMap(Map<String, Object> param) throws Exception {
        return itripHotelMapper.getItripHotelListByMap(param);
    }

    /**
     * 记录总数
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public Integer getItripHotelCountByMap(Map<String, Object> param) throws Exception {
        return itripHotelMapper.getItripHotelCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripHotel(ItripHotel itripHotel) throws Exception {
        itripHotel.setCreationDate(new Date());
        return itripHotelMapper.insertItripHotel(itripHotel);
    }

    @Override
    public Integer itriptxModifyItripHotel(ItripHotel itripHotel) throws Exception {
        itripHotel.setModifyDate(new Date());
        return itripHotelMapper.updateItripHotel(itripHotel);
    }

    @Override
    public Integer itriptxDeleteItripHotelById(Long id) throws Exception {
        return itripHotelMapper.deleteItripHotelById(id);
    }

    @Override
    public Page<ItripHotel> queryItripHotelPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripHotelMapper.getItripHotelCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? 1: pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? 10: pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotel> itripHotelList = itripHotelMapper.getItripHotelListByMap(param);
        page.setRows(itripHotelList);
        return page;
    }
}
