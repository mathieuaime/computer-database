package com.excilys.computerdatabase.dtos;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
    private List<T> objects;
    private int page;
    private int pageSize;
    private String search;
    private String order;
    private String column;

    /**
     * Default Page constructor.
     */
    public Page() {
        this(new ArrayList<T>(), 1, 10, null, "ASC", "name");
    }

    /**
     * Page constructor with objects.
     * @param objects list of T objects
     * @param page numero of the page
     * @param pageSize size of the page
     */
    public Page(List<T> objects, int page, int pageSize) {
        this.objects = objects;
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * Page constructor.
     * @param page numero of the page
     * @param pageSize size of the page
     */
    public Page(int page, int pageSize) {
        this(new ArrayList<T>(), page, pageSize);
    }

    /**
     * Page constructor.
     * @param page numero of the page
     * @param pageSize size of the page
     * @param search value for search
     * @param order value for order
     * @param column value for column
     */
    public Page(int page, int pageSize, String search, String order, String column) {
        this(new ArrayList<T>(), page, pageSize, search, order, column);
    }

    /**
     * Page constructor.
     * @param objects list of T objects
     * @param page numero of the page
     * @param pageSize size of the page
     * @param search value for search
     * @param order value for order
     * @param column value for column
     */
    public Page(List<T> objects, int page, int pageSize, String search, String order, String column) {
        super();
        this.objects = objects;
        this.page = page;
        this.pageSize = pageSize;
        this.search = search;
        this.order = order;
        this.column = column;
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getObjectNumber() {
        return objects.size();
    }
}
