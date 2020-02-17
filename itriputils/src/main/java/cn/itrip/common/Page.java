package cn.itrip.common;

import java.util.List;

/**
 * 分页类
 */
public class Page<T> {
    private Integer curPage;
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 记录数
     */
    private Integer pageCount;
    /**
     * 起始位置
     */
    private Integer beginPos;
    /**
     * 集合
     */
    private List<T> rows;

    public Page() {
    }

    public Page(int curPage, Integer pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    public Page(int curPage, Integer pageSize, Integer total) {
        super();
        this.curPage = curPage;//当前页码
        this.pageSize = pageSize;//页大小
        this.total = total;//总记录数
        //计算总页数
        this.pageCount = (total + this.pageSize - 1) / this.pageSize;
        this.beginPos = (curPage - 1) * pageSize;

    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getBeginPos() {
        return beginPos;
    }

    public void setBeginPos(Integer beginPos) {
        this.beginPos = beginPos;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
