package cn.itrip.service.image;

import beans.pojo.ItripImage;
import beans.vo.ItripImageVO;

import java.util.List;
import java.util.Map;

public interface ItripImageService {
    public ItripImage getItripImageById(Long id)throws Exception;

    public List<ItripImageVO> getItripImageListByMap(Map<String,Object> param)throws Exception;

    public Integer getItripImageCountByMap(Map<String,Object> param)throws Exception;

    public Integer itriptxAddItripImage(ItripImage itripImage)throws Exception;

    public Integer itriptxModifyItripImage(ItripImage itripImage)throws Exception;

    public Integer itriptxDeleteItripImageById(Long id)throws Exception;
}
