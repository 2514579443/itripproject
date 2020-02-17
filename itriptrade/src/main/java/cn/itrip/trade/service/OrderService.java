package cn.itrip.trade.service;

import beans.pojo.ItripHotelOrder;

public interface OrderService {

    /**
     * 加载酒店订单
     *
     * @param orderNo
     * @return
     */
    public ItripHotelOrder loadItripHotelOrder(String orderNo) throws Exception;

    /**
     * 判断订单支付状态
     *
     * @param orderNo
     * @return
     */
    public boolean processed(String orderNo) throws Exception;

    /**
     * 支付成功
     *
     * @param orderNo
     * @param payType
     * @param tradeNo
     */
    public void paySuccess(String orderNo, int payType, String tradeNo) throws Exception;

    /**
     * 支付失败
     *
     * @param orderNo
     * @param payType
     * @param tradeNo
     */
    public void payFailed(String orderNo, int payType, String tradeNo) throws Exception;

}
