package cn.itrip.service.image;

import beans.pojo.ItripImage;
import beans.vo.ItripImageVO;
import dao.image.ItripImageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripImageServiceImpl implements ItripImageService {

    @Resource
    private ItripImageMapper itripImageMapper;

    public ItripImage getItripImageById(Long id) throws Exception {
        return itripImageMapper.getItripImageById(id);
    }

    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageListByMap(param);
    }

    public Integer getItripImageCountByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageCountByMap(param);
    }

    public Integer itriptxAddItripImage(ItripImage itripImage) throws Exception {
        itripImage.setCreationDate(new Date());
        return itripImageMapper.insertItripImage(itripImage);
    }

    public Integer itriptxModifyItripImage(ItripImage itripImage) throws Exception {
        itripImage.setModifyDate(new Date());
        return itripImageMapper.updateItripImage(itripImage);
    }

    public Integer itriptxDeleteItripImageById(Long id) throws Exception {
        return itripImageMapper.deleteItripImageById(id);
    }
}
