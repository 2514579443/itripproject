package cn.itrip.service.labeldic;

import beans.pojo.ItripLabelDic;
import beans.vo.ItripLabelDicVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import dao.labeldic.ItripLabelDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripLabelDicServiceImpl implements ItripLabelDicService {
    @Resource
    private ItripLabelDicMapper itripLabelDicMapper;

    /**
     * 根据ID查询通用字典
     *
     * @param id
     * @return
     * @throws Exception
     */
    public ItripLabelDic getItripLabelDicById(Long id) throws Exception {
        return itripLabelDicMapper.getItripLabelDicById(id);
    }

    /**
     * 通用字典表多条件查询分页
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicListByMap(param);
    }

    /**
     * 查询记录数
     *
     * @param param
     * @return
     * @throws Exception
     */
    public Integer getItripLabelDicCountByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicCountByMap(param);
    }

    /**
     * 添加
     *
     * @param itripLabelDic
     * @return
     * @throws Exception
     */
    public Integer itriptxAddItripLabelDic(ItripLabelDic itripLabelDic) throws Exception {
        itripLabelDic.setCreationDate(new Date());
        return itripLabelDicMapper.insertItripLabelDic(itripLabelDic);
    }

    /**
     * 修改
     *
     * @param itripLabelDic
     * @return
     * @throws Exception
     */
    public Integer itriptxModifyItripLabelDic(ItripLabelDic itripLabelDic) throws Exception {
        itripLabelDic.setModifyDate(new Date());
        return itripLabelDicMapper.updateItripLabelDic(itripLabelDic);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Integer itriptxDeleteItripLabelDicById(Long id) throws Exception {
        return itripLabelDicMapper.deleteItripLabelDicById(id);
    }

    /**
     * 分页实现
     *
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    public Page<ItripLabelDic> queryItripLabelDicPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripLabelDicMapper.getItripLabelDicCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? 1 : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? 10 : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripLabelDic> itripLabelDicList = itripLabelDicMapper.getItripLabelDicListByMap(param);
        page.setRows(itripLabelDicList);
        return page;
    }


    /**
     * 根据parentId查询数据字典
     *
     * @param parentId
     * @return
     * @throws Exception add by hanlu
     */
    public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId) throws Exception {
        return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
    }
}
