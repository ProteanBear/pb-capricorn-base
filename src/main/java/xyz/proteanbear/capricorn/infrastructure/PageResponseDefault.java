package xyz.proteanbear.capricorn.infrastructure;

import org.springframework.data.domain.Page;

/**
 * <p>基础层：返回数据结构（带分页信息）默认</p>
 *
 * @author 马强
 */
public class PageResponseDefault {
    /**
     * 生成处理成功时的分页数据返回内容
     *
     * @param message  消息内容
     * @param pageData 分页数据
     */
    public static PageResponseDefault success(String message, Page<? extends Object> pageData) {
        PageResponseDefault result = new PageResponseDefault();
        result.setStatus("SUCCESS");
        result.setMessage(message);
        result.setData(pageData.getContent());
        result.setPagination(Pagination.of(pageData));
        return result;
    }

    /**
     * Status code
     */
    private String status;

    /**
     * return message
     */
    private String message;

    /**
     * data content
     */
    private Object data;

    /**
     * Pagination Information
     */
    private Pagination pagination;

    /**
     * Constructor
     *
     * @param status  status code
     * @param message return message
     * @param data    data content
     */
    public PageResponseDefault(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * constructor
     */
    public PageResponseDefault() {
    }

    /**
     * @return status code
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status status code
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message return message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return data content
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data data content
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return pagination information
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * @param pagination pagination information
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Pagination
     */
    public static class Pagination {
        /**
         * 通过分页数据生成最终输出的内容
         *
         * @param page 分页内容
         */
        public static Pagination of(Page<?> page) {
            Pagination pagination = new Pagination();
            pagination.setPage(page.getNumber() + 1);
            pagination.setSize(page.getSize());
            pagination.setTotal(page.getTotalElements());
            pagination.setTotalPages(page.getTotalPages());
            return pagination;
        }

        /**
         * Page number,starting from 1
         */
        private int page;

        /**
         * Number per page
         */
        private int size;

        /**
         * Total number
         */
        private long total;

        /**
         * Total pages
         */
        private int totalPages;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
    }
}