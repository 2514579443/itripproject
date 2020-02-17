package cn.itrip.controller;

import beans.dto.Dto;
import beans.pojo.ItripAreaDic;
import beans.pojo.ItripLabelDic;
import beans.vo.ItripAreaDicVO;
import beans.vo.ItripImageVO;
import beans.vo.ItripLabelDicVO;
import beans.vo.hotel.HotelVideoDescVO;
import beans.vo.hotel.ItripSearchDetailsHotelVO;
import beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import beans.vo.hotel.ItripSearchPolicyHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.service.areadis.ItripAreaDicService;
import cn.itrip.service.hotel.ItripHotelService;
import cn.itrip.service.image.ItripImageService;
import cn.itrip.service.labeldic.ItripLabelDicService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 酒店信息
 */
@Controller
@RequestMapping(value = "/api/hotel")
public class HotelController {
    private Logger logger = Logger.getLogger(HotelController.class);
    @Resource
    public ItripAreaDicService itripAreaDicService;
    @Resource
    private ItripLabelDicService itripLabelDicService;

    @Resource
    private ItripHotelService itripHotelService;

    @Resource
    private ItripImageService itripImageService;

    /**
     * 城市地区查询
     *type=1国内2，国外
     * @param type
     * @return
     */
    @RequestMapping(value = "/queryhotcity/{type}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) {
        List<ItripAreaDic> itripAreaDics = null;
        List<ItripAreaDicVO> itripAreaDicVOS = null;
        try {
            if (EmptyUtils.isNotEmpty(type)) {//判断非空
                Map param = new HashMap();
                param.put("isHot", 1);
                param.put("isChina", type);
                //城市地区查询方法
                itripAreaDics = itripAreaDicService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(itripAreaDics)) {//判断非空
                    itripAreaDicVOS = new ArrayList();
                    for (ItripAreaDic dic : itripAreaDics
                    ) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);
                        itripAreaDicVOS.add(vo);

                    }
                }
            } else {
                DtoUtil.returnFail("type不能为空", "10201");

            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", "10202");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOS);
    }

    /**
     * 查询酒店特色列表
     * @return
     */
    @RequestMapping(value = "/queryhotelfeature", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripLabelDicVO> queryHotelFeature() {
        List<ItripLabelDic> itripLabelDics = null;
        List<ItripLabelDicVO> itripAreaDicVOs = null;
        try {
            Map param = new HashMap();
            param.put("parentId", 16);
            itripLabelDics = itripLabelDicService.getItripLabelDicListByMap(param);
            if (EmptyUtils.isNotEmpty(itripLabelDics)) {
                itripAreaDicVOs = new ArrayList();
                for (ItripLabelDic dic : itripLabelDics
                ) {
                    ItripLabelDicVO vo = new ItripLabelDicVO();
                    BeanUtils.copyProperties(dic, vo);
                    itripAreaDicVOs.add(vo);

                }
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", "10205");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }

    /**
     * 查询商圈
     * @param cityId
     * @return
     */
    @RequestMapping(value = "/querytradearea/{cityId}" +
            "", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripAreaDicVO> queryTradeArea(@PathVariable Long cityId) {
        List<ItripAreaDic> itripAreaDics = null;
        List<ItripAreaDicVO> itripAreaDicVOs = null;
        try {
            if (EmptyUtils.isNotEmpty(cityId)) {
                Map param = new HashMap();
                param.put("isTradingArea", 1);
                param.put("parent", cityId);
                itripAreaDics = itripAreaDicService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(itripAreaDics)) {
                    itripAreaDicVOs = new ArrayList();
                    for (ItripAreaDic dic : itripAreaDics) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);
                        itripAreaDicVOs.add(vo);
                    }
                }
            } else {
                DtoUtil.returnFail("cityId不能为空", "10203");
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", "10204");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }

    /**
     * 根据酒店id查询酒店设施
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryhotelfacilities/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelFacilities(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {//判断非空
                //根据酒店的ID查询酒店设施
                itripSearchFacilitiesHotelVO = itripHotelService.getItripHotelFacilitiesById(id);
             //返回
                return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10206");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10207");
        }
    }

    /**
     * 根据酒店id查询酒店政策
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryhotelpolicy/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelPolicy(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        ItripSearchPolicyHotelVO itripSearchPolicyHotelVO = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {//判断非空
                //根据ID查询酒店政策
                itripSearchPolicyHotelVO = itripHotelService.queryHotelPolicy(id);
               //返回
                return DtoUtil.returnDataSuccess(itripSearchPolicyHotelVO.getHotelPolicy());
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10208");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10209");
        }
    }

    /**
     * 根据酒店id查询酒店特色和介绍
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryhoteldetails/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelDetails(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOList = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                itripSearchDetailsHotelVOList = itripHotelService.queryHotelDetails(id);
                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10210");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10211");
        }
    }

    /**
     * 查询酒店图片
     * @param targetId
     * @return
     */
    @RequestMapping(value = "/getimg/{targetId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getImgByTargetId(@ApiParam(required = true, name = "targetId", value = "酒店ID")
                                        @PathVariable String targetId) {
        Dto<Object> dto = new Dto<Object>();
        logger.debug("getImgBytargetId targetId : " + targetId);
        if (null != targetId && !"".equals(targetId)) {
            List<ItripImageVO> itripImageVOList = null;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("type", "0");
            param.put("targetId", targetId);
            try {
                itripImageVOList = itripImageService.getItripImageListByMap(param);
                dto = DtoUtil.returnSuccess("获取酒店图片成功", itripImageVOList);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店图片失败", "100212");
            }

        } else {
            dto = DtoUtil.returnFail("酒店id不能为空", "100213");
        }
        return dto;
    }

    /**
     * 根据酒店id查询酒店特色、商圈、酒店名称
     * @param hotelId
     * @return
     */
    @RequestMapping(value = "/getvideodesc/{hotelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getVideoDescByHotelId(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                             @PathVariable String hotelId) {
        Dto<Object> dto = new Dto<Object>();
        logger.debug("getVideoDescByHotelId hotelId : " + hotelId);
        if (null != hotelId && !"".equals(hotelId)) {
            HotelVideoDescVO hotelVideoDescVO = null;
            try {
                hotelVideoDescVO = itripHotelService.getVideoDescByHotelId(Long.valueOf(hotelId));
                dto = DtoUtil.returnSuccess("获取酒店视频文字描述成功", hotelVideoDescVO);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店视频文字描述失败", "100214");
            }

        } else {
            dto = DtoUtil.returnFail("酒店id不能为空", "100215");
        }
        return dto;
    }
}
