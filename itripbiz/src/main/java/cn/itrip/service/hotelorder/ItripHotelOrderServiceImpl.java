package cn.itrip.service.hotelorder;

import beans.pojo.ItripHotelOrder;
import beans.pojo.ItripOrderLinkUser;
import beans.pojo.ItripUserLinkUser;
import beans.vo.order.ItripListHotelOrderVO;
import beans.vo.order.ItripPersonalOrderRoomVO;
import cn.itrip.common.BigDecimalUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import dao.hotelorder.ItripHotelOrderMapper;
import dao.hotelroom.ItripHotelRoomMapper;
import dao.hoteltempstore.ItripHotelTempStoreMapper;
import dao.orderlinkuser.ItripOrderLinkUserMapper;
import dao.productstore.ItripProductStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_DOWN;

@Service
public class ItripHotelOrderServiceImpl implements ItripHotelOrderService {
    @Resource
    private ItripHotelOrderMapper itripHotelOrderMapper;

    @Resource
    private ItripHotelTempStoreMapper itripHotelTempStoreMapper;

    @Resource
    private ItripProductStoreMapper itripProductStoreMapper;

    @Resource
    private ItripHotelRoomMapper itripHotelRoomMapper;

    @Resource
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;


    public ItripHotelOrder getItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderById(id);
    }

    public List<ItripHotelOrder> getItripHotelOrderListByMap(Map<String, Object> param) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderListByMap(param);
    }

    public Integer getItripHotelOrderCountByMap(Map<String, Object> param) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
    }

    public Map<String, String> itriptxAddItripHotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> linkUserList) throws Exception {
        //定义变量map，里面存放订单的id和orderNo返回给前端
        Map<String, String> map = new HashMap<String, String>();
        if (null != itripHotelOrder) {
            int flag = 0;
            if (EmptyUtils.isNotEmpty(itripHotelOrder.getId())) {
                //删除联系人
                itripOrderLinkUserMapper.deleteItripOrderLinkUserByOrderId(itripHotelOrder.getId());
                itripHotelOrder.setModifyDate(new Date());
                flag = itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
            } else {
                itripHotelOrder.setCreationDate(new Date());
                flag = itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
            }
            if (flag > 0) {
                Long orderId = itripHotelOrder.getId();
                //添加订单之后还需要往订单与常用联系人关联表中添加记录
                if (orderId > 0) {
                    for (ItripUserLinkUser itripUserLinkUser : linkUserList) {
                        ItripOrderLinkUser itripOrderLinkUser = new ItripOrderLinkUser();
                        itripOrderLinkUser.setOrderId(orderId);
                        itripOrderLinkUser.setLinkUserId(itripUserLinkUser.getId());
                        itripOrderLinkUser.setLinkUserName(itripUserLinkUser.getLinkUserName());
                        itripOrderLinkUser.setCreationDate(new Date());
                        itripOrderLinkUser.setCreatedBy(itripHotelOrder.getCreatedBy());
                        itripOrderLinkUserMapper.insertItripOrderLinkUser(itripOrderLinkUser);
                    }
                }
                map.put("id", itripHotelOrder.getId().toString());
                map.put("orderNo", itripHotelOrder.getOrderNo());
                return map;
            }
        }
        return map;
    }

    public Integer itriptxModifyItripHotelOrder(ItripHotelOrder itripHotelOrder) throws Exception {
        ItripHotelOrder modifyItripHotelOrder = itripHotelOrderMapper.getItripHotelOrderById(itripHotelOrder.getId());
        //更新临时表的库存
        Map<String, Object> roomStoreMap = new HashMap<String, Object>();
        roomStoreMap.put("startTime", modifyItripHotelOrder.getCheckInDate());
        roomStoreMap.put("endTime", modifyItripHotelOrder.getCheckOutDate());
        roomStoreMap.put("count", modifyItripHotelOrder.getCount());
        roomStoreMap.put("roomId", modifyItripHotelOrder.getRoomId());
        itripHotelTempStoreMapper.updateRoomStore(roomStoreMap);
        return itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
    }

    public Integer itriptxDeleteItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.deleteItripHotelOrderById(id);
    }

    public Page<ItripHotelOrder> queryItripHotelOrderPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? 1 : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? 10 : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotelOrder> itripHotelOrderList = itripHotelOrderMapper.getItripHotelOrderListByMap(param);
        page.setRows(itripHotelOrderList);
        return page;
    }

    public boolean updateHotelOrderStatus(Long id) throws Exception {
        return itripHotelOrderMapper.updateHotelOrderStatus(id, null) > 0 ? true : false;
    }

    public int getRoomNumByRoomIdTypeAndDate(Integer roomId, String startDate, String endDate) throws Exception {


        return itripHotelOrderMapper.getRoomNumByRoomIdTypeAndDate(roomId, startDate, endDate);
    }

    public BigDecimal getOrderPayAmount(int count, Long roomId) throws Exception {
        BigDecimal payAmount = null;
        BigDecimal roomPrice = itripHotelRoomMapper.getItripHotelRoomById(roomId).getRoomPrice();
        payAmount = BigDecimalUtil.OperationASMD(count, roomPrice,
                BigDecimalUtil.BigDecimalOprations.multiply,
                2, ROUND_DOWN);
        return payAmount;
    }

    public Page<ItripListHotelOrderVO> queryOrderPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripHotelOrderMapper.getOrderCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? 1 : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? 10 : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripListHotelOrderVO> itripHotelOrderList = itripHotelOrderMapper.getOrderListByMap(param);
        page.setRows(itripHotelOrderList);
        return page;
    }

    @Override
    public boolean flushOrderStatus(Integer type) throws Exception {
        Integer flag;
        if (type == 1) {
            flag = itripHotelOrderMapper.flushCancelOrderStatus();
        } else {
            flag = itripHotelOrderMapper.flushSuccessOrderStatus();
        }
        return flag > 0 ? true : false;
    }

    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(Long orderId) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderRoomInfoById(orderId);
    }
}
