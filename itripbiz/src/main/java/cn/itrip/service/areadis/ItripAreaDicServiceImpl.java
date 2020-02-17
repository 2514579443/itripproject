package cn.itrip.service.areadis;

import beans.pojo.ItripAreaDic;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import dao.areadic.ItripAreaDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripAreaDicServiceImpl implements ItripAreaDicService {
    @Resource
    private ItripAreaDicMapper itripAreaDicMapper;

    /**
     * 根据ID查询区域
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ItripAreaDic getItripAreaDicById(Long id) throws Exception {
        return itripAreaDicMapper.getItripAreaDicById(id);
    }

    /**
     * 多条件区域查询可分页
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicListByMap(param);
    }

    /**
     * 查询记录数
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public Integer getItripAreaDicCountByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicCountByMap(param);
    }

    /**
     * 添加区域
     * @param itripAreaDic
     * @return
     * @throws Exception
     */
    @Override
    public Integer itriptxAddItripAreaDic(ItripAreaDic itripAreaDic) throws Exception {
        itripAreaDic.setCreationDate(new Date());
        return itripAreaDicMapper.insertItripAreaDic(itripAreaDic);
    }

    /**
     * 修改区域
     * @param itripAreaDic
     * @return
     * @throws Exception
     */
    @Override
    public Integer itriptxModifyItripAreaDic(ItripAreaDic itripAreaDic) throws Exception {
        itripAreaDic.setModifyDate(new Date());
        return itripAreaDicMapper.updateItripAreaDic(itripAreaDic);
    }

    /**
     * 删除区域
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Integer itriptxDeleteItripAreaDicById(Long id) throws Exception {
        return itripAreaDicMapper.deleteItripAreaDicById(id);
    }

    /**
     * 分页
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<ItripAreaDic> queryItripAreaDicPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripAreaDicMapper.getItripAreaDicCountByMap(param);//记录总数
        pageNo = EmptyUtils.isEmpty(pageNo) ? 1 : pageNo;//当前页码
        pageSize = EmptyUtils.isEmpty(pageSize) ? 10: pageSize;//页大小
        Page page = new Page(pageNo, pageSize, total);//赋值
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripAreaDic> itripAreaDicList = itripAreaDicMapper.getItripAreaDicListByMap(param);//查询
        page.setRows(itripAreaDicList);
        return page;
    }
}
