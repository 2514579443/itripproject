package cn.itrip.trade.service;

import beans.pojo.ItripHotel;
import beans.pojo.ItripHotelOrder;
import beans.pojo.ItripTradeEnds;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.SystemConfig;
import dao.hotelorder.ItripHotelOrderMapper;
import dao.tradeends.ItripTradeEndsMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private Logger logger = Logger.getLogger(OrderServiceImpl.class);
    @Resource
    private ItripHotelOrderMapper itripHotelOrderMapper;
    @Resource
    private ItripTradeEndsMapper itripTradeEndsMapper;
    @Resource
    private SystemConfig systemConfig;

    @Override
    public ItripHotelOrder loadItripHotelOrder(String orderNo) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orderNo", orderNo);
        List<ItripHotelOrder> orders = itripHotelOrderMapper.getItripHotelOrderListByMap(param);
        if (orders.size() == 1) {
            return orders.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean processed(String orderNo) throws Exception {
        ItripHotelOrder itripHotelOrder = this.loadItripHotelOrder(orderNo);
        return itripHotelOrder.getOrderStatus().equals(2) && !EmptyUtils.isEmpty(itripHotelOrder.getTradeNo());
    }

    /**
     * 支付成功
     *
     * @param orderNo
     * @param payType
     * @param tradeNo
     * @throws Exception
     */
    @Override
    public void paySuccess(String orderNo, int payType, String tradeNo) throws Exception {
        ItripHotelOrder itripHotelOrder = this.loadItripHotelOrder(orderNo);
        itripHotelOrder.setOrderStatus(2);//支付状态
        itripHotelOrder.setPayType(payType);
        itripHotelOrder.setTradeNo(tradeNo);//交易号
        itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);

        //增加订单后续待处理记录
        ItripTradeEnds itripTradeEnds = new ItripTradeEnds();
        itripTradeEnds.setId(itripHotelOrder.getId());
        itripTradeEnds.setOrderNo(itripHotelOrder.getOrderNo());
        itripTradeEndsMapper.insertItripTradeEnds(itripTradeEnds);
        //通知业务模块后续处理
        sendGet(systemConfig.getTradeEndsUrl(), "orderNo=" + orderNo);
    }

    /**
     * 支付失败
     *
     * @param orderNo
     * @param payType
     * @param tradeNo
     * @throws Exception
     */
    @Override
    public void payFailed(String orderNo, int payType, String tradeNo) throws Exception {
        ItripHotelOrder itripHotelOrder = this.loadItripHotelOrder(orderNo);
        itripHotelOrder.setOrderStatus(1);//支付状态
        itripHotelOrder.setPayType(payType);
        itripHotelOrder.setTradeNo(tradeNo);//交易号
        itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
    }

    public void sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection;
            if (systemConfig.getTradeUseProxy()) {//代理环境
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        systemConfig.getTradeProxyHost(),
                        systemConfig.getTradeProxyPort()));
                connection = realUrl.openConnection(proxy);
            } else {
                connection = realUrl.openConnection();
            }
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            System.out.println(connection.getContentLength());
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
    }
}
