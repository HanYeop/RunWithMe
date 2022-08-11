package com.ssafy.gumid101.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable {

    private final long offset;
    private final int limit;

    // this attribute can be let out if you don't need it
    private Sort sort;

    public OffsetBasedPageRequest(Long offset, Integer limit, Sort sort) {
        if (offset == null || offset < 0) {
        	offset = 0L;
        }

        if (limit == null || limit <= 10) {
        	limit = 10;
        }

        this.offset = offset;
        this.limit = limit;

        if (sort == null) {
//            this.sort = new Sort();
        }
    }

    public OffsetBasedPageRequest(Long offset, Integer limit) {
        this(offset, limit, null);
    }

    @Override
    public int getPageNumber() { return 0; }

    @Override
    public int getPageSize() { return limit; }

    @Override
    public long getOffset() { return offset; }

    @Override
    public Sort getSort() { return this.sort; }

    @Override
    public Pageable next() { return null; }

    @Override
    public Pageable previousOrFirst() { return this; }

    @Override
    public Pageable first() { return this; }

    @Override
    public Pageable withPage(int pageNumber) { return null; }

    @Override
    public boolean hasPrevious() { return false; }
}