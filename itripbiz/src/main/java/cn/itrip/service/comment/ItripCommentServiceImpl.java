package cn.itrip.service.comment;

import beans.pojo.ItripAreaDic;
import beans.pojo.ItripComment;
import beans.pojo.ItripImage;
import beans.vo.comment.ItripListCommentVO;
import beans.vo.comment.ItripScoreCommentVO;
import cn.itrip.common.BigDecimalUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import dao.comment.ItripCommentMapper;
import dao.hotelorder.ItripHotelOrderMapper;
import dao.image.ItripImageMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ItripCommentServiceImpl implements ItripCommentService {

    private Logger logger = Logger.getLogger(ItripCommentServiceImpl.class);
    @Resource
    private ItripCommentMapper itripCommentMapper;
    @Resource
    private ItripImageMapper itripImageMapper;
    @Resource
    private ItripHotelOrderMapper itripHotelOrderMapper;

    /**
     * 根据ID查询评论
     *
     * @param id
     * @return
     */
    @Override
    public ItripComment getItripCommentById(Long id) throws Exception {
        return itripCommentMapper.getItripCommentById(id);
    }

    /**
     * 查询评论分页
     *
     * @param param
     * @return
     */
    @Override
    public List<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentListByMap(param);
    }

    /**
     * 查询记录数
     *
     * @param param
     * @return
     */
    @Override
    public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentCountByMap(param);
    }

    /**
     * 新增评论
     *
     * @param obj
     * @param itripImages
     * @return
     */
    @Override
    public boolean itriptxAddItripComment(ItripComment obj, List<ItripImage> itripImages) throws Exception {
        if(null != obj ){
            //计算综合评分，综合评分=(设施+卫生+位置+服务)/4
            float score = 0;
            int sum = obj.getFacilitiesScore()+obj.getHygieneScore()+obj.getPositionScore()+obj.getServiceScore();
            score = BigDecimalUtil.OperationASMD(sum,4, BigDecimalUtil.BigDecimalOprations.divide,1, BigDecimal.ROUND_DOWN).floatValue();
            //对结果四舍五入
            obj.setScore(Math.round(score));
            Long commentId = 0L;
            if(itripCommentMapper.insertItripComment(obj) > 0 ){
                commentId = obj.getId();
                logger.debug("新增评论id：================ " + commentId);
                if(null != itripImages && itripImages.size() > 0 && commentId > 0){
                    for (ItripImage itripImage:itripImages) {
                        itripImage.setTargetId(commentId);
                        itripImageMapper.insertItripImage(itripImage);
                    }
                }
                //更新订单表-订单状态为4（已评论）
                itripHotelOrderMapper.updateHotelOrderStatus(obj.getOrderId(),obj.getCreatedBy());
                return true;
            }
        }
        return false;
    }

    /**
     * 修改评论
     *
     * @param itripComment
     * @return
     */
    @Override
    public Integer itriptxModifyItripComment(ItripComment itripComment) throws Exception {
        return itripCommentMapper.updateItripComment(itripComment);
    }

    /**
     * 删除评论
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Integer itriptxDeleteItripCommentById(Long id) throws Exception {
        return itripCommentMapper.deleteItripCommentById(id);
    }

    /**
     * 分页的实现
     *
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<ItripListCommentVO> queryItripCommentPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripCommentMapper.getItripCommentCountByMap(param);//记录总数
        pageNo = EmptyUtils.isEmpty(pageNo) ? 1 : pageNo;//当前页码
        pageSize = EmptyUtils.isEmpty(pageSize) ? 10 : pageSize;//页大小
        Page page = new Page(pageNo, pageSize, total);//赋值
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripListCommentVO> itripCommentList = itripCommentMapper.getItripCommentListByMap(param);//查询
        page.setRows(itripCommentList);
        return page;
    }

    @Override
    public ItripScoreCommentVO getAvgAndTotalScore(Long hotelId) throws Exception {
        return itripCommentMapper.getCommentAvgScore(hotelId);
    }
}
