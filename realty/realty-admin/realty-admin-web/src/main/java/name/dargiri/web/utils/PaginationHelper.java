package name.dargiri.web.utils;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dionis on 19/01/15.
 */
public class PaginationHelper {
    private final long size;
    private final int currentPage;
    private final int resultsOnPage;
    private String location;

    public PaginationHelper(long size, int currentPage, int resultsOnPage, String location) {
        Assert.isTrue(resultsOnPage > 0);
        this.size = size;
        this.currentPage = currentPage;
        this.resultsOnPage = resultsOnPage;
        this.location = location;
    }

    public boolean isCurrentPage(int pageNumber) {
        return currentPage == pageNumber;
    }

    public boolean hasNext() {
        return currentPage < getPageTotalCount();
    }

    public boolean isHasNext() {
        return hasNext();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int pageNumber) {
        throw new UnsupportedOperationException();
    }

    public boolean isListable() {
        return getPageTotalCount() > 1;
    }

    public boolean isHasPrev() {
        return hasPrev();
    }

    public boolean hasPrev() {
        return currentPage > 1;
    }

    public int getPrevPage() {
        return currentPage - 1;
    }

    public int getNextPage() {
        return currentPage + 1;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Integer> getPages() {
        final int size = getPageTotalCount();
        List<Integer> result = new ArrayList<>(size);
        for (int idx = 0; idx < size; idx++) {
            result.add(idx + 1);
        }
        return result;
    }

    public int getPageTotalCount() {
        long result = (this.size / resultsOnPage) + (this.size % resultsOnPage != 0 ? 1 : 0);
        return new Long(result).intValue();
    }

    public int getResultsOnPage() {
        return resultsOnPage;
    }

    public long getSize() {
        return size;
    }
}
