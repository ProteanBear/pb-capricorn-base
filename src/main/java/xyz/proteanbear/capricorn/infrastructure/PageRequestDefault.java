package xyz.proteanbear.capricorn.infrastructure;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * <p>基础层：通用分页请求</p>
 *
 * @author 马强
 */
public class PageRequestDefault {
    /**
     * Page number,starting from 1
     */
    @NotNull(message = "必须指定当前的页码")
    private Integer page;

    /**
     * Number per page
     */
    @NotNull(message = "必须指定当前每页的数量")
    @Min(value = 0L,message = "每页数量必须大于0")
    @Max(value = 100L,message = "每页数量不能大于100")
    private Integer size;

    public Pageable to() {
        return PageRequest.of(page-1, size);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
