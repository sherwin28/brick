package net.isger.brick.stub;

import net.isger.brick.stub.dialect.Page;
import net.isger.brick.stub.dialect.Sort;

public class SearchCondition extends Condition {

    private Page page;

    private Sort sort;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public void setTarget(String target) {
        super.setTarget(target == null ? StubCommand.OPERATE_SEARCH : target);
    }

}
